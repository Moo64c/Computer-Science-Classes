(define if3?
  (lambda (sxpr)
      (and
        (list? sxpr)
        (or
          (= 4 (length sxpr))
          (= 3 (length sxpr)) )
          (equal? 'if (car sxpr)
        )
      )
    )
  )


(define parse-if3
  (lambda (sxpr)
    (if (= 4 (length sxpr))
      `(if3 ,(parse (cadr sxpr)) ,(parse (caddr sxpr)) ,(parse (cadddr sxpr)))
      `(if3 ,(parse (cadr sxpr)) ,(parse (caddr sxpr)) ,`(const ,@(list (void))))
    )
  )
)

(define or?
  (lambda (sxpr)
    (and
      (list? sxpr)
      (< 0 (length sxpr))
      (equal? 'or (car sxpr))
    )
  )
)

(define parse-or
  (lambda (sxpr)
  (cond
    ((= 1 (length sxpr)) (parse #f))
    ((= 2 (length sxpr)) (parse (cadr sxpr)))
    (else `(or ,(map parse (cdr sxpr)))))
  )
)

(define and?
  (lambda (sxpr)
    (and
      (list? sxpr)
      (< 0 (length sxpr))
      (equal? 'and (car sxpr)))
  )
)

(define parse-and
  (letrec
    ((rex
      (lambda (lst)
        (if (= 1 (length lst))
          (parse (car lst))
          `(if3 ,(parse (car lst)) ,(rex (cdr lst)) ,(parse #f)))
      )
     )
    )
    (lambda (sxpr)
      (if (= 1 (length sxpr))
        (parse #t)
        (rex (cdr sxpr))
      )
    )
  )
)

(define cond?
  (letrec
    ((rex
      (lambda (lst)
        (cond
          (
            (and
              (= 1 (length lst))
              (pair? (car lst)))
          #t)
          (
            (and
              (pair? (car lst))
              (not (equal? 'else (caar lst)))
              (rex (cdr lst))
            )
          )
          (else #f)
         )
        )
      )
    )
    (lambda (sxpr)
      (and
        (list? sxpr)
        (< 1 (length sxpr))
        (equal? 'cond (car sxpr))
        (< 0 (length (cdr sxpr))) ;;
        (rex (cdr sxpr))
      )
    )
  )
)

(define parse-cond
	(letrec
    ((rex
      (lambda (condLst)
  		  (cond
          ((and
            (= 1 (length condLst))
            (equal? 'else (caar condLst) ))
            (decompose-list (cdar condLst)))
  			  (( = 1 (length condLst)) `(if ,(caar condLst) ,(decompose-list (cdar condLst) )))
  			  (else `(if ,(caar condLst) ,(decompose-list (cdar condLst)) ,(rex (cdr condLst))))
        )
      )
    )
   )
	 (lambda (sxpr)
    (parse (rex (cdr sxpr)))
    )
  )
)
