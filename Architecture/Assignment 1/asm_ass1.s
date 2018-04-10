;defines for multiply.
%define current_digit_result r14
%define current_digit_1 rax
%define current_digit_2 rbx
%define p_result r8
%define carry r9
%define p_bn1 r10
%define digits_bn1 r11
%define p_bn2 r12
%define digits_bn2 r13

section .data
  special_counter: dq 0
  special_counter2: dq 0

section .text
  global _add_bignums, _subtract_bignums, _multiply_bignums, _divide_bignums
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
  mov qword p_bn1, [rdi+24]
  mov p_bn2, [rsi+24]
  mov p_result, [rdx+24]
  mov current_digit_result, [rdx+24]
  ; Number of digits.
  mov digits_bn1, [rdi]
  mov digits_bn2, [rsi]
  mov carry, 0
  mov rcx, 10
  xor rax, rax

  _multiply_num2_loop:
    ;get the number from the link of the secound number
    mov current_digit_2, [p_bn2+16]

  _multiply_num1_loop:
    ;get the number from the link of the first number
    mov current_digit_1, [p_bn1+16]
    mul current_digit_2
    add current_digit_1, carry
    add current_digit_1, qword[p_result+16]
    ; carry -> handle the addition of the carry
    jmp _multiply_carry_handle

  _multiply_get_next_num1_digit:
    ; get next link of first number
    mov p_bn1, qword [p_bn1+8]
    jmp _multiply_end_of_num1_check

  _multiply_end_of_num1_check:
    ; reached the end of iteration over num1
    cmp p_bn1, 0
    ; call fucntion to handle the case were we need to add a add/mull carry
    je _multiply_handle_end_of_num1
    jmp _multiply_num1_loop_end

  ; decreasing counters
  _multiply_num1_loop_end:
    dec digits_bn1
    ; get next link of result number
    mov p_result, qword [p_result+8]
    cmp digits_bn1, 0
    jnz _multiply_num1_loop


  _multiply_num2_loop_end:
    ; prepere next digit of num2
    mov p_bn2, qword[p_bn2+8]
    cmp p_bn2, 0
    ; check if we have reached the end of num2
    je _multiply_handle_end_of_num2
    ; if not, prepere for next iteration
    jmp _multiply_prepare_for_next_iteration

  _multiply_prepare_for_next_iteration:
    ; reset num1 to first digit
    mov qword p_bn1, [rdi+24]
    ; start add from the next digit
    mov current_digit_result, [current_digit_result+8]
    ; get new starting digit for addtion
    mov p_result, current_digit_result
    ; carry = 0
    mov carry, 0
    ; reset digits_bn1
    mov digits_bn1, [rdi]

    ; decreasing counters
    dec digits_bn2
    cmp digits_bn2, 0
    jnz _multiply_num2_loop
    jmp _multiply_end


  _multiply_handle_end_of_num1:
    cmp carry, 0
    jg _multiply_add_at_the_end
    jmp _multiply_num1_loop_end


  _multiply_add_at_the_end:
    mov p_result, qword [p_result+8]
    mov qword [p_result+16], carry
    mov p_result, qword[p_result]
    mov carry, 0
    jmp _multiply_num1_loop_end


  _multiply_carry_handle:
    ; operate division: current_digit_1=current_digit_1/10 and rdx=remeinder
    idiv rcx
    mov [p_result+16], rdx
    ; store carry
    mov carry, current_digit_1
    jmp _multiply_get_next_num1_digit


  _multiply_add_at_the_end_of_num2:
    mov current_digit_1, qword[p_result+16]
    idiv rcx
    mov [p_result+16], rdx
    mov p_result, qword [p_result+8]
    add qword [p_result+16], current_digit_1
    jmp _multiply_end

  _multiply_handle_end_of_num2:
    add qword [p_result+16], carry
    cmp qword [p_result+16], 10
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
    mov [rbp-8], current_digit_1
    pop rbp
    ret

; ============================ DIVIDE =================

_divide_bignums:
  ; Start the function.
  push rbp
  mov rbp, rsp

  ; bn1's head
  mov r10, [rdi+16]
  ; bn2's last
  mov rbx, [rsi+24]
  ; Result's last
  mov r11, [rdx+24]
  ; copy Bn2's last.
  mov r15, [rcx+24]
  mov r9, 0
  ; rax = 0.
  xor rax, rax

  _divide_main_loop:
    mov rbx, [rsi+24]
    mov r15, [rcx+24]
    jmp _divide_compare_div

  _divide_loop:
    ; Get the value in the last link.
    mov r12, [rbx+16]
    mov r8, [rbx+16]
    add r8, r9
    add qword r12, r8
    cmp qword r12, 10
    jge _divide_have_carry
    jmp _divide_no_carry

  _divide_have_carry:
    sub r12, 10
    mov r9, 1
    jmp _divide_next

  _divide_no_carry:
    mov r9, 0

  _divide_next:
    mov qword [rbx+16], r12
    ; Increment link.
    mov rbx, qword [rbx+8]
    mov rbx, qword [rbx+8]
    cmp rbx, 0
    je _divide_end_add

    cmp r9, 1
    je _divide_last_carry
    jmp _divide_end_add

  _divide_last_carry:
    call add_carry

  _divide_end_add:
  jmp _divide_main_loop

  _divide_inc_count:
    inc qword [r11+16]
    cmp qword [r11+16], 10
    jge _divide_count_carry
    jmp _divide_end_inc

  _divide_count_carry:
    sub qword [r11+16], 10
    mov r11, qword [r11+8]
    jmp _divide_inc_count

  _divide_end_inc:
    mov r11, [rdx+24]
    jmp _divide_loop

  _divide_compare_div:
    mov rbx, [rsi+16]
    mov r12, [r10+16]
    mov r13, [rbx+16]
    cmp r12, r13
    jl _divide_end_div
    jg _divide_num1_is_bigger

  mov r10, [r10]
  mov rbx, [rbx]
  cmp rbx, 0
  je  _divide_inc_count
  jmp _divide_compare_div

  _divide_num1_is_bigger:
    mov rbx, [rsi+24]
    jmp _divide_inc_count

  _divide_end_div:
    ; Restore and return.
    mov [rbp-8], rax
    pop rbp
    ret
