#lang racket
(require "utils.rkt")

; Load student's solution file
(include "ex1-1.rkt")

(define delta 0.0001)

(define close-enough
  (lambda(res golden)
    (< (abs (- res golden)) delta)))

;Tests for question 5
(define (q5-tests) 
  (run-tests
   (test (reverse-digits 5) => 5)
   (test (reverse-digits 123) => 321)))

;Tests for question 6
(define (q6-tests) 
  (run-tests
   (test (close-enough (approx-golden-ratio 0) 1) => #t)
   (test (close-enough (approx-golden-ratio 1) 1.4142135623730951) => #t)))

;Run the tests
(q5-tests)