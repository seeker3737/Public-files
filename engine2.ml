module Lexer = struct

type token = CID of string | VID of string | NUM of string
| TO | IS | QUIT | OPEN | EOF | ONE of char

module P = Printf
exception End_of_system
let _ISTREAM = ref stdin
let ch = ref []
let read () = match !ch with [] -> input_char !_ISTREAM
| h::rest -> (ch := rest; h)

let unread c = ch := c::!ch
let lookahead () = try let c = read () in unread c; c with End_of_file -> '$'

let rec integer i =
(* 文字列として数字を構成 *)
let c = lookahead() in
if(c>='0' && c<='9')then
integer(i^(Char.escaped(read ())))
else i

and identifier id =
let c = lookahead () in
if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
(c >= '0' && c <= '9') || c == '_') then
identifier (id^(Char.escaped (read ())))
else id

and native_token () =
let c = lookahead () in

if (* CID に対する識別子および予約語 *)
(c>='a' && c<='z') then
let id = identifier "" in
match id with
"is" -> IS
|"quit"-> QUIT
|"open" -> OPEN
| _ -> CID (id)

else if (* VID に対する識別子 *)
(c>='A' && c<='Z') then VID(identifier "")

else if (c >= '0' && c <= '9') then NUM (integer "")

else if (* :- を認識して TO を返す *)
(c==':')
then
let _ = read() in
let y=lookahead() in
if(y=='-')then 
let _ =read() in
TO
else ONE(':')

else ONE (read ())


and gettoken () =
try
let token = native_token () in
match token with
ONE ' ' -> gettoken ()
| ONE '\t' -> gettoken ()
| ONE '\n' -> gettoken ()
| _ -> token
with End_of_file -> EOF


let print_token tk =
match tk with
(CID i) -> P.printf "CID(%s)" i
| (VID i) -> P.printf "VID(%s)" i
| (NUM i) -> P.printf "NUM(%s)" i
| (TO) -> P.printf ":-"
| (QUIT) -> P.printf "quit"
| (OPEN) -> P.printf "open"
| (IS) -> P.printf "is"
| (EOF) -> P.printf "eof"
| (ONE c) -> P.printf "ONE(%c)" c


let rec run () =
flush stdout;
let rlt = gettoken () in
match rlt with
(ONE '$') -> raise End_of_system
| _ -> (print_token rlt; P.printf "\n"; run())

end

(*  *)

module Evaluator =
struct

type ast = Atom of string | Var of string | App of string * ast list

module P = Printf
let rec print_ast ast = match ast with
(App(s, hd::tl)) -> (P.printf "App(\"%s\",[" s;
print_ast hd; List.iter (fun x -> (print_string ";"; print_ast x)) tl;
print_string "])")
| (App(s, [])) -> P.printf "App(\"%s\",[])" s
| (Atom s) -> P.printf "Atom \"%s\"" s
| (Var s) -> P.printf "Var \"%s\"" s
let print_ast_list lst = match lst with
(hd::tl) -> (print_string "["; print_ast hd;
List.iter (fun x -> (print_string ";";
print_ast x)) tl; print_string "]")
| [] -> print_string "[]"

let sub name term =
let rec mapVar ast = match ast with
(Atom x) -> Atom(x)
| (Var n) -> if n=name then term else Var n
| (App(n, terms)) -> App(n, List.map mapVar terms)
in mapVar




let mgu (a,b) =
let rec ut (one, another, unifier) = match (one, another) with
([], []) -> (true, unifier)
| (term::t1, Var(name)::t2) ->
let r = fun x -> sub name term (unifier x) in
ut(List.map r t1, List.map r t2, r)
| (Var(name)::t1, term::t2) ->
let r = fun x -> sub name term (unifier x) in
ut(List.map r t1, List.map r t2, r)
| (Atom(n)::t1, Atom(m)::t2) ->
if n=m then ut(t1,t2,unifier) else (false, unifier)
| (App(n1,xt1)::t1, App(n2,xt2)::t2) ->
if n1=n2 && List.length xt1 = List.length xt2 then
ut(xt1@t1, xt2@t2, unifier)
else (false, unifier)
| (_,_) -> (false, unifier);
in ut ([a],[b], (fun x -> x))







let resolution (rule, question) = match (rule, question) with
(head::conds,goal::goals) ->
let (unifiable, unifier) = mgu (head,goal)
in List.map unifier (conds@goals)
| ([], goals) -> goals
| (_, []) -> [] 




let succeed query = (print_ast query; true)



let rename ver term =
let rec mapVar ast = match ast with
(Atom x) -> Atom(x)
|(Var n) -> Var(n^ "#" ^ver)
| (App(n,terms)) -> App(n,List.map mapVar terms)
in mapVar term







