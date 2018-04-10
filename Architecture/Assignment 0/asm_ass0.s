section .data
  special_counter: dq 0
  special_counter2: dq 0

section .text
  global do_Str
  extern printf

do_Str:
  push rbp
  mov rbp, rsp
  mov rcx, rdi

  ; initialize answer
	mov	qword[special_counter], 0
	mov	qword[special_counter2], 0

	text_loop:
    mov bl, byte[rcx]

    cmp bl, 40
    je switch_paranthesis_1
    cmp bl, 41
    je switch_paranthesis_2

    ; Check if byte is special character: 0-64, 91-96, 123-127
    cmp bl, 65
    jl text_loop_increment

    ; Upper case letter.
    cmp bl, 91
    jl text_loop_check

    ; Special characters in between.
    cmp bl, 97
    jl text_loop_increment

    ; Lower case letter.
    cmp bl, 123
    jl text_loop_lowercase

    ; Other characters
    jmp text_loop_increment

    text_loop_lowercase:
       sub bl, 32
       mov byte[rcx], bl
       jmp text_loop_check

    text_loop_increment:
      inc qword[special_counter2]

    text_loop_check:
    ; increment pointer
    inc rcx
    ; check if byte pointed to is zero
  	cmp byte[rcx], 0
    ; keep looping until it is null terminated
    jnz text_loop


  ; return an (returned values are in rax)
  mov     rax, qword[special_counter2]
  mov     rsp, rbp
  pop     rbp
  ret


  switch_paranthesis_1:
    mov byte[rcx], 60
    jmp text_loop_increment

  switch_paranthesis_2:
    mov byte[rcx], 62
    jmp text_loop_increment
