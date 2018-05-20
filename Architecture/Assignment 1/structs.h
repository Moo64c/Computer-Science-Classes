/**
 * Part of the chain to create a bignum.
 * each link holds up to 18 digits (max size of long)
 */
typedef struct link { // sizeof = 24 bit
    struct link * next;
    struct link * prev;
    int num;
} link;


typedef struct bignum {
  long number_of_digits;
  int sign;
  link *head;
  link *last;
} bignum;

typedef struct stack_item {
  struct stack_item *next;
  struct bignum *value;
} stack_item;

void numstack_init();
void numstack_push(struct stack_item *item);
struct stack_item *numstack_pop();
