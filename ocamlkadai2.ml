(*課題1選択ソート*)
let rec sort list = match list with
[]->[]
| [a] -> [a]
| head :: head2 :: tale -> let rec minimum lst = match lst with
[] -> failwith "Error"
| [a] -> a
| first :: second :: rest -> if first < second then minimum (first :: rest)
else minimum (second :: rest)
in
let rec delete lis num = match lis with
[] -> []
|[b] -> if b = num then [] else [b]
|delhead::delrest -> if delhead = num then delrest else delhead :: (delete delrest num)
in
if head = minimum list then head :: sort (head2 :: tale)
else (minimum list) :: sort (head :: delete (head2 :: tale) (minimum list) )
;;


(*挿入ソート補助関数comp*)
let rec comp a list= match list with
[]->[a]
|head :: tail -> if head > a then a :: list
else head :: (comp a tail)
;;

(*課題1挿入ソート*)
let rec insort list = match list with
[]->[]
| head :: tail -> comp head (insort tail) 
;;



(*課題2微分係数を求めるプログラム*)
let kadai2 f a =
let h = 0.0001 in
 ( ( f (a+.h) -. f (a) ) /. h)
;;

(*課題2関数fの極値を求めるプログラム*)
let rec ext f x =
let abs y = if y >0. then y
else if y < 0. then -. y
else 0. in
let dx =0.00001 in
let i = 0.0000001 in
if abs ( ( f (x+.dx) -. f x ) /. dx)  < i then f (x)
else if ( ( f (x+.dx) -. f x ) /. dx) > ( ( f (x+.2.*.dx) -. f x ) /. (2.*.dx)) then ext f (x +. dx)
else ext f (x -. dx/. 2. )
;;


(*課題3*)
let tra a b h = (a+.b)*. h/. 2.
;;
(*補助関数*)
let rec prekadai3 f a b =
(f(b) +. f(a)) *. (b -. a) /. 2. (*(上底+下底)*高さ/2*)
;;

(*本命関数*)
let rec kadai3 f a b =
let i = 0.01
in
if (a +. i) < b then (prekadai3 f a (a +. i)) +. (kadai3 f (a +. i) b)
else 0.

;;


(*課題4コラッツ予想*)
let rec kadai4 n =
if n=1 then 1
else if n mod 2 = 0 then kadai4 (n/2)
else kadai4 (n*3+1)
;;



(*kadai5 hojyo*)(*二点間の距離*)
let rec nagasa (x1, y1) (x2, y2) =
sqrt( (float_of_int x2 -. float_of_int x1)*.(float_of_int x2 -. float_of_int x1) +. (float_of_int y2 -. float_of_int y1)*.(float_of_int y2 -. float_of_int y1))
;;



(*kadai5 hojyo3*)(*指定した要素をリストから消し、残ったリストを返す*)
let rec delete5 (x1,y1) list = match list with
[]->[]
|head :: tail -> if head = (x1,y1) then tail
else head :: delete5 (x1,y1) tail 
;;

(*kadai5 hojyo2*)
let rec nagasa2 (x1, y1) list = match list with (*最長になる点見つけてその座標返す*)
[]->(x1,y1)
|[a] -> a
|(x2, y2) :: (x3,y3) :: tail -> if nagasa (x1,y1) (x2, y2) > nagasa (x1,y1) (x3,y3)
then nagasa2 (x1,y1) ((x2,y2) :: tail)
else nagasa2 (x1,y1) ((x3,y3) :: tail)
;;

(*課題５最長閉路,整列されたリストを返す*)
let rec kadai5 list = match list with
[]->[]
|[a] -> [a]
| (x1,y1) :: (x2,y2) :: (x3,y3) :: rest -> 
(x1,y1) :: 
kadai5 (   nagasa2 (x1,y1) ((x2,y2) :: (x3,y3) :: rest)  :: (delete5 (nagasa2 (x1,y1) ((x2,y2) :: (x3,y3) :: rest) )    ((x2,y2) :: (x3,y3) :: rest))       )
|head :: tail -> head :: tail
;;

let rec kyori list (*list ->  goukeikyori*)
=match list with
[] -> 0.
|first :: second :: rest -> (nagasa first second) +. (kyori (second :: rest))
|first :: rest -> 0.
;;

 
let rec length lst = match lst with
[] -> 0
| _ :: rest -> 1 + (length rest)
;;

let rec posl n list = match list with
[] -> failwith "Not Exist..."
| head :: rest -> if n > 1 then posl (n-1) rest
else if n=0 then failwith "Not Exist..."
else head
;;



let kadai51 list = (*本命の関数*)
let a = (kadai5 list)
in
let b = kyori (kadai5 list) +. (nagasa(posl 1 (kadai5 list)) (posl (length (kadai5 list)) (kadai5 list)))  (*最後始点に戻ってくる分の長さを足した*)
in
(a,b)
;;



(*課題6フィボナッチ数*)
let rec fibo n =
if n=1 ||  n=2 then 1
else fibo(n-1) + fibo(n-2)
;;
