#lang racket
(provide (all-defined-out))

; Signature: make-mlist()
; Type: () -> MList
; Purpose:   Creates an empty mutable list.
(define make-mlist (lambda () (list (box (list)) 'mlist))) 

; Signature: mlist?( value )
; Type: T -> Boolean
; Purpose:   Validates that the passed value is indeed an mlist.
; Example:   (mlist? (make-mlist)) --> #t
; Example:   (mlist? 42) --> #f
(define mlist? (lambda (value) (and (list? value) (>= (length value) 2) (eq? (cadr value) 'mlist))))

; Signature: mlist-void?( mlist )
; Type: MList -> Boolean
; Purpose: Tests the passed Mutated List for emptyness.
; Example: (mlist-void? (make-mlist)) --> #t
; Example: (define m (make-mlist)) (mlist-add m 1) (mlist-void? m) --> #f

(define mlist-void? (lambda (mlist) (null? (unbox (car mlist)))))


; Signature: get-list-box( mlist )
; Type: MList -> Box(list)
; Purpose: gets the box for the list.
(define get-list-box (lambda (mlist) (car mlist)))


; Signature: mlist-add!(list, item)
; Type: MList*T -> T
; Purpose: Adds the item to the end of mlist and returns the item.
; Example (mlist-add mlist 8) -> 8
(define mlist-add! (lambda (mlist item) 
       (begin 
       	(set-box! (get-list-box mlist) (append (unbox (get-list-box mlist)) (list (box item)) ))
       	item )
))

; Signature: mlist-size(mlist)
; Type: MList->Integer
; Purpose: Return the number of items in mlist.
; Example: (mlist-size (let (lst (make-mlist)) (begin (mlist-add! lst 'a) lst))) --> 1
(define mlist-size (lambda (mlist)
   	(length (unbox (get-list-box mlist))) 
    ))

; Signature: mlist-get-index( mlist, index )
; Type: MList*Integer->T|Void
; Purpose: Return the item at the passed index (0-based), or (void), if no such item exists.
(define mlist-get-index (lambda (mlist index) 
	(letrec (
		(get-idx (lambda (lst index)
				(if (= index 0) (unbox (car lst))
				    (get-idx (cdr lst) (- index 1)))))
  	)
  (if (> (mlist-size mlist) index)  
    (get-idx (unbox (get-list-box mlist)) index)
    (void)
  ))))

  
  

; Signature: mlist-set-index!( mlist, index, item )
; Type: MList*Integer*T->T|Void
; Purpose: Set the item at the passed (0-based) index to the new item. Return the item, or void, if no such item exists.
(define mlist-set-index! (lambda (mlist index item) 
	(letrec (
        (return-value (void) )
		(get-idx (lambda (list index item) 
              (if (= index 0) 
           	  (begin 
           		  (set! return-value (unbox (car list))) 
                  (set-box! (car list) item) 
           		  return-value) 
     		    (get-idx (cdr list) (- index 1) item))))
   	)
    (if (> (mlist-size mlist) index)
        (get-idx (unbox (get-list-box mlist)) index item)
        return-value
    ))))

; Signature: mlist-replace-where(mlist, predicate, new-item)
; Type: MList*[T->Boolean]*T->Void
; Purpose: Replace all items in the list on which pred returns true with new-item.
; Example: (define m (make-mlist))
;          (mlist-add m 'secret1)
;          (mlist-add m 3)
;          (mlist-add m 'secret2)
;          (mlist-replace-where m symbol? 'redacted)
;           --> m is now ('redacted, 3, 'redacted) (actual representation may vary)
(define mlist-replace-where! (lambda (mlist predicate new-item) 
	(letrec (
		(replace-items (lambda (lst condition new-item)
    	(if (null? lst)
      	(void)
        (begin 
        	(if (condition (unbox (car lst)))
        		(set-box! (car lst) new-item) (void) ) 
        		(replace-items (cdr lst) condition new-item))))
		))
    (replace-items (unbox (get-list-box mlist)) predicate new-item))))

; Signature: mlist-delete-index!(mlist, index)
; Type: MList*Integer->void
; Purpose: Remove the item at the passed (0-based) index from mlist. If no such item exists, do not throw an error.
(define mlist-delete-index! (lambda (mlist index) 
	(letrec (
		(delete-the-index$ (lambda (lst index cont) 
        (if (= index 0) 
        	(cont (cdr lst))
          (delete-the-index$ (cdr lst) (- index 1) (lambda (ret) (cont (cons (car lst) ret)))     )))
          ))
    (if (> (mlist-size mlist) index)
	  (set-box! (get-list-box mlist) (delete-the-index$ (unbox (get-list-box mlist)) index (lambda (x) x) ))
      (void)
    ))))

;; Iterator ADT
; Signature: make-iterator(mlist)
; Type: MList->Iterator
; Purpose: Create an iterator that will go over the values in mlist.
(define make-iterator (lambda (mlist) 
	(box (list mlist 'iterator) )))
    
    
; Signature: get-inner-mlist(iterator)
; Type: Iterator->MList
; Purpose: get the inner mlist of the iterator.
(define get-inner-mlist (lambda (iter) 
	(car (unbox iter)) ))

    
; Signature: iterator-has-next?(itr)
; Type: Iterator->Boolean
; Purpose: Test if itr has any more values
(define iterator-has-next? (lambda (itr) 
	(not (mlist-void? (get-inner-mlist itr)))))

; Signature: iterator-next!(itr)
; Type: Iterator->T
; Purpose: Advance itr to its next value, and return its current one.
; Example: > (define ml (make-mlist))
;          (mlist-add! ml 0)
;          > 0
;          (mlist-add! ml 1)
;          > 1
;          (mlist-add! ml 2)
;          > 2
;          (mlist-add! ml 3)
;          > 3
;          (define itr (make-iterator ml))
;          > (iterator-has-next? itr)
;          #t
;          > (iterator-next! itr)
;          0
;          > (iterator-next! itr)
;          1
;          > (iterator-next! itr)
;          2
;          > (iterator-next! itr)
;          3
;          >  (iterator-has-next? itr)
;          #f
(define iterator-next! (lambda (itr) 
	(letrec (
		(x 'unassigned)
	)
    (if (iterator-has-next? itr)
        (begin
            (set! x (mlist-get-index (get-inner-mlist itr) 0) ) 
            (mlist-delete-index! (get-inner-mlist itr) 0) 
            x)
        (void))
     )))
