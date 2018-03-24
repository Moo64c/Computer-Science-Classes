#include <stdio.h>
#include "structs.h"

// Init stack.
struct stack_item *top;

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
