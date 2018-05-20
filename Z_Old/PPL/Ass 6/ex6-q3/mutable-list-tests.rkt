#lang racket
;
; Basic unit tests for the mutable list ADT. You may want to add more tests,
; especially for edge cases.

(require "utils.rkt")
(require "mutable-list.rkt")

(define add-items-to-mlist
  (lambda (m item-count)
    (letrec (( add-item (lambda (m current-item)
      (if (= current-item 0)
          m
          (begin (mlist-add! m (- item-count current-item) )
                 (add-item m (- current-item 1)))))))
    (add-item m item-count))))

(define mlist-with-deleted-index
  (lambda (amount deleted)
    (define m (add-items-to-mlist (make-mlist) amount))
    (mlist-delete-index! m deleted)
    m))

(define mlist-with-replaced
  (lambda (amount pred item)
    (define m (add-items-to-mlist (make-mlist) amount))
    (mlist-replace-where! m pred item)
    m))

(define iterator-over-mlist
  (lambda (mlist-size next-call-count)
    (define m (add-items-to-mlist (make-mlist) mlist-size))
    (define itr (make-iterator m))
    (letrec ((adv (lambda (count)
                            (if (= 0 count)
                              itr
                              (begin
                                (iterator-next! itr)
                                (adv (- count 1)))
                              ))))
      (adv next-call-count))))

(define (test-init-mlist)
  (test (mlist-size(make-mlist)) => 0)
  (test (mlist-size (add-items-to-mlist (make-mlist) 1) ) => 1))

(define (test-get-mlist)
  (test (mlist-get-index (add-items-to-mlist (make-mlist) 4) 1) => 1)
  (test (mlist-get-index (add-items-to-mlist (make-mlist) 4) 2) => 2)
)

(define (test-del-mlist)
  (test (mlist-get-index (mlist-with-deleted-index 10 5) 5) => 6 )
  (test (mlist-get-index (mlist-with-deleted-index 10 5) 4) => 4 ))

(define (test-replace-mlist)
  (test (mlist-get-index (mlist-with-replaced 10 even? 'even) 3) => 3 )
  (test (mlist-get-index (mlist-with-replaced 10 even? 'even) 6) => 'even ))

(define (test-iterators)
  (test (iterator-next! (iterator-over-mlist 10 2)) => 2)
  (test (iterator-has-next? (iterator-over-mlist 2 2)) => false)
  (test (iterator-has-next? (iterator-over-mlist 10 2)) => true))

(run-tests
 (test-init-mlist)
 (test-del-mlist)
 (test-get-mlist)
 (test-replace-mlist)
 (test-iterators)
)
