typedef struct bignum {
  long number_of_digits;
  char *digit;
} bignum;

typedef struct stack_item {
  struct stack_item *next;
  struct bignum *value;
} stack_item;

void numstack_init();
void numstack_push(struct stack_item *item);
struct stack_item *numstack_pop();
