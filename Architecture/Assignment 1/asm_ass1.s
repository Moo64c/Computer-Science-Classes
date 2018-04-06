section .data
  special_counter: dq 0
  special_counter2: dq 0

section .text
  global _add_bignums, _subtract_bignums
  extern add_carry, sub_borrow

;================ ADD ============================
_add_bignums:
  ; Save rbp.
  push rbp
  mov rbp, rsp

  ; Get parameters.
  mov rax, [rdi+24]
  mov rbx, [rsi+24]
  mov rcx, [rdi]
  ; Reset result.
  mov r9, 0

  _add_loop:
    mov rdx, [rax+16]
    mov r8, [rbx+16]
    add r8, r9
    add qword rdx, r8
    cmp qword rdx, 10
    jge _add_have_carry
    jmp _add_no_carry

  _add_have_carry:
    sub rdx, 10
    mov r9, 1
    jmp _add_next

  _add_no_carry:
    mov r9, 0

  _add_next:
    mov qword [rax+16], rdx
    ; Next link pointer (at rax+8)
    mov rax, qword [rax+8]
    mov rbx, qword [rbx+8]
    loop _add_loop

  cmp r9, 1
  je _add_last_carry
  jmp _add_end_add

  _add_last_carry:
    call add_carry

  _add_end_add:
    ; Save result.
    mov [rbp-8], rax
    ; Restore before return.
    pop rbp
    ret

;================ SUBTRACT ============================
_subtract_bignums:
  push rbp
  mov rbp, rsp

  ; Save registers.
  push rdx
  push rcx
  push rbx
  push r8
  push r9
  push r10
  push r11
  push r12
  push r13
  push r14

  ; Get variables.
  mov rax, [rdi+24]
  mov rbx, [rsi+24]
  mov rcx, [rdi]
  mov r9, 0

  _subtract_loop:
  ; get num of the last link
    mov rdx, [rax+16]
    mov r8, [rbx+16]
    sub rdx, r9
    sub qword rdx, r8
    cmp qword rdx, 0
    jl _subtract_have_borrow
    jmp _subtract_no_borrow

    _subtract_have_borrow:
      add rdx, 10
      mov r9, 1
      jmp _subtract_next
    _subtract_no_borrow:
      mov r9, 0

    _subtract_next:
      mov qword [rax+16], rdx
      ; Next link at rax+8
      mov rax, qword [rax+8]
      mov rbx, qword [rbx+8]
      loop _subtract_loop

    cmp r9, 1
    je _subtract_last_borrow
    jmp _subtract_end_sub

  _subtract_last_borrow:
    call sub_borrow

_subtract_end_sub:
  ; Restore registers.
  pop r14
  pop r13
  pop r12
  pop r11
  pop r10
  pop r9
  pop r8
  pop rbx
  pop rcx
  pop rdx
  mov [rbp-8], rax

  ; Pop RBP and return.
  pop rbp
  ret
