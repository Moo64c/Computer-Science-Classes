;Procedures to create the abstract syntax 
; For each Abstract-syntax type define:
; - A membership predicate named <type>?
; - A constructor named make-<type>
; - Accessors for each part named <type>-><part>



(define poly? (lambda (exp) (and (list? exp) (eq? (tag exp) 'poly))))
(define make_poly (lambda (degree coefs) (list 'poly degree coefs)))
(define poly->degree (lambda (poly) (cadr poly)))
(define poly->coefs (lambda (poly) (caddr poly)))


(define plus? (lambda (exp) (and (list? exp) (eq? (tag exp) 'plus))))
(define make_plus (lambda (p1 p2) (list 'plus p1 p2)))
(define plus->p1 cadr)
(define plus->p2 caddr)



;Signature: times?(t)
;Type: [T->Boolean]
;Purpose: determine whether an exp. is of type times.
;Pre-conditions: 
;Tests: (times? (make_times (make_poly 0 (0)) (make_poly 0 (0))) => #t
;       (times? 5) => #f
(define times? (lambda (exp) (and (list? exp) (eq? (tag exp) 'times))))

;Signature: make_times(p1 p2)
;Type: [Poly-exp*Poly-exp->times]
;Purpose: create an times statement
;Pre-conditions: p1, p2 are Poly-exp.
;Tests: (make_times (make_poly 0 (0)) (make_poly 0 (1)) => '(times (poly 0 (0)) (poly 0 (1))
(define make_times (lambda (p1 p2) (list 'times p1 p2)))

;Signature: times->p1(t)
;Type: [times->Poly-exp]
;Purpose: retrieves the first Poly-exp in the times expression.
;Pre-conditions: t is a times.
;Tests: (times->p1 (make_times (make_poly 0 (0)) (make_poly 0 (1))) => (poly 0 (0))
(define times->p1 cadr)

;Signature: times->p2(t)
;Type: [times->Poly-exp]
;Purpose: retrieves the second Poly-exp in the times expression.
;Pre-conditions: t is a times.
;Tests: (times->p1 (make_times (make_poly 0 (0)) (make_poly 0 (1))) => (poly 0 (1))
(define times->p2 caddr)



;Signature: define?(t)
;Type: [T->Boolean]
;Purpose: determine whether an exp. is of type define.
;Pre-conditions: 
;Tests: (define? (make_define 'p1 (make_poly 0 (0))) => #t
;       (define? 5) => #f
(define define? (lambda (exp) (and (list? exp) (eq? (tag exp) 'def))))

;Signature: make_define(var val)
;Type: [Symbol*Poly-exp->define]
;Purpose: create an define statement
;Pre-conditions: var is a symbol, val is a Poly-exp.
;Tests: (make_derive 'p1 (make_poly 0 (0)) ) => '(define 'p1 '(p 0 (0)) )
(define make_define (lambda (var val) (list 'define var val)))

;Signature: define->var(d)
;Type: [define->Symbol]
;Purpose: retrieves the Symbol in the define expression
;Pre-conditions: d is a define.
;Tests: (define->var (make_derive 'p1 (make_poly 0 (0)) )) => 'p1
(define define->var cadr)

;Signature: define->val(d)
;Type: [define->Poly-exp]
;Purpose: retrieves the Poly-exp in the define expression
;Pre-conditions: d is a define.
;Tests: (define->val (make_derive 'p1 (make_poly 0 (0)) )) => '(p 0 (0))
(define define->val caddr)








;Signature: apply?(t)
;Type: [T->Boolean]
;Purpose: determine whether an exp. is of type apply.
;Pre-conditions: 
;Tests: (apply? (make_apply '(p 0 (0)) 2) 5)) => #t
;       (apply 5) => #f
(define apply? (lambda (exp) (and (list? exp) (eq? (tag exp) 'apply))))


;Signature: make_apply(p n)
;Type: [poly*Number->apply]
;Purpose: create an apply statement
;Pre-conditions: p is a Poly-exp, n is a Number.
;Tests: (make_apply (make_poly  0 (0)) 5) => '(apply '(p 0 (0)) 2) 5)
(define make_apply (lambda (p n) (list 'apply p n)))


;Signature: apply->p(a)
;Type: [apply->Poly-exp]
;Purpose: retrieves the Poly-exp in the applying expression
;Pre-conditions: a is of type apply.
;Tests: (apply-> (make_apply '(p 0 (0)) 2) 5)) => '(p 0 (0)) 2)
(define apply->p cadr)


;Signature: apply->n(a)
;Type: [apply->Number]
;Purpose: retrieves the number in the applying expression
;Pre-conditions: a is of type apply.
;Tests: (apply-> (make_apply '(p 0 (0)) 2) 5) => 5
(define apply->n caddr)


; The abstract-syntax type is the union of all types defined in the syntax of the language.
(define abstract-syntax? 
  (lambda (exp)
    (or (poly? exp)
        (plus? exp)
        (times? exp)
        (define? exp)
        (apply? exp)
        (number? exp)
        (symbol? exp))))

;****************** Parser code and additional procedures ******************
(define id (lambda (x) x))
(define make_number id)
(define make_symbol id)

;; Read-eval-print loop
(define prompt "> ")
