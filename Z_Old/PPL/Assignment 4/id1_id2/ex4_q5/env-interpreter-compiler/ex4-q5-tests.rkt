#lang racket

(require "interpreter-core.rkt" "../asp.rkt")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Primitive procedures tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (exists?-tests)
  (test (derive-eval '(exists? x)) => #f)
  (test (derive-eval '(define x 4)) => 'ok)
  (test (derive-eval '(exists? x)) => #t)
  (test (derive-eval '(exists? y)) => #f)
  (test (derive-eval '(let ((y 5)) (exists? z))) => #f)
  (test (derive-eval '(let ((y 5)) (exists? y))) => #t)
  (test (derive-eval '(exists? y)) => #f)
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoking tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(run-tests
 (exists?-tests)
 )
