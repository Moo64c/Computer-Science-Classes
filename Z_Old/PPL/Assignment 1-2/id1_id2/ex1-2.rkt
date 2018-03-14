; You should not remove any of these two lines for test running
; or for submission. Just use the file as is.
#lang racket
(provide (all-defined-out))

; Replace '... text in this file with a proper code.

;----------------;
;  question 3-a  ;
;----------------;
; Signature: simple-calc(op n1 n2)
; Type: [Number*Number*Number->Number]
; Purpose: a simple calculator to support arithmetic operations based on a given code
; Pre-conditions: op,n1,n2 are integers, 0<=op and op<=3, if op=3 then n2!=0
; Tests: (simple-calc 0 1 2)=>3
(define simple-calc
  (lambda(op n1 n2)
    '...))

;----------------;
;  question 3-b  ;
;----------------;
; Signature: advanced-calc(calc op n1 n2)
; Type: [[Number*Number*Number->Number]*Number*Number*Number->Number]
; Purpose: a calculator to support arithmetic operations based on a given code
; Pre-conditions: op,n1,n2 are integers, 0<=op and op<=5, if op=3 then n2!=0
; Tests: (advanced-calc simple-calc 0 1 2)=>3
(define advanced-calc
  (lambda(calc op n1 n2)
    '...))

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
    '...))

; Signature:fun-sub(f g)
; Type:[[Number->Number]*[Number->Number]->[Number->Number]]
; Purpose: given two functions f,g construct a function that given a number x returns 
; the difference of f applied to x and g applied to x 
; Pre-conditions: true
; Post-condition: result = closure r, such that r(x) = f(x)-g(x)
; Tests:((fun-sub (lambda(x)x) (lambda(x)1)) 0)=>-1
(define fun-sub
  (lambda(f g)
    '...))