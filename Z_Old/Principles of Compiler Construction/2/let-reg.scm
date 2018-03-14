;@file let-reg.scm
; Hold let processing

(define _let?
  (lambda (expr)
    (letrec (
        (params?
          (lambda (lst)
            (cond
              ((null? lst)
                #t)
              ((and
                (pair? (car lst))
                (_var? (caar lst)))
                (params? (cdr lst))
              )
              (else
                #f)
            )
          )
        )
        ; Pull variable names out of list.
        (proc-vars
          (lambda (lst)
            (if (null? lst)
              '()
              (cons (caar lst) (proc-vars (cdr lst)))
            )
          )
        )
      )
      (and
        (list? expr)
        (< 2 (length expr))
        (equal? (car expr) 'let)
        (list? (cadr expr))
        ; Check parameter validity.
        (params? (cadr expr))
        (check-no-double-vars (proc-vars (cadr expr)))
      )
    )
  )
)

(define parse-let
  (lambda (expr)
    (letrec (
      (params-list
        (lambda (lst)
          (cond
            ((null? lst)
              '())
            (else
              (cons (caar lst) (params-list (cdr lst)))
          )
        )
      ))
      (params-value
        (lambda (lst)
          (cond
            ((null? lst)
              '())
            (else
              (cons (cadar lst) (params-value (cdr lst)))
            )
          )
        )
      )
    )
    ; Use the parse function to macro-expand with lambda and applic.
    (parse `(
      (lambda
      ; List of var names
      ,(params-list (cadr expr))
      ; Expressions in the let body
      ,@(cddr expr))
      ; use applic to call the lambda
      ,@(params-value (cadr expr))))
    )
  )
)
