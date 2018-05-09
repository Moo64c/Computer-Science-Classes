sbn index, start_of_memory, +1
sbn A, index, +1
sbn index, one, +1
sbn B, index, +1
sbn index, one, +1
sbn C, index, +1
sbn A, neg_start_of_memory, +1
sbn B, neg_start_of_memory, +1
sbn C, neg_start_of_memory, +1

; Check A == 0 && B == 0 && C == 0
; if so, exit program.
check_zero: 6
  ; if A -1 < 0
  sbn A, one, check_zero_2
  ; A-1 > 0, continue loop.
  sbn A, neg_one, continue_loop
  ; jmp continue_loop
  sbn neg_one, zero, continue_loop
check_zero_2: 9
  ; Add 1 back, if still negative then we keep going
  sbn A, neg_one, continue_loop
  ; A == 0. Check B
check_zero_3: 10
  ; if b -1 < 0
  sbn B, one, check_zero_4
  ; B-1 > 0, continue loop.
  sbn B, neg_one, continue_loop
  ; jmp continue_loop
  sbn neg_one, zero, continue
check_zero_4: 13
  ; Add 1 back, if still negative then we keep going
  sbn B, neg_one, continue_loop
  ; B == 0, check C.
check_zero_5: 14
  ; if C -1 < 0
  sbn C, one, check_zero_6
  ; C-1 > 0, continue loop.
  sbn C, neg_one, continue_loop
  ; jmp continue_loop
  sbn neg_one, zero, continue
check_zero_6: 17
  ; Add 1 back, if still negative then we keep going
  sbn C, neg_one, continue_loop
  ; A, B, C all equal 0. Exit.
  sbn neg_one, zero, exit
continue_loop: 19
  sbn A, B, loop_result_neg
  sbn index, one, re_loop

loop_result_neg: 21
  ; jmp to new index.
  sbn index, index, +1
  sbn index, C, +1
re_loop: 23
  sbn A, A, +1
  sbn A, index, +1
  sbn index, one, +1
  sbn B, B, +1
  sbn B, index, +1
  sbn index, one, +1
  sbn C, C, +1
  sbn C, index, +1

  ; jump to start of loop (check A,B,C != 0)
  sbn neg_one, zero, check_zero

exit:
  sbn 0, 0, 0

three: 3
zero: 0
neg_one: -1
one: 1
index: 0
; TODO
Aval: 0
A: 0
Areturn: neg_one, zero, return_address
Bval: 0
B: 0
Breturn: neg_one, zero, return_address
Cval: 0
C: 0
Creturn: neg_one, zero, return_address
start_of_memory: 423432
neg_start_of_memory: -423432



















index, start_of_memory, 3
A, index, 6
index, one, 9
B, index, 12
index, one, 15
C, index, 18
A, one, 27
A, neg_one, 57
neg_one, zero, 57
A, neg_one, 57
B, one, 39
B, neg_one, 57
neg_one, zero, 57
B, neg_one, 57
C, one,
C, neg_one, 19
neg_one, zero, 19
C, neg_one, 19
neg_one, zero, exit
A, B, 21
index, one, 23
index, index, 22
index, C, 23
A, A, 24
A, index, 25
index, one, 26
B, B, 27
B, index, 28
index, one, 29
C, C, 30
C, index, 31
neg_one, zero, 6
0, 0, 0
3
0
-1
1
0
0
0
0
