;@file let-reg.scm
; Hold let processing

(define _let-star?
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
        (equal? (car expr) 'let*)
        (list? (cadr expr))
        ; Check parameter validity.
        (params? (cadr expr))
      )
    )
  )
)
(define parse-let-star
  (letrec
    (
      (let*->let
        (lambda (args body)
          (if (= 1 (length args))
            `(let
            ,args
            ,@body)
            `(let
            (,(car args))
            ,(let*->let (cdr args) body)
            )
          )
        )
      )
    )
    (lambda (expr)
      (if (= 0 (length (cadr expr)))
       (parse `(let () ,@(cddr expr)))
       (parse (let*->let (cadr expr) (cddr expr)))
      )
    )
  )
)
