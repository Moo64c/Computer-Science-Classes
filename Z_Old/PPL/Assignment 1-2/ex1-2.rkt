; You should not remove any of these two lines for test running
; or for submission. Just use the file as is.
#lang racket
(provide (all-defined-out))

; Signature: simple-calc(op n1 n2)
; Type: [Number*Number*Number -> Number]
; Purpose: Apply action determined by op to n1 and n2.
; Pre-condition: 0 <= op <= 3
; Post-conditions: result = closure r, such that op =
; 0 : addition
; 1 : subtraction
; 2 : multiplication
; 3 : division
; AND r(x) = n1 [op] n2.y
; Tests: ((simple-calc(0 1 2)) 1) => 1
; Tests: ((simple-calc(1 2 1)) 1) => 1
; Tests: ((simple-calc(2 1 2)) 2) => 1
; Tests: ((simple-calc(3 4 2)) 2) => 1
(define simple-calc (lambda(op x1 x2)
  (cond
   [(equal? 0 op) (+ x1 x2)]
   [(equal? 1 op) (- x1 x2)]
   [(equal? 2 op) (* x1 x2) ]
   [(equal? 3 op) (/ x1 x2)])))

; Signature: advanced-calc(calc op n1 n2)
; Type: [Number*Number*Number*Number -> Number]
; Purpose: Apply action determined by calc and op to n1 and n2.
; Pre-condition: 0 <= op <= 5
; Post-conditions: result = closure r, such that r(x) =
; 0-3 : (calc op x1 x2)
; 4 : (expt x1 x2)
; 5 : (average x1 x2)
; AND r(x) = n1 [op] n2.
; Tests: ((advanced-calc simple-calc 0 1 2) 1) => 1
; Tests: ((advanced-calc simple-calc 2 1) 1) => 1
; Tests: ((advanced-calc simple-calc 4 2 2) 4) => 1
; Tests: ((advanced-calc simple-calc 5 4 2) 3) => 1
(define advanced-calc (lambda(calc op x1 x2)
  (cond
    [(and (> op -1) (> 4 op)) (calc op x1 x2)]
    [(equal? op 4) (expt x1 x2)]
    [(equal? op 5) (average x1 x2)])))

;----------------;
;  question 3-c  ;
;----------------;
; Signature:fun-add(f g)
; Type:[[Number->Number]*[Number->Number]->[Number->Number]]
; Purpose: given two functions f,g construct a function that given a number x returns 
; the sum of applying f to x and g to x 
; Pre-conditions: true
; Post-condition: result = closure r, such that r(x) = f(x)+g(x)
; Tests:((fun-add (lambda(x)x) (lambda(x)1)) 0)=>1
(define fun-add 
  (lambda(f g) 
    (lambda(x)(+ (f x) (g x))
   )))

; Signature:fun-sub(f g)
; Type:[[Number->Number]*[Number->Number]->[Number->Number]]
; Purpose: given two functions f,g construct a function that given a number x returns 
; the difference of f applied to x and g applied to x 
; Pre-conditions: true
; Post-condition: result = closure r, such that r(x) = f(x)-g(x)
; Tests:((fun-sub (lambda(x)x) (lambda(x)1)) 0)=>-1
  (define fun-sub 
    (lambda(f g) 
      (lambda(x)(- (f x) (g x))
   )))


; Signature:average(x1 x2)
; Type:[Number*Number->Number]
; Purpose: Given two numbers x1, x2 calculate their average.
; Pre-conditions: true
; Post-condition: result = (x1+x2)/2
; Tests:((average (3 5) 0)=>4
(define average (lambda(x1 x2) (/ (+ x1 x2) 2)))