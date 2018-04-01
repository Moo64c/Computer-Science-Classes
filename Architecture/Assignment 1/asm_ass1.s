section .data
  special_counter: dq 0
  special_counter2: dq 0

section .text
  global _add_bignums, _subtract_bignums
  extern add_carry, sub_borrow

;================ ADD ============================ 
_add_bignums:
  ; standard start.
  push rbp
  mov rbp, rsp
  mov rax, [rdi+24]
  mov rbx, [rsi+24]
  mov rcx, [rdi]
  mov r9, 0

  loop_add_bignums:
    ; Get number of in the last link
    mov rdx, [rax+16]
    mov r8, [rbx+16]
    add r8, r9
    add qword rdx, r8
    cmp qword rdx, 10
    jge add_have_carry
    jmp add_no_carry

  add_have_carry:
    sub rdx, 10
    mov r9, 1
    jmp add_next
  add_no_carry:
    mov r9, 0

  add_next:
    ; Move to next link.
    mov qword [rax+16], rdx
    mov rax, qword [rax+8]
    mov rbx, qword [rbx+8]
    loop loop_add_bignums

  cmp r9, 1
  je add_last_carry
  jmp end_add

  add_last_carry:
    call add_carry

  end_add:
    mov [rbp-8], rax
    pop rbp
    ret

;================ SUBTRACT ============================
_subtract_bignums:
  ; standard start.
  push rbp
  mov rbp, rsp
  mov rax, [rdi+24]
  mov rbx, [rsi+24]
  mov rcx, [rdi]
  mov r9, 0

  _subtract_bignums_loop:
    ; Get number of in the last link
    mov rdx, [rax+16]
    mov r8, [rbx+16]
    sub rdx, r9
    sub qword rdx, r8
    cmp qword rdx, 0
    jl subtract_have_borrow
    jmp subtract_no_borrow

  subtract_have_borrow:
    add rdx, 10
    mov r9, 1
    jmp subtract_next

  subtract_no_borrow:
    mov r9, 0

  subtract_next:
    ; Move to next link.
    mov qword [rax+16], rdx
    mov rax, qword [rax+8]
    mov rbx, qword [rbx+8]
    loop _subtract_bignums_loop

  cmp r9, 1
  je subtract_last_borrow
  jmp end_sub

  subtract_last_borrow:
    call sub_borrow

  end_sub:
    mov [rbp-8], rax
    pop rbp
    ret