exception Compiler_error
(*
let rec solve (program, question, result) = match question with
[] -> succeed result ???
|goal :: goals ->
 let onestep _ clause = match clause with
   [] -> raise Compiler_error
  |head :: conds ->
    let (unifiable, unifier) = mgu(head,goal) in
     if unifiable then
       solve (program, List.map unifier (conds@goals), unifier result)  
          else true
  in List.fold_left onestep true program
*)

let rec solve (program, question,result,depth)=match question with
[] -> succeed result
|goal :: goals ->
let onestep _ clause =
match List.map (rename (string_of_int depth)) clause with
[] -> raise Compiler_error
|head :: conds ->
let (unifiable,unifier)=mgu(head,goal) in
if unifiable then
solve (program,List.map unifier (conds@goals), unifier result,depth+1)
else true
in
List.fold_left onestep true program



let eval (program, question) = solve(program, [question], question,1)


end



















(*構文解析*)

module Parser = struct

module L = Lexer

module E = Evaluator

let tok = ref (L.ONE ' ')
let getToken () = L.gettoken ()
let advance () = (tok := getToken(); L.print_token (!tok))
let prog = ref [[E.Var ""]]

exception Syntax_error
let error () = raise Syntax_error
let check t = match !tok with
L.CID _ -> if (t = (L.CID "")) then () else error()
| L.VID _ -> if (t = (L.VID "")) then () else error()
| L.NUM _ -> if (t = (L.NUM "")) then () else error()
| tk -> if (tk=t) then () else error()

let eat t = (check t; advance())

let rec clauses() = match !tok with
L.EOF -> []
| _ -> let a =clause() in  a::clauses()


and clause() = match !tok with
L.ONE '(' -> let a = term() in eat(L.ONE '.') ; [a]
| _ -> ( let a = predicate() in let b = a :: to_opt() in (eat(L.ONE '.') ; b))


and to_opt()= match !tok with
L.TO -> (eat(L.TO) ; let a = terms() in a)
| _ -> []


and command() = match !tok with
L.QUIT -> exit 0
| L.OPEN -> (eat(L.OPEN);
match !tok with
L.CID s -> (eat(L.CID ""); check (L.ONE '.');
L._ISTREAM := open_in (s^".pl"); advance();
prog := clauses(); close_in (!L._ISTREAM))
| _ -> error())
| _ -> let t = term() in
(check(L.ONE '.'); let  _ = E.eval(!prog, t) in ())



and term() = match !tok with
L.ONE '(' -> eat(L.ONE '('); let a = term() in ( eat(L.ONE ')') ; a )
| _ -> let b =predicate() in b

and terms() = term();t'()

and t'() = match !tok with
L.ONE ',' ->  (eat(L.ONE ',');let a = term() in a :: t'() )
| _ -> []

and predicate() =match !tok with
L.CID x -> eat(L.CID ""); eat(L.ONE '('); let a = args() in ( eat(L.ONE ')' ) ; E.App(x,a) )
| _ -> error()


and args() = expr();e'()

and e'() = match !tok with
L.ONE ',' -> (eat(L.ONE ',');let a = expr() in a :: e'() )
| _ -> []

and expr() = match !tok with
L.ONE '(' -> eat(L.ONE '(' ); let a = expr() in (eat(L.ONE ')'); a)
|L.ONE '[' -> eat(L.ONE '['); let b = list() in (eat(L.ONE ']'); b)
|L.CID x -> (eat(L.CID ""));let c = tail_opt(x) in c
|L.VID y -> eat(L.VID "");E.Var(y)
|L.NUM z -> eat(L.NUM "");E.Atom(z)
| _ -> error()

and tail_opt s = match !tok with
L.ONE '(' -> ( eat(L.ONE '('); let a =args() in  eat(L.ONE ')');  E.App(s, a) )
| _ -> E.Atom(s)


and list() = match !tok with
L.ONE ']' -> E.Atom("nil")
| _ -> (let a = expr() in let b = list_opt() in E.App("cons",[a;b]))


and list_opt()= match !tok with
L.ONE '|' -> eat(L.ONE '|'); let a =id() in a
|L.ONE ',' -> eat(L.ONE ',');let b = list() in b
| _ -> E.Atom("nil")

and id() = match !tok with
L.CID x -> eat(L.CID ""); E.Atom(x)
|L.VID y -> eat(L.VID ""); E.Var(y)
|L.NUM z -> eat(L.NUM ""); E.Atom(z)

end


let rec run() =
print_string "?- ";
while true do
flush stdout; Lexer._ISTREAM := stdin;
Parser.advance(); Parser.command(); print_string "\n?- "

done
let _ = run()





