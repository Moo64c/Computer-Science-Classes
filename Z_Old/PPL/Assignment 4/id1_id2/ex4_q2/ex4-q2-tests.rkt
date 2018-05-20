#lang racket

(require "asp.rkt")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 'derive' tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (derive-tests)
  (test (or->if  '(or)) => '#f)
  (test (or->if  '(or 4)) => '(if 4 4 (or)))
  (test (derive '(or 4 5)) => '(if 4 4 (if 5 5 #f)))
)
  
                         
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoking tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(run-tests
 (derive-tests)
 )
