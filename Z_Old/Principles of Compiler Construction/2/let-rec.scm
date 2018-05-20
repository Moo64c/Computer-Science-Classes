;@file let-rec.scm
; Hold letrec processing

(define _letrec?
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
        (params-list
          (lambda (lst)
            (if (null? lst)
              '()
              (cons (caar lst) (params-list (cdr lst)))
            )
          )
        )
      )
      (and
        (list? expr)
        (< 2 (length expr))
        (equal? (car expr) 'letrec)
        (list? (cadr expr))
        ; Check parameter validity.
        (params? (cadr expr))
        (check-no-double-vars (params-list (cadr expr)))
      )
    )
  )
)

(define parse-letrec
    (letrec (
        (params-list
          (lambda (lst)
            (if (null? lst)
              '()
              (cons (caar lst) (params-list (cdr lst)))
            )
          )
        )
        (param-values
          (lambda (lst)
            (if (null? lst)
              '()
              (cons (cadar lst) (param-values (cdr lst)))
            )
          )
        )
    		(let-body-wrapper
          (lambda (expr)
  		      `(,(let-body expr) )
          )
        )
        (let-body
          (lambda (expr)
            `(lambda ,(list) ,@(cddr expr))
          )
        )
      )
      (lambda (expr)
        (parse
          `(let
            ; Outside wrapper.
            ,(map (lambda (v)
              (list v #f)
              )
              (params-list (cadr expr)))
            ; Internal part
            ,@(map (lambda (var val)
            ; Inside lambda with defined variables.
            ; Add (set [var] [val]) calls for applying data cleanly.
              `(set! ,var ,val)
              )
               (params-list (cadr expr))
               (param-values (cadr expr)))
            ,(let-body-wrapper expr)
          )
        )
    )
  )
)
