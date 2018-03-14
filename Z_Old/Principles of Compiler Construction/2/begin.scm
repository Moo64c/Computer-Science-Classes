(define begin?
  (lambda (sxpr)
    (and
      (list? sxpr)
      (< 0 (length sxpr))
      (equal? 'begin (car sxpr))
    )
  )
)

(define parse-begin
      (letrec
        ((rex
          (lambda (lstExp)
      		  (cond
              ((null? lstExp) (list))
      			  ((begin? (car lstExp)) (append (rex (cdr (car lstExp))) (rex (cdr lstExp))))
      			  (else (cons (parse (car lstExp) ) (rex (cdr lstExp))))
            )
          )
        )
       )
       (lambda (sxpr)
	      (cond
          ((= 1 (length sxpr)) `(const ,@(list (void))))
	        ((= 2 (length sxpr)) (parse (cadr sxpr)))
          (else `(seq ,(rex (cdr sxpr))))
        )
      )
    )
  )
