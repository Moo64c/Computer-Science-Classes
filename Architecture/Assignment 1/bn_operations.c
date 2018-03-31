
void equalize_links(bignum* bn1, bignum* bn2);
void add_carry(bignum* bn);
void sub_borrow(bignum* bn);
int compare_bignum(bignum* bn1, bignum* bn2);
void subtract(bignum* num1, bignum* num2);

void add_carry(bignum* bn) {
    link* newLink = (link*) malloc(sizeof(link));
    newLink->num = 1;
    bn->head->prev = newLink;
    newLink->next = bn->head;
    bn->head = newLink;
    bn->number_of_digits++;
}

void equalize_links(bignum* bn1, bignum* bn2){
  while(bn1->number_of_digits > bn2->number_of_digits){
    link* newLink = (link*) malloc(sizeof(link));
    newLink->num = 0;
    bn2->head->prev = newLink;
    newLink->next = bn2->head;
    bn2->head = newLink;
    bn2->number_of_digits++;
  }
  while(bn1->number_of_digits < bn2->number_of_digits){
    link* newLink = (link*) malloc(sizeof(link));
    newLink->num = 0;
    bn1->head->prev = newLink;
    newLink->next = bn1->head;
    bn1->head = newLink;
    bn1->number_of_digits++;
  }
}

void sub_borrow(bignum* bn){
    link* oldLink = bn->head;
    bn->head = bn->head->next;
    free(oldLink);
    bn->number_of_links --;
}

int compare_bignum(bignum* bn1, bignum* bn2){
    int ans = (int) (bn1->number_of_links - bn2->number_of_links);
    equalize_links(bn1,bn2);
    if (ans!=0)
        return ans;
    link* curr1=bn1->head;
    link* curr2 = bn2->head;
    while(curr1!=0 && curr1->num == curr2->num){
        curr1 = curr1->next;
        curr2 = curr2->next;
    }
    if(curr1 == 0)
        return 0;
    return curr1->num - curr2->num;

}

void subtract(bignum* num1, bignum* num2){
    int comp = compare_bignum(num1,num2);
    if (comp > 0 && (num1->sign+num2->sign == 0 || num1->sign+num2->sign == 2)) { // 1 6
        _subtract(num1, num2);
        push(num1);
//        free_bigNum(num2);
    }
    else if(comp < 0 && (num1->sign+num2->sign == 0 || num1->sign+num2->sign == 2)){ // 2 7
        _subtract(num2, num1);
        num2->sign = 1 - num2->sign; // if =1 change to 0 , if =0 change to 1
        push(num2);
 //       free_bigNum(num1);
    }
    else if(num1->sign ^ num2->sign){ // 3 4 5 8
        _add(num1, num2);
        push(num1);
//        free_bigNum(num2);
    }
    else if(comp == 0){
        _subtract(num1, num2);
        num1->sign = 0;
        push(num1);
//        free_bigNum(num2);
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
 //   free(bn);
}
