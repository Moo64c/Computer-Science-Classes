// Global debug variable.
#define debug 0
#define print_all_digits 0

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

char** split_string(char* buffer);
void clear_wordstack(char ** stack);

int stack_size = 0;

int main(void) {
  char ** stack = 0;

  // Set starting values for valgrind reasons.
  char str_buf[MAX_LEN];
  for (int initIndex = 0; initIndex < MAX_LEN; initIndex++) {
    str_buf[initIndex] = 0;
  }
  numstack_init();

  /* Read user's command line string */
  fgets(str_buf, MAX_LEN, stdin);
  // Create stack.
  stack = split_string(str_buf);

  int current_word_type = -1;
  for (int index = 0; index < stack_size; index++) {
    current_word_type = word_type(stack[index]);
    if (current_word_type == 0 || current_word_type == 1) {
      // tis a number
      // Add this to the number stack.
      struct stack_item * newItem = create_number(stack[index]);
      numstack_push(newItem);
    }

    if (current_word_type == 2) {
      // Add.
      if (debug > 1) {
        printf("adding numbers\n");
      }
      add_wrapper();
    }

    if (current_word_type == 3) {
      // Subtract.
      stack_item * si2 = numstack_pop();
      stack_item * si1 = numstack_pop();
      bignum* bn2 = si2->value;
      bignum* bn1 = si1->value;

      int which = subtract_bignums(bn1, bn2);
      after_operation_cleanup(which, si1, si2);
    }
    if (current_word_type == 4) {
      // Multiply.
      multiply_wrapper();
    }

    if (current_word_type == 6) {
      // Print action.
      if (top != NULL) {
        print_bn(top->value);
        printf("\n");
      }
    }

    if (current_word_type == 7) {
      // Clear stack.
      clear_numstack();
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
  clear_wordstack(stack);
  return 0;
}

char** split_string(char* buffer) {
  int string_length = strlen(buffer);
  int index;
  int wordcount = 0;
  char ch;

  for (index = 0; index < string_length; index ++) {
      ch = buffer[index];
      if (ch == ' ' || (ch == '\0')) {
        // if the character is blank, or null byte add 1 to the wordcounter
        wordcount++;
      }
  }

  if (debug > 1) {
    printf("Found %i words\n", wordcount);
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
    words[index] = malloc((strlen(pch) + 1) * sizeof(char));
    strcpy(words[index], pch);

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
void clear_wordstack(char ** stack) {
  for (int index = 0; index < stack_size; index++) {
    free(stack[index]);
  }
  free(stack);
}
