extern int _subtract_bignums(bignum*, bignum*);
extern int _add_bignums(bignum*, bignum*);
extern int _multiply_bignums(bignum*, bignum*, bignum*);

void resize_numbers(bignum* bn1, bignum* bn2);
void add_wrapper();
void add_carry(bignum* bn);
void sub_borrow(bignum* bn);
int compare_bignum(bignum* bn1, bignum* bn2);
int subtract_bignums(bignum* bn1, bignum* bn2);
bignum* create_result_container(bignum* bn1, bignum* bn2);

void add_carry(bignum* bn) {
    link* addedLink = (link*) malloc(sizeof(link));
    addedLink->num = 1;
    bn->head->prev = addedLink;
    addedLink->next = bn->head;
    bn->number_of_digits++;
    bn->head = addedLink;
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

  if (bn_iterator_1 == 0) {
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

  bignum* result = create_result_container(bn1, bn2);

  resize_numbers(bn1,bn1);
  _multiply_bignums(bn1, bn2, result);
  numstack_push_bignum(result);

  // Set them bytes free!!!!11
  clear_stack_item(si1);
  clear_stack_item(si2);

}

bignum* create_result_container(bignum* bn1, bignum* bn2) {
  long length_bn1 = bn1->number_of_digits;
  long length_bn2 = bn2->number_of_digits;

  // Create empty bignum.
  long length = ((length_bn1 >= length_bn2) ? length_bn1 : length_bn2) * 2;
  bignum* result = (bignum*) malloc(sizeof(bignum));
  result->number_of_digits = length;
  result->sign = bn1->sign == bn2->sign ? 0 : 1;
  result->head = NULL;
  result->last = NULL;
  for (int wordIndex = 0; wordIndex < length; wordIndex++) {
    // Create links for the bignum.
    link * newLink = createLink('0');
    if (result->head == NULL) {
      // First link.
      result->head = newLink;
      result->last = result->head;
    }
    else {
      newLink->prev = result->last;
      result->last->next = newLink;
      result->last = newLink;
    }
  }

  return result;
}
