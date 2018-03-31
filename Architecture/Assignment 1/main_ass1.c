// Global debug variable.
#define debug 1

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "stack.c"
#include "bn_operations.c"


/* Maximal line size */
#define MAX_LEN 10000

#define OP_ADD '+'
#define OP_SUBTRACT '-'
#define OP_MULTIPLY '*'
#define OP_DIVIDE '/'

extern int _add_bignums(bignum*, bignum*);
extern int _subtract_bignums(bignum*, bignum*);
char** split_string(char* buffer);
void clear_wordstack();

int stack_size = 0;
char ** stack = 0;

int main(void) {
  numstack_init();

  char str_buf[MAX_LEN];
  int counter = 0;

  /* Read user's command line string */
  fgets(str_buf, MAX_LEN, stdin);
  // Do the calculation
  // somefunc();
  //counter = do_Str (str_buf);
  // Create stack.
  stack = split_string(str_buf);

  int current_word_type = -1;
  for (int index = 0; index < stack_size; index++) {
    current_word_type = word_type(stack[index]);
    if (current_word_type == 0 || current_word_type == 1) {
      // tis a number
      // Add this to the number stack.
      numstack_push(create_number(stack[index]));
    }

    if (current_word_type == 2) {
      // Add.
      if (debug) {
        printf("adding numbers\n");
      }
      bignum* num1 = numstack_pop(stack, top)->value;
      bignum* num2 = numstack_pop(stack, top)->value;
      bignum* result;

      _add_bignums(num1, num2);
      stack_item * result_item = (stack_item *) malloc(sizeof(stack_item));
      result_item->value = num1;
      numstack_push(result_item);
    }

    if (current_word_type == 6) {
      // Print action.
      print_bn(top->value);
      printf("\n");
    }

    if (current_word_type == 7) {
      // Clear stack.
      clear_numstack();
      numstack_init();
    }

    if (current_word_type == 8) {
      // Exit.
      break;
    }
  }

  if (debug > 1) {
    print_stack();
  }

  // Clean the stacks.
  clear_numstack();
  clear_wordstack();
  return 0;
}

char** split_string(char* buffer) {
  int index;
  int wordcount = 0;
  char ch;

  for (index = 0; index < strlen(buffer); index ++){
      ch = buffer[index];
      if(ch == ' ' || (ch == '\0')){                   // if the character is blank, or null byte add 1 to the wordcounter
          wordcount += 1;
      }
  }

  // Start up a neat little words array.
  char** words = (char **) malloc((wordcount+1) * (sizeof (char*)));
  char *pch = strtok (buffer, " \n");
  index = 0;
  while (pch != NULL)
  {
    // Get next part.
    if (strlen(pch) < 1) {
      // No need to add this part..
      stack_size = index;
      break;
    }
    // Create space for the new string.
    words[index] = malloc((strlen(pch)) * sizeof(char));
    strncpy(words[index], pch, strlen(pch));

    // Print for good measure.
    if (debug > 1) {
      printf("%d: %s\n", index, words[index]);
    }
    pch = strtok(NULL, " \n");
    index++;
    stack_size = index;
  }

  return words;
}

// Clean the memory from the stack variables.
void clear_wordstack() {
  for (int index = 0; index < stack_size; index++) {
    free(stack[index]);
  }
  free(stack);
}
