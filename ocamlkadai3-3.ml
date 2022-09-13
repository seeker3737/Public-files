module Student :
sig
exception Not_found
exception Empty
type 'a student
  val empty: 'a student
  val add: 'a -> string -> 'a student -> 'a student
  val find: string -> 'a student -> 'a
  val show: int student -> unit
  val is_empty: 'a student -> bool
end
=struct
exception Not_found
exception Empty
type 'a student = Nil | Node of 'a * string * 'a student

let empty = Nil

let add a b c = Node (a,b,c)

let rec find x = function
Nil -> raise Not_found
| Node (a,b,c) ->if b = x then a
else find x c  

let rec show = function
Nil -> ()
|Node (a,b,c) -> show c;
print_int a ; print_string ":" ; print_string b ;
print_newline ()

let rec is_empty = function
Nil -> true
|Node (a,b,c) -> false

end;;
open Student ;;




