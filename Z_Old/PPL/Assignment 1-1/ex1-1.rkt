;#lang racket
;--------------;
;  question 5  ;
;--------------;
; Signature: reverse-digits(n)
; Type: [Number -> Number]
; Purpose: Return the number's digits in reverse.
; Pre-conditions: n >= 0 and is an integer.
; Tests: (test (reverse-digits 123) => 321)
(define reverse-digits 
  (lambda (n) (if (>= n 10) 
                  (+ (reverse-digits (floor (/ n 10))) (* (expt 10 (floor (logB n 10))) (modulo n 10)))
                  n)
  )
)

; Signature: logB(x, B)
; Type: [Number * Number-> Number]
; Purpose: Return the log of x on basis B.
; Pre-conditions: x > 0, B > 0.
; Tests: (test (reverse-digits 100 10) => 2.0)
(define logB 
    (lambda (x B) 
      (/ (log x) (log B))))

;--------------;
;  question 6  ;
;--------------;
; Signature: approx-golden-ratio(n)
; Type: [Number -> Number]
; Purpose: Calculate golden ratio number to n-th accuracy.
; Pre-conditions: n >= 0
; Tests: (test (approx-golden-ratio 10) => 1.6180285974702324)
(define approx-golden-ratio 
  (lambda (n) (if (<= n 0)
                   1
                   (sqrt (+ 1 (approx-golden-ratio (- n 1))))
               )
   )
 )
