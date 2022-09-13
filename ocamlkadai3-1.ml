module List = struct
exception Empty
type 'a list = Nil | Cell of 'a * 'a list

let create = Nil

let rec unshift x = function
    Nil -> Cell(x, Nil)
    |Cell (y,a) -> Cell (x,Cell (y,a))

let rec shift = function
    Nil -> Nil
    |Cell (y,a) -> a

let rec push x = function
Nil -> Nil
|Cell (y,a) -> if a=Nil then Cell(y,Cell (x,Nil))
else Cell(y, push x a)

let rec pop =function
Nil->Nil
|Cell (y,a)->if a=Nil then Nil
else Cell (y,pop a) 

let rec size = function
Nil->0
|Cell (y,a) -> 1 + size a

exception FailerSearch

let rec max = function
Nil-> raise FailerSearch
|Cell(y0, Nil) -> y0
|Cell(y1, Cell(y2, a)) ->if y1>y2 then max (Cell (y1,a))
else max (Cell (y2,a))


let rec min= function
Nil-> raise FailerSearch
|Cell(y0,Nil) -> y0
|Cell(y1, Cell(y2, a))->if y1>y2 then min (Cell (y2,a))
else min (Cell (y1,a))


let rec get x = function
Nil-> failwith "empty"
|Cell(y,a)-> if x=0 then y
else get (x-1) a

let rec indexOf x = function
Nil -> -1
|Cell (y,a)-> if x=y then 0
else 1 + indexOf x a


let rec set x y = function
Nil -> Nil
|Cell (z,a) -> if z=x then Cell (y, set x y a)
else Cell (z, set x y a)



let rec remove x = function
Nil -> Nil
|Cell(y,a) -> if  y=x then remove x a
else Cell(y, remove x a)

let rec concat x = function
Nil->x
|Cell(y,a) -> concat (push y x) a



end;;

open List;;
