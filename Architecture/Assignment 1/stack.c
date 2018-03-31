#include <stdio.h>
#include "structs.h"

// Init stack.
struct stack_item *top;
int word_type(char * word);
void print_stack();
void print_bn();
void clear_bn(bignum * bn);

void numstack_init() {
  top = NULL;
}

/**
 * Push new item to stack.
 */
void numstack_push(struct stack_item *item) {
  item->next = top;
  top = item;
}

/**
 * Pop an item from the stack.
 */
struct stack_item *numstack_pop() {
  if (top == NULL) {
    // Stack is empty.
    return NULL;
  }

  // Save item to return.
  struct stack_item *item = top;
  top = item->next;
  return item;
}

// Check word type.
// @param char * word string to check.
// @return according to word:
// 0 - number
// 1 - negative number
// 2 - plus
// 3 - minus
// 4 - times
// 5 - divide
// 6 - print
// 7 - clear stack
// 8 - exit
// 999 - unknown
// 9999 - Empty word.
int word_type(char * word) {
  if (strlen(word) < 1) {
    // Empty word.
    return 9999;
  }
  if (strlen(word) == 1) {
    if (word[0] == '+') {
      return 2;
    }
    if (word[0] == '-') {
      return 3;
    }
    if (word[0] == '*') {
      return 4;
    }
    if (word[0] == '/') {
      return 5;
    }
    if (word[0] == 'p') {
      return 6;
    }
    if (word[0] == 'c') {
      return 7;
    }
    if (word[0] == 'q') {
      return 8;
    }
  }

  int type;
  if (word[0] == '_') {
    // Negative number.
    type = 1;
  }
  else if(word[0] >= '0' && word[0] <= '9') {
    type = 0;
  }
  else {
    return 999;
  }

  for (int index = 1; index < strlen(word); index++) {
    if(word[index] < '0' || word[index] > '9') {
      // Not a number.
      return 999;
    }
  }

  return type;
}

// Receives a number as a string, creates a bignum and wraps it as a stack_item.
struct stack_item * create_number(char * word) {
  int index = 0;

  if (strlen(word) < 1) {
    // Empty word.
    return create_number("0");
  }

  if (debug > 1) {
    printf("creating number from %s, length %d\n", word, (int) strlen(word));
  }

  bignum* bn = (bignum*) malloc(sizeof(bignum));
  bn->number_of_digits = strlen(word);
  bn->sign = 0;

  if (word[0] == '_') {
    // negative number.
    bn->sign = 1;
    index = 1;
    if (strlen(word) == 1) {
      // recreate these steps for the number 0.
      free(bn);
      return create_number("0");
    }
  }

  // Create first number.
  bn->head = (link *) malloc(sizeof(link));
  bn->head->num = (word[index] - '0');
  bn->last = bn->head;
  index++;

  for ( ; index < bn->number_of_digits; index++) {
    // Add another link in the chain.
    link* newLink = (link*) malloc(sizeof(link));
    newLink->prev = bn->last;
    newLink->num = word[index] - '0';
    bn->last->next = newLink;
    bn->last = newLink;
  }

  stack_item* si = (stack_item*) malloc(sizeof(stack_item));
  si->value = bn;
  if (debug > 1) {
    printf ("Adding number ");
    print_bn(bn);
    printf (" to stack\n");
  }
  return si;
}

void print_bn(bignum * bn) {
  int zeros = 0;
  link* curr = bn->head;
  if(bn->sign)
    printf("-");
  while(curr != 0) {
    if(zeros > 0 || curr->num != 0 || curr->next == 0) {
        printf("%i", curr->num);
        zeros = 1;
    }
    curr = curr->next;
  }
}

// Clears the stack from the memory.
void clear_numstack() {
  stack_item * iterator = top;
  stack_item * last = 0;

  while (iterator != NULL) {
    last = iterator;
    clear_bn(iterator->value);
    iterator = iterator->next;
    free(last);
  }
}

// Clears a bignum from memory.
void clear_bn(bignum * bn) {
  link * current = bn->head;
  link * last;
  while(current != NULL) {
    last = current;
    current = current->next;
    free(last);
  }
}

void print_stack() {
  printf("\n=== Printing number stack ====\n\n");
  stack_item * iterator = top;
  while (iterator != NULL) {
    print_bn(iterator->value);
    iterator = iterator->next;
    printf("\n");
  }
  printf("==============================\n\n");
}
