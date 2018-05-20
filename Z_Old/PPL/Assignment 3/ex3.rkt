#lang racket

; Signature: or-map(f lst)
; Type: [[T->Bool]*LIST->Bool]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: List is filled with type T variables.
; Tests: (or-map-seq-filter odd? (list 1 2 3 4)) => #t
(define or-map
  (lambda (f lst)
		(cond 
                   ((null? lst) #f)
			((f (car lst)) #t)
			(else (or-map f (cdr lst)))
	)
))

; Signature: or-map-seq-filter(f lst)
; Type: [[T->Bool]*LIST->Bool]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: List is filled with type T variables.
; Tests: (or-map-seq-filter odd? (list 1 2 3 4)) => #t
(define or-map-seq-filter
  (lambda (f lst)
		(cond 
                  ((null? lst) #f)
                  (else (not (null? (filter f lst)))))
))

; Signature: or-map-seq-acc(f lst)
; Type: [[T->Bool]*LIST->Bool]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: List is filled with type T variables.
; Tests: (or-map-seq-acc null? (list 1 2 3 4)) => #f
(define or-map-seq-acc
  (lambda (f lst)
    (letrec 
      ((g (lambda(x1 x2) (or x1 x2))))
      (accumulate g #f (map f lst))
      )
    ))

; Signature:  or-map-c-f(f)
; Type: [[T1->T2] -> [LIST->T2]]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: LIST is filled with type T1 variables.
; Tests: ((or-map-c-f even?) (list 1 2 3 4)) => #t
(define or-map-c-f
  (lambda (f)
    (lambda (lst)
		(cond 
                   ((null? lst) #f)
			((f (car lst)) #t)
			(else ((or-map-c-f f) (cdr lst)))
      ))
    ))


; Signature:  or-map-c-lst(lst)
; Type: [LIST->[[T1->T2]->T2]]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: LIST is filled with type T1 variables.
; Tests: ((or-map-c-lst (list 1 2 3 4)) odd?) => #t
(define or-map-c-lst
  (lambda (lst)
    (lambda (f)
		(cond 
                   ((null? lst) #f)
			((f (car lst)) #t)
			(else ((or-map-c-lst (cdr lst)) f))
      ))
))
; Signature: or-map-tree(f lst)
; Type: [[T1->Bool]*T2->Bool]
; Purpose: Check if any of the variables in LIST returns true in the function.
; Pre-Conditions: List is filled with type T variables.
; Tests: (or-map-tree even? (list (list 1 2) (list 3 4))) => #t
(define or-map-tree
  (lambda (f tree)
    (accumulate 
         (lambda (x1 x2) (or x1 x2))
         #f
         (map 
          (lambda (x)
            (cond 
              ((not (list? x)) (f x))
              (else 
                (or-map-tree f x)
              )))
          tree))))

; Signature: make-lzl(x, lzl-tail)
; Type: [T*[->list]->[->list]]
; Purpose: Create a lazy list.
; Pre-Conditions: none.
(define make-lzl
  (lambda (head wrapped-tail)
    (lambda () (list head wrapped-tail))
))

; Signature: head(lzl)
; Type: [[->list]->T]
; Purpose: Get the head of a lazy list
; Pre-Conditions: none.
(define head
  (lambda (lzl)
    (car (lzl))
))

; Signature: tail(lzl)
; Type: [[->list]->[->list]]
; Purpose: Get the tail of a lazy list
; Pre-Conditions:none.
(define tail
  (lambda (lzl)
    (car (cdr (lzl)))
))

; Signature: take(lzl, n)
; Type: [[->list]->list]
; Purpose: Returns a list of the first n elements in the lazy list.
; Pre-Conditions:none.
(define take
  (lambda (lzl n)
    (cond
      ((= n 1) (list (head lzl)))
      (else (cons (head lzl) (take (tail lzl) (- n 1))))
    )
))

; Signature: drop(lzl, n)
; Type: [[->list]->[->list]]
; Purpose: Drop the first n elements in a lazy list.
; Pre-Conditions: none.
(define drop
  (lambda (lzl n)
    (cond
      ((= n 1) (tail lzl))
      (else (drop (tail lzl) (- n 1)))
  )
))

; Signature: nth(lzl,n)
; Type: [[->list]->T]
; Purpose: Returns the nth element of lazy list.
; Pre-Conditions: none.
(define nth
  (lambda (lzl n)
    (cond
      ((= n 1) (head (lzl)))
      (else (drop (tail lzl) (- n 1)))
  )
))

; Signature: lzl?(lzl)
; Type: [T->Boolean]
; Purpose: Check if lzl is a lazy list.
; Pre-Conditions: none.
(define lzl?
  (lambda (lzl)
    (cond 
      ((procedure? lzl) (list? (lzl)))
      (else #f)
    )
))

; Signature: equal-prefix-lzl?(lzl1,lzl2,n)
; Type: [[->list]*[->list]*Number->Boolean]
; Purpose: Check if the first n elements of lzl1 and lzl2 are eqaul.
; Pre-Conditions: none.
(define equal-prefix-lzl?
  (lambda (lzl1 lzl2 n)
    (cond
      ((= n 0) (= (head lzl1) (head lzl2)))
      (else (and (= (head lzl1) (head lzl2)) (equal-prefix-lzl? (tail lzl1) (tail lzl2) (- n 1))))
    )
))

; Signature: lzl-pow(lzl,k)
; Type: [[->list]*T->[->list]]
; Purpose: Raise every item in lzl to the power of k.
; Pre-Conditions: k is natural.
(define lzl-pow
  (lambda (lzl k)
   (lambda() (list (expt (head lzl) k) (lambda () (lzl-pow (tail lzl)))))
   ))

; Signature: lzl-apply(f,init)
; Type: [[T->T]*T->[->list]]
; Purpose: Create a lazy list with incrementation by f.
; Pre-Conditions: f has a stop condition that returns 'end_of_lazy_list.
(define lzl-apply
  (lambda (f init)
    (cond 
      ((equal? init 'end_of_lazy_list) '..)
      (else (make-lzl init (lzl-apply f (f init))))
    )
))

; Signature: group-generator(p,g)
; Type: [Number*Number->[->list]]
; Purpose: Create a list of g^n mod p results.
; Pre-Conditions: -
(define group-generator
  (lambda (p g)
    (lzl-apply (lambda () (modulo (* g g) p)) (modulo g p))
    ))

; Signature: integers-until(condi,init)
; Type: [[Number->Boolean]*Number -> [->list]]
; Purpose: Create a list of integers from init until condi returns true.
; Pre-Conditions: -
(define integers-until
  (lambda (condi init)
    (lzl-apply
     (lambda (n)   
       (if 
        (condi n)
        'end_of_lazy_list
        (+ n 1)))
     init)
))

; Signature: lzl-cyclic?(lzl,bound)
; Type: [[->List]*Number ->  String/Boolean]
; Purpose: Identify cycles within a lazy list.
; Pre-Conditions: -
(define lzl-cyclic?
  (lambda (lzl bound)
    (lzl-cyclic-iter lzl (tail lzl) bound))
)

; Signature: lzl-cyclic-iter(lzl-slow,lzl-fast,bound)
; Type: [[->List] * [->List] * Number -> T] 
; Purpose: An iterative function to be wrapped by 'lzl-cyclic?'.
; Pre-Conditions: -
(define lzl-cyclic-iter (lambda (lzl-slow lzl-fast bound)
                          (cond ((equal? (head lzl-fast) 'end_of_lazy_list) #f)
                                ((equal? (head lzl-slow) (head lzl-fast)) #t)
                                ((= bound 0) '?)
                                (else (lzl-cyclic-iter  (tail lzl-slow) (tail (tail lzl-fast))  (sub1 bound)  )))))

    
;Signature: append(list1, list2)
;Purpose: return a list which the arguments lists, from left to right. Append list2 to list1.
;Type: [List * List -> List]
;Example: 
;(append$$ '(1 2) '(3 4) id) should produce '(1 2 3 4)
;Tests: (append$ '() '(3 4) id) ==> '(3 4)
;(append$ '(1 2) '(3 4) (lambda(x) (map (lambda(x)(+ x 1)) x)))  ==> '(2 3 4 5)
(define append$ (lambda (x y cont)
    (if (null? x)
        (cont y)
        (append$ (cdr x) y (lambda (y) (cont (cons (car x) y) ) ))
)))

; Signature: equal-trees$(tree1, tree2, succ, fail)
; Type: [list*list*[list->list]*[list->list]->list]
; Purpose: check if trees have equal structure. If true, get the pairs of different leaf values, and if false, get first sub-tree difference.
; Pre-Conditions: t1 and t1 are trees. succ and fail are closures [list->list]
(define equal-trees$ (lambda (tree1 tree2 succ fail)
    (cond ( (and (null? tree1) (null? tree2))                           (succ null)               ) ; the nodes are totally equal.
          ( (and (not (list? tree1)) (not (list? tree2)) ) (succ (list (cons tree1 tree2)) ) ) ;the nodes are leaves that aren't equal.
          ( (or (not (list? tree1))  (not (list? tree2)) ) (fail (list tree1 tree2)) )
          ( (or (null? tree1)        (null? tree2)       ) (fail (list tree1 tree2)) )
          (else      (equal-trees$ 
                     (car tree1)
                     (car tree2)
                     (lambda (car-list) (equal-trees$ (cdr tree1) (cdr tree2) (lambda (cdr-list) (succ (append car-list cdr-list))) fail) )
                      fail))


    )))

; Signature: is-consistent(list1)
; Type: [list->boolean]
; Purpose: checks if a list of pairs is consistent - meaning that if two cars are equal, then the two cdr are equal too.
; Pre-Conditions: the list is a list of pairs.
(define is-consistent?
  (letrec ((check-pair (lambda (p l) (if (or (null? l) (equal? (car p) (cdr p) ) )
                                #t
                                (and (or (not (equal? (car p) (car (car l)))) (equal? (car (car l)) (cdr (car l)) ) (equal? (cdr p) (cdr (car l)))) (check-pair p (cdr l)))
                                )) ))
  
  (lambda (lst)
   (if (null? lst)
       #t
       (and (check-pair (car lst) (cdr lst)) (is-consistent? (cdr lst)) )))
  
  ))

; Signature: te-eq$(t1 t2 succ fail)
; Type: [list*list*[Boolean->Boolean]*[Boolean->Boolean]->Boolean]
; Purpose: check if two typing cons' are equal
; Pre-Conditions: t1, t2 are trees. succ and fail are [Boolean->Boolean]
(define te-eq$
  (lambda (t1 t2 succ fail)
    (if (is-consistent? (equal-trees$ t1 t2 (lambda (x) x) (lambda (x) (list (cons 1 2) (cons 1 3))) ) ) (succ #t)
    (fail #f)
    )) )

;Signature: accumulate(op,initial,sequence)
;Purpose: Accumulate by ’op’ all sequence elements, starting (ending)
;with ’initial’
;Type: [[T1*T2 -> T2]*T2*LIST(T1) -> T2]
(define accumulate
(lambda (op initial sequence)
  (if (null? sequence) initial 
      (op (car sequence)
      (accumulate op initial (cdr sequence))))
  ))