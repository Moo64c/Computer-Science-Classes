#lang racket

(require "asp.rkt")
;(require "../utils.rkt")
;(provide (all-defined-out) (all-from-out "utils.rkt"))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 'derive' tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (derive-tests)
  (test (derive 3) => 3)
  (test (derive '(let ((x 1)) (+ x 1))) => 
        '( (lambda (x)(+ x 1)) 1))
  (test (derive '(letrec ((f (lambda(n)(if (= n 0)
                                          1
                                         (* n (f (- n 1)))))
                            ))
                     (f 4)))
        =>
        '((lambda (f)
            (set-box! f (lambda (n) (if (= n 0) 1 (* n ((unbox f) (- n 1))))))
            ((lambda (f) (f 4)) (unbox f)))
          (box 'unassigned))) 
  (test (derive '(letrec ((f1 (lambda (x) (+ x 1)))
                          (f2 (lambda (x y) (if (> x 0) 
                                                 (f2 (- x 1) (+ y 1))
                                                 (f1 y)))))
                    (f2 3 6)))
        => 
        '((lambda (f1 f2)
    (set-box! f1 (lambda (x) (+ x 1)))
    (set-box! f2 (lambda (x y) (if (> x 0) ((unbox f2) (- x 1) (+ y 1)) ((unbox f1) y))))
    ((lambda (f1 f2) (f2 3 6)) (unbox f1) (unbox f2)))
  (box 'unassigned)
  (box 'unassigned))
   )
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invoking tests
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(run-tests
 (derive-tests)
 )
