#lang racket
; Type: [[T1 -> T2] * [T2 -> T3] -> [T1 -> T3]]
; Purpose: Compute the composition of 2 functions
; Signature: compose(f1, f2)
; Test: ((compose add1 add1) 1) -> 3
(define compose
(lambda (f1 f2)
(lambda (x) (f1 (f2 x)))))
; Type: [Number * [T -> T] -> [T -> T]]
; Purpose: Compose a function f n times.
; Signature: c1-n(n, f)
; Precondition: n > 0
; Test: ((c1-n 10 add1) 1) -> 11
(define c1-n
(lambda (n f)
(lambda (x)
(if (= n 1)
 (f x)
((c1-n (- n 1) f) (f x))))))

; Type: [Number * [T -> T] ->
; Purpose: Compose a function
; Signature: c2-n(n, f)
; Precondition: n > 0
; Test: ((c2-n 10 add1) 1) ->
; Type: [Number * [T -> T] -> [T -> T]]
; Purpose: Compose a function f n times.
; Signature: c2-n(n, f)
; Precondition: n > 0
; Test: ((c2-n 10 add1) 1) -> 11
(define c2-n
(lambda (n f)
(if (= n 1)
f
(compose f (c2-n (- n
1) f)))))

; Type: [Number -> [[T -> T] -> [T -> T]]]
; Purpose: Curried version of compose-n
; Pre-condition: n > 0
; Signature: comp1-n(n)(f)
; Test: (((comp1-n 10) add1) 1) -> 11
(define comp1-n
(lambda (n)
  (lambda (f)
    (cond 
          ((= n 1) f)
          ((even? n) ((comp1-n (/ n 2)) (compose f f)))
          (else (compose f ((comp1-n (- n 1)) f))))
  )
))

; Type: [Number -> [[T -> T] -> [T -> T]]]
; Purpose: Curried version of compose-n
; Pre-condition: n > 0
; Signature: comp2-n(n)(f)
; Test: (((comp2-n 10) add1) 1) -> 11
(define comp2-n
(lambda (n)
(cond 
  ((= n 1) (lambda (f) f))
  ((even? n) (let ((cn/2 (comp2-n (/ n 2)))) (lambda (f) (cn/2 (compose f f)))))
  (else      (let ((cn-1 (comp2-n (- n 1)))) (lambda (f) (compose f (cn-1 f)))))
  )
))












