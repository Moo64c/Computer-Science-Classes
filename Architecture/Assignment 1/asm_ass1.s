%define num1_ptr r13
%define num2_ptr rbx
%define carry r9
%define links_count1 r12
%define links_count2 r11
%define result_ptr r8
%define digit1 rax
%define digit2 r10
%define result_curr r14

section .data
  special_counter: dq 0
  special_counter2: dq 0

section .text
  global _add_bignums, _subtract_bignums, _multiply_bignums
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

  %define num1_ptr r13
  %define num2_ptr rbx
  %define carry r9
  %define links_count1 r12
  %define links_count2 r11
  %define result_ptr r8
  %define digit1 rax
  %define digit2 r10
  %define result_curr r14

  section .text
  global _multiply

_multiply_bignums:
  ;start and save registers.
  push rbp
  mov rbp, rsp
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
  mov qword num1_ptr, [rdi+24]
  mov num2_ptr, [rsi+24]
  mov result_ptr, [rdx+24]
  mov result_curr, [rdx+24]
  ; Number of digits.
  mov links_count1, [rdi]
  mov links_count2, [rsi]
  mov carry, 0
  mov rcx, 10
  xor rax, rax

  _multiply_num2_loop:
    ;get the number from the link of the secound number
    mov digit2, [num2_ptr+16]

  _multiply_num1_loop:
    ;get the number from the link of the first number
    mov digit1, [num1_ptr+16]
    mul digit2
    add digit1, carry
    add digit1, qword[result_ptr+16]
    ; carry -> handle the addition of the carry
    jmp _multiply_carry_handle

  _multiply_get_next_num1_digit:
    ; get next link of first number
    mov num1_ptr, qword [num1_ptr+8]
    jmp _multiply_end_of_num1_check

  _multiply_end_of_num1_check:
    ; reached the end of iteration over num1
    cmp num1_ptr, 0
    ; call fucntion to handle the case were we need to add a add/mull carry
    je _multiply_handle_end_of_num1
    jmp _multiply_num1_loop_end

  ; decreasing counters
  _multiply_num1_loop_end:
    dec links_count1
    ; get next link of result number
    mov result_ptr, qword [result_ptr+8]
    cmp links_count1, 0
    jnz _multiply_num1_loop


  _multiply_num2_loop_end:
    ; prepere next digit of num2
    mov num2_ptr, qword[num2_ptr+8]
    cmp num2_ptr, 0
    ; check if we have reached the end of num2
    je _multiply_handle_end_of_num2
    ; if not, prepere for next iteration
    jmp _multiply_prepare_for_next_iteration

  _multiply_prepare_for_next_iteration:
    ; reset num1 to first digit
    mov qword num1_ptr, [rdi+24]
    ; start add from the next digit
    mov result_curr, [result_curr+8]
    ; get new starting digit for addtion
    mov result_ptr, result_curr
    ; carry = 0
    mov carry, 0
    ; reset links_count1
    mov links_count1, [rdi]

    ; decreasing counters
    dec links_count2
    cmp links_count2, 0
    jnz _multiply_num2_loop
    jmp _multiply_end


  _multiply_handle_end_of_num1:
    cmp carry, 0
    jg _multiply_add_at_the_end
    jmp _multiply_num1_loop_end


  _multiply_add_at_the_end:
    mov result_ptr, qword [result_ptr+8]
    mov qword [result_ptr+16], carry
    mov result_ptr, qword[result_ptr]
    mov carry, 0
    jmp _multiply_num1_loop_end


  _multiply_carry_handle:
    ; operate division: digit1=digit1/10 and rdx=remeinder
    idiv rcx
    mov [result_ptr+16], rdx
    ; store carry
    mov carry, digit1
    jmp _multiply_get_next_num1_digit


  _multiply_add_at_the_end_of_num2:
    mov digit1, qword[result_ptr+16]
    idiv rcx
    mov [result_ptr+16], rdx
    mov result_ptr, qword [result_ptr+8]
    add qword [result_ptr+16], digit1
    jmp _multiply_end

  _multiply_handle_end_of_num2:
    add qword [result_ptr+16], carry
    cmp qword [result_ptr+16], 10
    jge _multiply_add_at_the_end_of_num2
    jmp _multiply_end

  _multiply_end:
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
    mov [rbp-8], digit1
    pop rbp
    ret
