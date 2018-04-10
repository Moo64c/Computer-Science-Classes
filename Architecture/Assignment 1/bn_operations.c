extern int _subtract_bignums(bignum*, bignum*);
extern int _add_bignums(bignum*, bignum*);
extern int _multiply_bignums(bignum*, bignum*, bignum*);

void resize_numbers(bignum* bn1, bignum* bn2);
void add_wrapper();
void add_carry(bignum* bn);
void sub_borrow(bignum* bn);
int compare_bignum(bignum* bn1, bignum* bn2);
int div_comparer(bignum * bn1,bignum * bn2);
int subtract_bignums(bignum* bn1, bignum* bn2);
void div_algorithm(bignum *bn1, bignum *bn2, bignum * divFactor);
int bn_is_zero(bignum* bn1);

bignum* divQuotiant;
bignum* divRemainder;

void add_carry(bignum* bn) {
    link* addedLink = (link*) malloc(sizeof(link));
    addedLink->num = 1;
    bn->head->prev = addedLink;
    addedLink->next = bn->head;
    bn->number_of_digits++;
    bn->head = addedLink;
}


 bignum* init_mul_ptr(long length){
     long new_length = length*2;
     bignum* result= (bignum*) malloc(sizeof(bignum));
     result->number_of_digits = new_length;
     result->head = (link*) malloc(sizeof(link));
     result->last = result->head;
     result->last->num = 0;
     for(int i=0; i<new_length;i++){
         link* newLink = (link*) malloc(sizeof(link));
         newLink->num = 0;
         newLink->prev = result->last;
         result->last->next = newLink;
         result->last = newLink;
     }
     return result;
 }

void resize_numbers(bignum* bn1, bignum* bn2) {

  while(bn1->number_of_digits > bn2->number_of_digits) {
    // Add zero before the actual number.
    link* addedLink = (link*) malloc(sizeof(link));
    addedLink->num = 0;
    bn2->head->prev = addedLink;
    addedLink->next = bn2->head;
    bn2->number_of_digits++;
    bn2->head = addedLink;
  }

  while(bn1->number_of_digits < bn2->number_of_digits) {
    // Add zero before the actual number.
    link* addedLink = (link*) malloc(sizeof(link));
    addedLink->num = 0;
    addedLink->prev = NULL;
    bn1->head->prev = addedLink;
    addedLink->next = bn1->head;
    bn1->head = addedLink;
    bn1->number_of_digits++;
  }
}

void sub_borrow(bignum* bn){
    link* oldLink = bn->head;
    bn->head = bn->head->next;
    free(oldLink);
    bn->number_of_digits --;
}

/**
 * Compare bignum values (ignore sign).
 * @param  bn1
 * @param  bn2
 * @return
 *  - 0 on same value.
 *  - positive value if bn1 is bigger.
 *  - negative value if bn2 is bigger.
 */
int compare_bignum(bignum* bn1, bignum* bn2){
  int result = (int) (bn1->number_of_digits - bn2->number_of_digits);

  resize_numbers(bn1, bn2);
  if (result != 0) {
    return result;
  }
  link* bn_iterator_1 = bn1->head;
  link* bn_iterator_2 = bn2->head;
  while (bn_iterator_1 != 0 &&
         bn_iterator_1->num == bn_iterator_2->num)
  {
    bn_iterator_1 = bn_iterator_1->next;
    bn_iterator_2 = bn_iterator_2->next;
  }

  if (bn_iterator_1 == NULL) {
    return 0;
  }

  return bn_iterator_1->num - bn_iterator_2->num;
}

/**
 * Subtracts bn1 from bn2.
 * @param  bn1
 * @param  bn2
 * @return 1/2 number of the bignum to free.
 */
int subtract_bignums(bignum* bn1, bignum* bn2) {
    int comp = compare_bignum(bn1, bn2);
    if  (comp > 0 &&
        (bn1->sign + bn2->sign == 0 || bn1->sign + bn2->sign == 2)
      ) {
      // Bn1 is larger, but numbers are both positive or both negative.
      _subtract_bignums(bn1, bn2);
      return 2;
    }
    else if (comp < 0 &&
            (bn1->sign + bn2->sign == 0 || bn1->sign + bn2->sign == 2)
      ) {
      // Bn2 is larger, but numbers are both positive or both negative.
      _subtract_bignums(bn2, bn1);
      // Switch sign are for bn2 (since we're subtracting a bigger number).
      bn2->sign = 1 - bn2->sign;
      return 1;
    }
    else if (bn1->sign ^ bn2->sign) {
      // Only one of the numbers is negative and the other is positive.
      _add_bignums(bn1, bn2);
      return 2;
    }
    else if(comp == 0) {
      // Numbers are equal.
      _subtract_bignums(bn1, bn2);
      bn1->sign = 0;
      return 2;
    }
    // Error.
    return -1;
}

// Adds two numbers, pushes the result, and frees the unused variable.
void add_wrapper() {
  stack_item * si2 = numstack_pop();
  stack_item * si1 = numstack_pop();
  bignum* bn2 = si2->value;
  bignum* bn1 = si1->value;

  // Resize variables.
  resize_numbers(bn1, bn2);
  // Compare bignums.
  int comp = compare_bignum(bn1, bn2);

  int which_to_free = 0;
  if (bn1->sign == 0) {
    // BN1 is positive.
    if (bn2->sign == 0) {
      // BN2 is positive.
      // Just add.
      _add_bignums(bn1, bn2);
      which_to_free = 2;
    }
    else {
      // BN2 is negative.
      // Subtract BN2 from BN1.
      bn2->sign = 0;
      _subtract_bignums(bn1, bn2);
      if (comp < 0) {
        // BN2 is bigger, so the sign should be negative.
        bn1->sign = 1;
      }
      which_to_free = 2;
    }
  }
  else if (bn1->sign == 1) {
    // BN1 is negative
    if (bn2->sign == 0) {
      // BN2 is positive.
      // Subtract BN1 from BN2.
      bn1->sign = 0;
      _subtract_bignums(bn1, bn2);
      if (comp > 0) {
        // BN1 is bigger than BN2. We should return a negative number.
        bn1->sign = 1;
      }
      which_to_free = 2;
    }
    else {
      // Both numbers are negative.
      // Just add.
      _add_bignums(bn1, bn2);
      which_to_free = 2;
    }
  }
  else {
    // Error.
    printf("Error with sign in bn1.\n");
    return;
  }
  after_operation_cleanup(which_to_free, si1, si2);
}

