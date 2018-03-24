#include <stdio.h>
#include <string.h>
#include "stack.c"

/* Maximal line size */
#define MAX_LEN 10000

#define OP_ADD '+'
#define OP_SUBTRACT '-'
#define OP_MULTIPLY '*'
#define OP_DIVIDE '/'

extern int do_Str (char*);
char** split_string(char* buffer);
int ended = 0;

int main(void) {
  numstack_init();

  char str_buf[MAX_LEN];
  int counter = 0;

  while (!ended) {
    /* Read user's command line string */
    fgets(str_buf, MAX_LEN, stdin);
    // Do the calculation
    // somefunc();
    //counter = do_Str (str_buf);
    printf("%s\n",split_string(str_buf));


    // FIXME should happen if we have "q"
    ended = 1;
  }

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
  char** words = (char **)malloc(wordcount * (sizeof char*));
  char *pch = strtok (buffer, " ");
  index = 0;
  while (pch != NULL)
  {
    // Save some room.
    words + (index * (sizeof char*)) = (char *) malloc(strlen(pch) * (sizeof (char)))
    pch = strtok(NULL, " ");
  }

  return words;
}
