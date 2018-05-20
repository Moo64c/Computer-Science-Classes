#lang racket
(provide (all-defined-out))

;Signature: derive(f)
;Type: [[Number->Number]->[Number->Number]]
;Purpose: Get the derived function from a given function.
;Pre-conditions: f is derivable.
;Tests: ((derive (lambda(x)(* x x))) 3) ~ 6
(define derive
(lambda(f)
(let ([h 0.001])
  (lambda(x) (/(- (f (+ x h)) (f x)) h)))))

;Signature: good-enough?(guess f)
;Type: [Number * [Number->Number] -> Boolean] 
;Purpose: Compute if the guess is close enough to a real root. 
;Pre-conditions: f is a function.
;Tests: (good-enough? 2 (lambda(x)(- (* x x) 4))) => #t
(define good-enough? (lambda (guess f)
                       (< (abs (- (f guess) 0.0)) 0.001)))

;Signature: root(f)
;Type: [Number*Number] -> Number
;Purpose: computes a root of a function using Newton’s method.
;Precondition: x >= 0
;Tests: (root (lambda(x) (* (- x 1) (- x 1) (- x 1)))) => 1.0832050589321334
(define root
(lambda (f)
(letrec 
((iter (lambda (guess)
    (if (good-enough? guess f)
        guess 
        (iter (improve f guess)))))
(improve (lambda (f guess) 
    (begin (display guess) (newline) (- guess (/ (f guess) ((derive f) guess)))))))
(iter 25.0)
  )
  ))