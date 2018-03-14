#lang racket
(require "tagged.rkt" "utils.rkt")
(provide (all-defined-out))

;; Abstract syntax for Scheme subset
;; BNF
;; <scheme-exp> :: <scheme-atomic> | <scheme-composite>
;; <scheme-atomic> :: <scheme-var> | <number> | <bool>
;; <scheme-composite> :: <scheme-proc> | <scheme-app>
;; <scheme-proc> :: (lambda (<scheme-var>*) <scheme-exp>+)
;; <scheme-app> :: (<scheme-exp>+)

;; Abstract syntax types:
;; Scheme-proc: Components vars:List(Scheme-var)
;;                         body:List(Scheme-exp)
;; Scheme-app:  Components operator:Scheme-exp
;;                         operands:List(Scheme-exp)


;; ============================================================
;; union types (have no constructors and no selectors)

(define scheme-exp?
  (lambda (exp)
    (or (scheme-atomic? exp)
        (scheme-composite? exp))))

(define scheme-atomic?
  (lambda (exp)
    (or (scheme-var? exp)
        (number? exp)
        (boolean? exp))))

(define scheme-composite?
  (lambda (exp)
    (or (scheme-proc? exp)
        (scheme-app? exp)
        (scheme-let? exp))))

;; ============================================================
;; Atomic types
(define scheme-var? symbol?)
(define make-scheme-var (lambda (s) s))

;; ============================================================
;; compound types

;; ------------------------------
;; SCHEME-PROC
;; Type: LIST(Scheme-var) * LIST(Scheme-exp) -> Scheme-proc
;; Example: (make-scheme-proc
;;            (list (make-scheme-var 'a))
;;            (list (make-scheme-app '+ (make-scheme-var 'a) 2)))
;;          --> ast of (lambda (a) (+ a 2))
(define make-scheme-proc
  (lambda (vars body)
    (make-tagged 'lambda (list vars body))))

(define scheme-proc? (tagged-by? 'lambda))

;; Type: Scheme-proc -> LIST(Symbol)
(define scheme-proc->vars
  (lambda (exp)
    (first (tagged->content exp))))

;; Type: Scheme-proc -> LIST(Scheme-exp)
(define scheme-proc->body
  (lambda (exp)
    (second (tagged->content exp))))

;; ------------------------------
;; SCHEME-APP
;; Type: Scheme-exp * LIST(Scheme-exp) -> Scheme-app
;; Example: (make-scheme-app '+ (list (make-scheme-var 'a) 2))
;;          --> (lambda + a 2)
;;
(define make-scheme-app
  (lambda (operator operands)
    (make-tagged 'app (list operator operands))))

(define scheme-app? (tagged-by? 'app))

;; Type: Scheme-app -> Scheme-exp
(define scheme-app->operator
  (lambda (app)
    (first (tagged->content app))))

;; Type: Scheme-app -> LIST(Scheme-exp)
(define scheme-app->operands
  (lambda (app)
    (second (tagged->content app))))

;; ------------------------------
;; SCHEME-LET
;; Type: LIST(Pair(Scheme-var, Scheme-exp)) * LIST(Scheme-exp) -> Scheme-let
;; Example: (make-scheme-let (list (cons (make-scheme-var 'a) 2)) 
;;                           (list (make-scheme-app '+ (list (make-scheme-var 'a) 3))))
;;          --> (let ((a . 2)) ((app + (a 3))))
;;
(define make-scheme-let
  (lambda (bindings body)
    (make-tagged 'let (list bindings body))))

(define scheme-let? (tagged-by? 'let))

;; Type: Scheme-let -> LIST(Pair(Scheme-var, Scheme-exp))
(define scheme-let->bindings
  (lambda (let)
    (first (tagged->content let))))

(define scheme-let->vars
  (lambda (let)
    (map car (scheme-let->bindings let))))

(define scheme-let->vals
  (lambda (let)
    (map cdr (scheme-let->bindings let))))

;; Type: Scheme-let -> LIST(Scheme-exp)
(define scheme-let->body
  (lambda (let)
    (second (tagged->content let))))

;; ------------------------------
;; Purpose: Traverse composite expressions as a tree
;;          This is a polymorphic function.
;; Type: [Scheme-composite -> List(Scheme-exp)]
(define scheme-composite->components
  (lambda (c)
    (cond ((scheme-proc? c) (append (scheme-proc->vars c)
                                    (scheme-proc->body c)))
          ((scheme-app? c) (cons (scheme-app->operator c)
                                 (scheme-app->operands c)))
          ((scheme-let? c) (append (scheme-let->vars c)
                                   (scheme-let->vals c)
                                   (scheme-let->body c)))
          (else (error 'scheme-composite->components
                       "Bad scheme abstract syntax ~s" c)))))


;; ============================================================
;; Scheme-Parser

;; Type: Concrete-syntax -> Scheme-exp  ;the abstract syntax used by the type inference system
(define scheme-parse
  (lambda (e)
    (cond
      ((number? e) e)
      ((boolean? e) e)
      ((scheme-var? e) (make-scheme-var e))
      ((empty? e) (error 'scheme-parse "Unexpected empty"))
      ((pair? e)
       (cond ((eq? (car e) 'lambda)
              (make-scheme-proc (map make-scheme-var (second e))
                                (map scheme-parse (cddr e))))
             ((eq? (car e) 'let)
              (let ((bindings (second e))
                    (body (cddr e)))
                (make-scheme-let (map (lambda (binding) 
                                        (cons (make-scheme-var (car binding))
                                              (scheme-parse (second binding))))
                                      bindings)
                                 (map scheme-parse body))))
             (else (make-scheme-app (scheme-parse (car e))
                              (map scheme-parse (cdr e))))))
      (else (error 'scheme-parse "Unexpected type")))))

;; Type: Scheme-exp -> Concrete-syntax
(define scheme-concrete
  (lambda (e)
    (cond ((scheme-atomic? e) e)
          ((scheme-proc? e)
           (cons 'lambda
                 (cons (map scheme-concrete (scheme-proc->vars e))
                       (map scheme-concrete (scheme-proc->body e)))))
          ((scheme-let? e)
           (cons 'let (cons (map list (map scheme-concrete (scheme-let->vars e))
                                 (map scheme-concrete (scheme-let->vals e)))
                            (map scheme-concrete (scheme-let->body e)))))
          ((scheme-app? e)
           (cons (scheme-concrete (scheme-app->operator e))
                 (map scheme-concrete (scheme-app->operands e))))
          (else (error 'scheme-concrete "Bad scheme abstract syntax ~s" e)))))