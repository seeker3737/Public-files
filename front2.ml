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


(*構文解析*)

module Parser = struct

module L = Lexer

(* module E = Evaluator *)
let tok = ref (L.ONE ' ')
let getToken () = L.gettoken ()
let advance () = (tok := getToken(); L.print_token (!tok))

exception Syntax_error
let error () = raise Syntax_error
let check t = match !tok with
L.CID _ -> if (t = (L.CID "")) then () else error()
| L.VID _ -> if (t = (L.VID "")) then () else error()
| L.NUM _ -> if (t = (L.NUM "")) then () else error()
| tk -> if (tk=t) then () else error()

let eat t = (check t; advance())

let rec clauses() = match !tok with
L.EOF -> ()
| _ -> (clause(); clauses())

and clause() = match !tok with
L.ONE '(' -> (term(); eat(L.ONE '.'))
| _ -> (predicate(); to_opt(); eat(L.ONE '.'))


and to_opt()= match !tok with
L.TO -> (eat(L.TO);terms())
| _ -> ()


and command() = match !tok with
L.QUIT -> exit 0
| L.OPEN -> (eat(L.OPEN);
match !tok with
L.CID s -> (eat(L.CID ""); check (L.ONE '.');
L._ISTREAM := open_in (s^".pl");
advance(); clauses(); close_in (!L._ISTREAM))
| _ -> error())
| _ -> (term(); check(L.ONE '.'))




and term() = match !tok with
L.ONE '(' -> (eat(L.ONE '('); term(); eat(L.ONE ')') )
| _ -> predicate()

and terms() = term();t'()

and t'() = match !tok with
L.ONE ',' -> (eat(L.ONE ',');term();t'())
| _ -> ()

and predicate() = eat(L.CID ""); eat(L.ONE '(') ; args(); eat(L.ONE ')')


and args() = expr();e'()

and e'() = match !tok with
L.ONE ',' -> (eat(L.ONE ',');expr();e'())
| _ -> ()

and expr() = match !tok with
L.ONE '(' -> (eat(L.ONE '(' ); expr(); eat(L.ONE ')') )
|L.ONE '[' -> (eat(L.ONE '['); list(); eat(L.ONE ']') )
|L.CID _ -> (eat(L.CID ""));tail_opt()
|L.VID _ -> eat(L.VID "")
|L.NUM _ -> eat(L.NUM "") 
| _ -> error()

and tail_opt() = match !tok with
L.EOF -> ()
|L.ONE '(' -> ( eat(L.ONE '('); args(); eat(L.ONE ')') )
| _ -> ()


and list() = match !tok with
L.ONE ']' -> ()
| _ -> (expr(); list_opt())


and list_opt()= match !tok with
L.ONE '|' -> (eat(L.ONE '|'); id())
|L.ONE ',' -> (eat(L.ONE ',');list())
| _ -> ()

and id() = match !tok with
L.CID _ -> eat(L.CID ""); 
|L.VID _ -> eat(L.VID "")
|L.NUM _ -> eat(L.NUM "")

end


let rec run() =
print_string "?- ";
while true do
flush stdout; Lexer._ISTREAM := stdin;
Parser.advance(); Parser.command(); print_string "\n?- "
done








