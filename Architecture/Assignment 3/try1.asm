sbn index, start_of_memory, +1
sbn A, index, +1
sbn index, one, +1
sbn B, index, +1
sbn index, one, +1
sbn C, index, +1

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

zero: 0 99
neg_one: -1 100
one: 1 101
index: 0 102
A: 0 103
B: 0 104
C: 0 105
start_of_memory: 106
neg_start_of_memory: -423432























102, 106, 3
103, 102, 6
102, 101, 9
104, 102, 12
102, 101, 15
105, 102, 18
103, 101, 27
103, 100, 57
100, 99, 57
103, 100, 57
104, 101, 39
104, 100, 57
100, 99, 57
104, 100, 57
105, 101, 51
105, 100, 57
100, 99, 57
105, 100, 57
100, 99, 96
103, 104, 63
102, 101, 69
102, 102, 66
102, 105, 69
103, 103, 72
103, 102, 75
102, 101, 78
104, 104, 81
104, 102, 84
102, 101, 87
105, 105, 90
105, 102, 93
100, 99, 18
0, 0, 0
0
-1
1
0
0
0
0
