#lang racket
(provide (all-defined-out))
(define tag car)

(include "task5-aux.rkt")

; Manipulate environment (to support define)
; An environment is a function that maps a symbol to a computed value.

; Signature: empty_env(var)
; Type: [Symbol -> Value]
; Purpose: the empty environment in which no symbol has a value.
; Pre-conditions: true
; Tests: (empty_env 'a) -> 'none
(define empty_env (lambda (var) 'none))

; Signature: extend_env(var,val,env)
; Type: [Symbol * Value * Env -> Env]
; Purpose: extend an environment with a new binding (var -> val)
; Pre-conditions: true 
; Tests: ((extend_env 'a 2 empty_env) 'a) -> 2
(define extend_env 
  (lambda (var val env)
    (lambda (v)
      (if (eq? v var)
          val
          (env v)))))


;Interperter procedures


;Signature: derive?(t)
;Type: [T->Boolean]
;Purpose: determine whether an exp. is of type derive.
;Pre-conditions: 
;Tests: (derive? (make_derive '(p 0 (0)) 2) )) => #t
;       (derive? 5) => #f
(define derive? (lambda (exp) (and (list? exp) (eq? (tag exp) 'der))))

;Signature: derive->poly(d)
;Type: [derive->Poly-exp]
;Purpose: retrieves the Poly-exp in the deriving expression
;Pre-conditions: d is a derive.
;Tests: (derive->poly (make_derive '(p 0 (0)) 5)) => '(p 0 (0)) 5)
(define derive->poly (lambda (x) (car (cdr x))))


;Signature: make_derive(p)
;Type: [poly->derive]
;Purpose: create an derive statement
;Pre-conditions: p is a Poly-exp.
;Tests: (make_derive '(p 0 (0)) 2)) => '(der '(p 0 (0)) 2))
(define make_derive (lambda (poly) (list 'der poly)))





; These are the primitive procedures of our polynomial computer
;Signature: poly_plus(p1 p2)
;Type:[<poly>*<poly>-><poly>]
;Purpose: add one poly to another.
;Pre-conditions:p1 and p2 are polynomials.
;Tests: (+ (p + (* 1 (** x 2)) 2) (p + (* 3 (** x 2)) (* 1 (** x 1)) 2)) -> '(poly 2 (4 1 4))
(define poly_plus (lambda (p1 p2)
                    (letrec (
         (zero_poly (make_poly 0 '(0)))
         
         (combine (lambda (coefs1 coefs2 ans) (cond ((and (> (length coefs1) 0) (> (length coefs2) 0)) 
                                                         (combine (cdr coefs1) (cdr coefs2) (cons (+ (car coefs1) (car coefs2)) ans)))
                                                    ((> (length coefs1) 0)
                                                         (combine (cdr coefs1) coefs2 (cons (car coefs1) ans)))
                                                    ((> (length coefs2) 0)
                                                         (combine coefs1 (cdr coefs2) (cons (car coefs2) ans)))
                                                    (else (reverse ans))
                                                     )))
         (trim_zeros (lambda (coefs) (if (or (< (length coefs) 2) (not (= (car coefs) 0))) 
                                         coefs
                                         (trim_zeros (cdr coefs))) ))
         (result (trim_zeros (reverse (combine (reverse (poly->coefs p1)) (reverse (poly->coefs p2)) '()
                                               ))))
         )
         (if (= (length result) 0)
         zero_poly
         (make_poly (- (length result) 1) result))
         
         
         )))


;Signature: poly_apply(p n)
;Type: [<poly>*Number->Number]
;Purpose: calculates p(n)
;Pre-conditions: p is of type poly, n is a number.
;Tests: (poly_apply (make_poly 3 '(1 -6 11 -6)) 3)-> 0
(define poly_apply (lambda (p n) 
                     (letrec ((horner (lambda (coefs n result) (if (null? coefs) 
                                                                  result
                                                                  (horner (cdr coefs) n (+ (* result n) (car coefs)) ) ))
                       ))
                     (horner (poly->coefs p) n 0))
                     ))

;Signature: poly_times(p1 p2)
;Type: [<poly> * <poly> -> <poly>] 
;Purpose: Compute the multiplication of two polynomials.  
;Pre-conditions:  p1 and p2 are polynomials.
;Tests: (* (p + (* 1 (** x 2)) 2) (p + (* 3 (** x 2)) (* 1 (** x 1)) 2)) => '(poly 4 (3 1 8 2 4))
(define poly_times (lambda (p1 p2)
                     (letrec ((helper (lambda (coef expt coeffs ans)
                                        (if (null? coeffs)
                                            (let ((new_coeffs (add_zeroes expt ans)))
                                              (make_poly (- (length new_coeffs) 1) new_coeffs))
                                            (helper coef expt (cdr coeffs) (cons (* coef (car coeffs)) ans)))))
                              (add_zeroes (lambda (expt coeffs)
                                            (if (zero? expt)
                                                (reverse coeffs)
                                                (add_zeroes (- expt 1) (cons 0 coeffs)))))
                              (p1_coeffs (poly->coefs p1))
                              (p2_coeffs (poly->coefs p2))
                              (helper1 (lambda (c1 c2 c1-deg ans)
                                         (if (null? c1)
                                             ans
                                             (let ((poly_ans (helper (car c1) (- (length c1) 1) c2 (list)))
                                                   (rest_c1 (cdr c1)))
                                               (helper1 rest_c1 c2 (- (length rest_c1) 1) (poly_plus poly_ans ans)))))))
                       (helper1 p1_coeffs p2_coeffs (poly->degree p1) (make_poly 0 (list 0))))))


;Signature: poly_derive(p)
;Type: [<poly> -> <poly>] 
;Purpose: Compute the deriviation of a polynomials.  
;Pre-conditions:  p1 is a polynomial.
;Tests: (poly_derive '(p 4 (1 0 3 2 1) )) => '(poly 3 (4 0 6 2))
(define poly_derive (lambda (poly)
                     (letrec (
                              (zero_poly (make_poly 0 '(0)))
                              (derive-helper (lambda (ans coefs degree)
                                             (if (= degree 0)
                                                 (make_poly (- (length ans) 1) (reverse ans))
                                                 (derive-helper (cons (* degree (car coefs)) ans) (cdr coefs) (- degree 1))
                                             )
                              )))
                       (if (= (poly->degree poly) 0)
                           zero_poly
                           (derive-helper (list) (poly->coefs poly) (poly->degree poly)))
                       )))




(define print-poly display)

;Signature: Ipoly(as env)
;Type: [<abstract-syntax> * <environment> -> <abstract-syntax>] 
;Purpose: Interpreter for polynomial expressions  
;Pre-conditions: 
;Tests: (Ipoly (make-plus (make_poly 2 '(1 2 3)) (make_poly 1 '(1 2))) empty_env) => '(poly 2 (1 3 5))
(define Ipoly
  (lambda (as env)
    (cond 
      ((poly? as) as)
      ((number? as) as)
      ((symbol? as) (env as))
      ((define? as) (extend_env (define->var as) (Ipoly (define->val as) env) env))
      ((plus? as) (poly_plus (Ipoly (plus->p1 as) env)
                             (Ipoly (plus->p2 as) env)))
      ((times? as) (poly_times (Ipoly (times->p1 as) env)
                               (Ipoly (times->p2 as) env)))
      ((apply? as) (poly_apply (Ipoly (apply->p as) env)
                               (Ipoly (apply->n as) env)))
      ((derive? as) (poly_derive (Ipoly (derive->poly as) env)))
      (else "Ipoly did not recognize the expression"))))

;****************** Parser code ******************

;Signature: parse-poly(exp)
;Type: [<sexpre> -> <poly>] 
;Purpose: parse a scheme expression and returns a <poly> expression.  
;Pre-conditions: 
;Tests: (parse-poly '( + (* 2 (** x 2)) 1)) => '(poly 2 (2 0 1))
;       (parse-poly '(5 x 4 + 3 x 2 + 2)) => '(poly 4 (5 0 3 0 2))
(define parse-poly
  (lambda (exp)
    (letrec ((scheme-form (lambda (exp coeff expts)
                            (cond ((null? exp) "malformed expression")
                                  ((and (number? (car exp)) (null? (cdr exp)))
                                   (let* ((coeff_full (make_coeffs_full coeff expts (list (car exp)) 0)))
                                     (make_poly (-  (length coeff_full) 1) coeff_full)))
                                  ((check-sub-expr (car exp))
                                   (let ((coef (cadar exp))
                                         (deg (caddr (caddar exp))))
                                     (scheme-form (cdr exp) (cons coef coeff) (cons deg expts))))
                                  (else (error "expression is malformed")))))
             
             (human-form (lambda (exp)
                           (if (= (length exp) 1)
                               (if (number? (car exp))
                                   (make_poly 0 (car exp))
                                   (error "not a poly"))
                               (get-human-poly-helper exp (list) (caddr exp) (caddr exp))
                               )
                             ))
             (get-human-poly-helper (lambda (exp ans degree cur) 
                                          (cond ((and (= cur 0) (= (length exp) 1) (number? (car exp))) 
                                                     (make_poly degree (reverse (cons (car exp) ans))))
                                                ((and (> cur 0) (= (length exp) 1) (number? (car exp)))
                                                      (get-human-poly-helper exp (cons 0 ans) degree (- cur 1)))
                                                ((and (> (length exp) 1) (number? (car exp)) (number? (caddr exp)) (> cur (caddr exp)) )
                                                      (get-human-poly-helper exp (cons 0 ans) degree (- cur 1)))
                                                ((and (> (length exp) 1) (number? (car exp)) (number? (caddr exp)) (= cur (caddr exp)) )
                                                      (get-human-poly-helper (cdr (cdddr exp)) (cons (car exp) ans) degree (- cur 1)))
                                                (else (error "malformed human-poly expression"))
                                          )))
             (check-sub-expr (lambda (expr)(let ((sub-exp (caddr expr)))
                                             (and 
                                              (eq? (car expr) '*)
                                              (number? (cadr expr))
                                              (eq? (car sub-exp) '**)
                                              (eq? (cadr sub-exp) 'x)
                                              (number? (caddr sub-exp))))))
             (add_zeroes (lambda (n res)
                           (cond ((< n 0) (error "wrong order of exponents"))
                                 ((zero? n) res)
                                 (else (add_zeroes (- n 1) (cons 0 res))))))
             (make_coeffs_full (lambda (coeffs expts res last)
                                 (if (null? coeffs)
                                     res
                                     (make_coeffs_full (cdr coeffs) (cdr expts) (cons (car coeffs) (add_zeroes (- (- (car expts) last) 1) res)) (car expts))))))
      (if (eq? '+ (car exp))
      (scheme-form (cdr exp) (list) (list))
      (human-form exp)
      ))))

;Signature: poly_exp?(exp)
;Type: [<Abstract-Syntax> -> boolean]
;Purpose: return true if expression is a poly expression.
;Pre-conditions:
;Tests:  (and (poly_exp? 4) (poly_exp? '(poly 2 (2 0 1)))) ) => #t
(define poly_exp? (lambda(exp)  (or (poly? exp) (times? exp) (plus? exp) (symbol? exp))))

;Signature: number_exp?(exp)
;Type: [<Abstract-Syntax> -> boolean]
;Purpose: return true if expression is a number expression.  
;Pre-conditions: 
;Tests: (number_exp? 4) => #t
(define number_exp? (lambda(exp) (or (number? exp) (apply? exp))))

;Signature: parse(expr)
;Type: [<sexpr> -> <Abstract-Syntax>]
;Purpose: Parse scheme expressions and create expressions from the abstract-syntax.  
;Pre-conditions: true
;Tests: (parse '(+ P1 (p 2 x 2 + 1)) -> (plus P1 (poly 2 (2 0 1)))
(define parse 
  (lambda (expr) 
    (cond 
      ((number? expr) (make_number expr))
      ((symbol? expr) (make_symbol expr))
      ((list? expr)
       (cond 
         ((eq? (car expr) 'def)  (make_define  (parse (cadr expr)) (parse (caddr expr))) )
         ((eq? (car expr) '+)  (make_plus  (parse (cadr expr)) (parse (caddr expr))) )
         ((eq? (car expr) '*)  (make_times  (parse (cadr expr)) (parse (caddr expr))) )
         ((eq? (car expr) 'p)  (parse-poly (cdr expr) ) )
         ((eq? (car expr) 'der) (make_derive (parse (cadr expr)) ) )
         (else (let ((poly (parse (car expr)))
                     (number (parse (cadr expr))))
                 (if (and (poly_exp? poly) (number_exp? number))
                     (make_apply poly number)
                     "Can not parse expression"))))))))

; Signature: repl()
; Type: Void->Void
; Purpose: Read-Eval-Print-Loop
; Pre-conditions: true
; Tests: (repl)
(define repl (lambda ()
               (letrec ((repl-iter (lambda (env)
                                     (display prompt)
                                     (let ((expr (read)))
                                       (let ((as (parse expr)))
                                         (let ((val (Ipoly as env)))
                                           (cond 
                                             ((number? val) (print val) (newline) (repl-iter env))
                                             ((poly? val) (print-poly val)  (newline) (repl-iter env))
                                             ((procedure? val)  (newline) (repl-iter val))
                                             (else (print (string-append "Error: " val)) (newline) (repl-iter env)))))))))
                 (repl-iter empty_env))))
