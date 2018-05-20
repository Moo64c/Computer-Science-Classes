#lang racket

(require "analyzer-core.rkt" "../asp.rkt")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Primitive procedures tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (lambda-variadic-tests)
  (test (derive-analyze-eval '((lambda l l) 1 2 3)) => '(1 2 3))
  (test (derive-analyze-eval '((lambda l (car l)) 1 2 3)) => '1)
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoking tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(run-tests
 (lambda-variadic-tests)
 )