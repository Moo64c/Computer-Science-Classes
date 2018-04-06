#include <stdio.h>
#include "structs.h"

// Init stack.
struct stack_item *top;
int word_type(char * word);
void print_stack();
void print_bn();
void clear_stack_item(stack_item * si);
void clear_bn(bignum * bn);
void numstack_push_bignum(struct bignum *number);
void after_operation_cleanup(int which, stack_item *si1, stack_item *si2);
struct link * createLink(char c);

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
 * Push new bignum to stack.
 */
void numstack_push_bignum(struct bignum *number) {
  struct stack_item* result_item = (stack_item *) malloc(sizeof(stack_item));
  result_item->value = number;
  numstack_push(result_item);
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
  int word_length = strlen(word);

  if (word_length < 1) {
    // Empty word.
    return 9999;
  }
  if (word_length == 1) {
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

  for (int index = 1; index < word_length; index++) {
    if(word[index] < '0' || word[index] > '9') {
      // Not a number.
      return 999;
    }
  }

  return type;
}

// Receives a number as a string, creates a bignum and wraps it as a stack_item.
struct stack_item * create_number(char * word) {
  int word_length = strlen(word);
  if (word_length < 1) {
    // Nothing?
    return create_number("0");
  }
  int wordIndex = 0;

  if (word[0] == '_') {
    // Skip first char when creating number.
    wordIndex++;
  }
  bignum * newBn = (bignum *) malloc(sizeof(bignum));
  newBn->number_of_digits = word_length - wordIndex;
  newBn->sign = wordIndex;
  newBn->head = NULL;
  newBn->last = NULL;

  int runUntil = (newBn->number_of_digits + newBn->sign);
  for ( ; wordIndex < runUntil; wordIndex++) {
    // Create links for the bignum.
    link * newLink = createLink(word[wordIndex]);
    if (newBn->head == NULL) {
      // First link.
      newBn->head = newLink;
      newBn->last = newBn->head;
    }
    else {
      newLink->prev = newBn->last;
      newBn->last->next = newLink;
      newBn->last = newLink;
    }
  }

  stack_item* si = (stack_item*) malloc(sizeof(stack_item));
  si->value = newBn;
  return si;
}

/**
 * Creates a new link for number creation.
 * @param  c number char to add.
 * @return pointer to the new link.
 */
struct link * createLink(char c) {
  link* newLink = (link*) malloc(sizeof(link));
  newLink->num = c - '0';
  newLink->prev = NULL;
  newLink->next = NULL;
  return newLink;
}

void print_bn(bignum * bn) {
  if (bn == NULL || bn->head == NULL) {
    printf("ERROR: Empty number sent to print_bn\n");
    return;
  }
  int zeros = 0;
  link* curr = bn->head;

  if (bn->sign) {
    printf("-");
  }

  while(curr != NULL) {
    if (print_all_digits) {
      printf("%i", curr->num);
    }

    if(!print_all_digits && (zeros > 0 || curr->num != 0 || curr->next == 0)) {
      printf("%i", curr->num);
      zeros = 1;
    }
    curr = curr->next;
  }
}

// Clears the stack from the memory.
void clear_numstack() {
  stack_item * iterator = numstack_pop();

  while (iterator != NULL) {
    clear_stack_item(iterator);
    iterator = numstack_pop();
  }
}

void clear_stack_item(stack_item * si) {
  clear_bn(si->value);
  free(si);
}

// Clears a bignum from memory.
void clear_bn(bignum * bn) {
    link *curr = bn->head;
    link *temp = curr;

    while(curr != NULL) {
        temp = curr;
        curr = curr->next;
        free(temp);
    }
    free(bn);
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

void after_operation_cleanup(int which_to_free, stack_item *si1, stack_item *si2) {
  if (debug > 1) {
    fprintf(stderr, "freeing si%d\n", which_to_free);
  }
  if (which_to_free == 1) {
    clear_stack_item(si1);
    numstack_push(si2);
  }
  if (which_to_free == 2) {
    clear_stack_item(si2);
    numstack_push(si1);
  }
}
