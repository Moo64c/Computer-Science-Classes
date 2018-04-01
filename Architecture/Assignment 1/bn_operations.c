extern int _subtract_bignums(bignum*, bignum*);
extern int _add_bignums(bignum*, bignum*);

void resize_numbers(bignum* bn1, bignum* bn2);
void add_carry(bignum* bn);
void sub_borrow(bignum* bn);
int compare_bignum(bignum* bn1, bignum* bn2);
void subtract_bignums(bignum* bn1, bignum* bn2);

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

int compare_bignum(bignum* bn1, bignum* bn2){
  int result = (int) (bn1->number_of_digits - bn2->number_of_digits);

  resize_numbers(bn1, bn2);
  if (result!=0) {
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

void subtract_bignums(bignum* bn1, bignum* bn2) {
    int comp = compare_bignum(bn1, bn2);
    if  (comp > 0 &&
        (bn1->sign + bn2->sign == 0 || bn1->sign + bn2->sign == 2)
      ) {
      // Bn1 is larger, but numbers are both positive or both negative.
      _subtract_bignums(bn1, bn2);
      numstack_push_bignum(bn1);
    }
    else if (comp < 0 &&
            (bn1->sign + bn2->sign == 0 || bn1->sign + bn2->sign == 2)
      ) {
      // Bn2 is larger, but numbers are both positive or both negative.
      _subtract_bignums(bn2, bn1);
      // Switch sign are for bn2 (since we're subtracting a bigger number).
      bn2->sign = 1 - bn2->sign;
      numstack_push_bignum(bn2);
    }
    else if (bn1->sign ^ bn2->sign) {
      // Only one of the numbers is negative and the other is positive.
      _add_bignums(bn1, bn2);
      numstack_push_bignum(bn1);
    }
    else if(comp == 0) {
      // Numbers are equal.
      _subtract_bignums(bn1, bn2);
      bn1->sign = 0;
      numstack_push_bignum(bn1);
    }
}

void free_bigNum(bignum * bn){
    link *curr = bn->last;
    link *temp = bn->last;
    while(curr!=0){
        temp=curr;
        curr=curr->prev;
        free(temp);
    }
}
