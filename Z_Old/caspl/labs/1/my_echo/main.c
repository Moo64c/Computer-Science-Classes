#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv) {
  /* Get the in-line text. */
   int i;
  for(i=1; i<argc; i++){
      printf("%s",argv[i]);
  }
  
  /* New line */
  printf("\n");
  /* Shut down. */
  return 0;
}
