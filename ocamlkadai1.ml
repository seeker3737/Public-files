(*1:dellt*)
let rec dellt n list = match list with
[] -> []
|head::rest -> if  n > 0 then dellt (n-1) rest
else if n=0 then list
else failwith "Error"
;;

(*2:dellt2*)
let rec dellt2 n list = match list with
[]->[]
| head :: rest ->if n > 1 then head :: dellt2 (n-1) rest else rest
;;

(*3:find*)
let rec find n list = match list with
[]->false
|head::rest-> if head = n then true else find n rest
;;

(*4:posl*)
let rec posl n list = match list with
[] -> failwith "Not Exist..."
| head :: rest -> if n > 1 then posl (n-1) rest
else if n=0 then failwith "Not Exist..."
else head
;;

(*5:divlist*)
let rec divlist lst1 lst2 = match lst2 with
[]->[]
|head::tail->match lst1 with
[]->[]
|n::rest->(n/head):: divlist rest tail
;;

(*6:mul2list*)
let rec mul2list list = match list with
[]->[]
|[a]->[]
| first :: second :: rest ->(first * second) :: mul2list (second :: rest)
;;

(*7:chglist*)
let rec chglist (a, b) list = match list with
[]->[]
| head :: rest -> if head = a then b :: chglist ( a , b ) rest
 else head :: chglist (a,b) rest
;;

(*8:inslist*)
let rec inslist n x list= match list with
[]->[x]
| head :: rest -> if n>1 then head :: inslist (n-1) x rest
else if n=0 then failwith "Error"
else x :: head :: rest
;;

(*9:replicate*)
let rec replicate n a=
if n=1 then [a]
else a :: replicate (n-1) a
;;

(*10:merge*)
let rec merge list1 list2 = match list1 with
[]->list2
| head :: rest -> match list2 with
|[]->[]
| first :: tale -> head :: first  :: merge rest tale
;;

(*11:inside_length*)
let rec inside_length lst =match lst with
[] -> 0
| head :: rest -> let rec length lst =match lst with
[] -> 0
| first :: tale -> 1 + (length tale)
in
length head + inside_length rest
;;


(*12の補助関数append*)
let rec append l1 l2 =
match l1 with
[] -> l2
| first :: rest -> first :: (append rest l2)
;;

(*12:concat*)
let rec concat list = match list with
[]->[]
|[a]->a
|n1::(n2::rest)->concat ((append n1 n2) :: rest) 
;;


(*13:assoc*)
let rec assoc a list = match (a, list) with
(_,[])->failwith "Not found..."
|(_,(x,y)::rest) -> if x=a then y else if y=a then x
else assoc a rest
;;

(*14:maximum*)
let rec maximum list = match list with
[] -> failwith "Error"
| [a] -> a
| first :: second :: rest -> if first < second then maximum (second :: rest)
else maximum (first :: rest)
;;

(*15:index*)
let rec index list a = match list with
[]->failwith "Not found..."
|head::rest->if head=a then 0
else 1+ index rest a
;;


(*16inter*)
let rec inter lst1 lst2 = match lst2 with
[]->[]
|n :: rest ->match lst1 with
[]->[]
|head :: tail -> if (find n lst1)=false then inter lst1 rest
else n :: inter lst1 rest 
;;

(*16union*)
let rec union lst1 lst2 =
let rec preunion lst1 lst2 = match lst2 with
[]->lst1
|n :: rest ->match lst1 with
[]->lst2
|head :: tail -> if find n lst1 = true then preunion (dellt2 ((index lst1 n)+1) lst1) rest
else preunion lst1 rest
in
append (preunion lst1 lst2) lst2
;;


(*16diff*)
let rec diff lst1 lst2=
let rec prediff lst1 lst2 = match lst2 with
[]->lst1
|n :: rest -> match lst1 with
[]->lst2
|head :: tail -> if find n lst1 = true then prediff (dellt2 ((index lst1 n)+1) lst1) rest
else prediff lst1 rest
in
append (prediff lst1 (inter lst1 lst2)) (prediff lst2 (inter lst1 lst2)) 
;;

