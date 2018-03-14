;@file lambda-opt.scm
; Hold optional-params lambda form processing

;Dependencies:
;- utility.scm

; Of the form (lambda ([var]*) [expr]+)
(define _lambda-opt?
    (letrec (
        (improper-list-of-vars?
            (lambda (lst)
                (cond
                    ((null? (cdr lst))
                            #f)
                    ((and
                        (_var? (car lst))
                        (_var? (cdr lst))
                        )
                            #t)
                    ((and
                        (_var? (car lst))
                        (_var? (cadr lst))
                        )
                            (improper-list-of-vars? (cdr lst)))
                    (else
                            #f))
            )
        )
    )
    (lambda (expr)
        (and
            ; Check we have a list.
            (list? expr)
            ; Should have "lambda" ([var]* . [var] [expr]+ (so at least 3 elements)
            (< 2 (length expr))
            (equal? (car expr) 'lambda)
            (pair? (cadr expr))
            ; Check this is an improper list of variables
            (improper-list-of-vars? (cadr expr))
        )
    ))
)

(define parse-lambda-opt
    (letrec (
        ; Last item (free argument) should be a legit var.
        (all-except-last
            (lambda (lst)
                (if (_var? (cdr lst))
                    ; Next item is the .[free item].
                    (list (car lst))
                    (cons (car lst) (all-except-last (cdr lst))))))
        (last-in-list
            (lambda (lst)
                (if (_var? (cdr lst))
                    (cdr lst)
                    (last-in-list (cdr lst))))))

    (lambda (expr)
        ; Change to "lambda-opt" ([var]*) [opt] (parse [exp])
        `(lambda-opt
            ; List of normal parameters.
            ,(all-except-last (cadr expr))
            ; Special form ".[free item]".
            ,(last-in-list (cadr expr))
            ,(parse (decompose-list (cddr expr)))
        )
    ))
)
