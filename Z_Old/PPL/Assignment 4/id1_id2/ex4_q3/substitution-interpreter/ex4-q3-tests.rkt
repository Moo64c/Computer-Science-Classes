#lang racket

(require "substitution-core.rkt" "../asp.rkt")
;(require "../utils.rkt")
;(provide (all-defined-out) (all-from-out "utils.rkt"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Primitive procedures tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (exists?-tests)
  (test (derive-eval '(exists? x)) => '(value #f))
  (test (derive-eval '(define x 5)) => 'ok)
  (test (derive-eval '(exists? x)) => '(value #t))
 )

(run-tests
 (exists?-tests)
 )