void multiply_wrapper() {
  stack_item * si2 = numstack_pop();
  stack_item * si1 = numstack_pop();
  bignum* bn2 = si2->value;
  bignum* bn1 = si1->value;

  bignum* result = create_result_container_wrapper(bn1, bn2);

  resize_numbers(bn1,bn1);
  _multiply_bignums(bn1, bn2, result);
  // Fix sign.
  result->sign = (bn1->sign == bn2->sign) ? 0 : 1;
  numstack_push_bignum(result);

  // Set them bytes free!!!!11
  clear_stack_item(si1);
  clear_stack_item(si2);
}

/**
 * Integer-divides the first two bignums in the stack.
 * (bn1/bn2).
 */
void divide_wrapper() {
  stack_item * si2 = numstack_pop();
  stack_item * si1 = numstack_pop();
  bignum* bn2 = si2->value;
  bignum* bn1 = si1->value;

  resize_numbers(bn1, bn2);
  divQuotiant = create_result_container(bn1->number_of_digits);
  divRemainder = create_result_container(bn1->number_of_digits);
  bignum* divFactor = create_result_container(bn1->number_of_digits);

  // Start factor at 1.
  divFactor->last->num = 1;
  if(bn_is_zero(bn2)) {
      printf("Error: divide by zero.\n");
      numstack_push(si2);
  }
  else if(div_comparer(bn1, bn2) < 0) {
    // Bn2 is bigger - push 0.
    numstack_push_bignum(divQuotiant);
    // Clean up.
    clear_bn(divFactor);
  }
  else if (div_comparer(bn1, bn2) == 0) {
    // Bignums are equal - push 1.
    numstack_push_bignum(divFactor);
    // Clean up.
    clear_bn(divQuotiant);
  }
  else {
    // Start division algorithm (see explanation below).
    div_algorithm(bn1, bn2, divFactor);
    bignum * result = clone_bignum(divQuotiant);
    if(bn1->sign ^ bn2->sign && !bn_is_zero(result)) {
      result->sign = 1;
    }
    numstack_push_bignum(result);
    // Cleanup.
    clear_bn(divFactor);
  }
}

int bn_is_zero(bignum* bn1) {
  link* iterator = bn1->head;
  while(iterator != NULL) {
    if (iterator->num != 0) {
      // Not zero...
      return 0;
    }
    iterator = iterator->next;
  }
  // Yes it is a zero.
  return 1;
}

/** USING MAYER's algorithm
  * Idea behind the algorithm:
  * Recusively (incrementing x) find if dividing bn1 by 2^x
  * reduces it below bn2.
  * if so, add 2^(x -1) to a "divisor list" that's added together.
  * remove the part that can be divided from bn1 for future calculations.
  * EXAMPLE: format - (bn1, bn2, result, factor)
  ; 2000 / 15 : (2000, 15, 0, 1)
  ; -> (2000, 30, 0, 2) -> (2000, 60, 0, 4) -> (2000, 120, 0, 8)
  ; -> (2000, 240, 0 , 16) -> (2000, 480, 0 , 32) -> (2000, 960, 0, 64)
  ; -> (2000, 1920, 0, 128) -> (2000, 3840, 0, 256) --- FOUND
  ; (2000 - 1920 = 80, 3840, 0 + (256 / 2), 256) = (80, 3840, 128, 256)
  ; (bn1 < bn2 - keep going back in divisors until bn1 > bn2).
  ; -> .... -> (80, 60, 128, 8) FOUND!
  ; -> (80-60, 60, 128+(8/2), 8) = (20, 60, 132, 8)
  ; (bn1 < bn2 - keep going back in divisors until bn1 > bn2).
  ; -> ... -> (20 - 15, 15, 132 + (1 /2), 1) =  (5, 15, 132.5, 1)
  ; (there's a remainder... if we continue we should get 133+1/3)
  */
void div_algorithm(bignum *bn1, bignum *bn2, bignum * divFactor) {
  if(div_comparer(bn1, bn2) < 0){
    divRemainder = bn1;
    divQuotiant = create_result_container(bn1->number_of_digits);
  }
  else {
    bignum * first = clone_bignum(bn2);
    bignum * second = clone_bignum(divFactor);
    _add_bignums(second, divFactor);
    _add_bignums(first, bn2);
    div_algorithm(bn1, first, second);

    if(div_comparer(divRemainder, bn2) >= 0 ) {
      _add_bignums(divQuotiant, divFactor);
      _subtract_bignums(divRemainder, bn2);
    }
  }
}

int div_comparer(bignum * bn1, bignum * bn2) {
  resize_numbers(bn1, bn2);
  link* bn1_link = bn1->head;
  link* bn2_link = bn2->head;
  while (bn1_link != 0 && bn1_link->num == bn2_link->num) {
      bn1_link = bn1_link->next;
      bn2_link = bn2_link->next;
  }
  if (bn1_link == NULL) {
    return 0;
  }
  return bn1_link->num - bn2_link->num;
}
