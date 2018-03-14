#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "main.h"


int main(int argc, char **argv) {
  lineNum = 0;
  /* Get parameters */    
  int i = 0,flagD = 0 ,flagS = 0;
  flag = '.';
  input=stdin;
  for(i=1; i<argc; i++){
    if(strcmp(argv[i],"-i")==0){
      input=fopen(argv[i+1], "r");
      i+=1;
    }
    else if(strcmp(argv[i],"-d")==0) {
      flagD = 1;
    }
    else if(flagS == 0 && strcmp(argv[i],"-s")==0) {
      flagS = 1;
      flag = *argv[i+1];
      i++;
    }
    else {
	printf("invalid parameter - %s\n",argv[i]);
	return 1;
    }
  }
  
  /* Get input from user. */
  char *line = read();
  workLine(line);
  
  fprintf(stdout, "\n");	
  return 0;
}

/**
 * Gets a single line input from the user.
 */
char * read() {
    char * line = malloc(100), * linep = line;
    size_t lenmax = 100, len = lenmax;
    int c;

    if(line == NULL)
        return NULL;

    for(;;) {
        c = fgetc(input);
        if(c == EOF)
	  break;

        if(--len == 0) {
            len = lenmax;
            char * linen = realloc(linep, lenmax *= 2);

            if(linen == NULL) {
                free(linep);
                return NULL;
            }
            line = linen + (line - linep);
            linep = linen;
        }

        *line++ = c;
    }
    
    *line = '\0';
    printLine();
    return linep;
}

void workLine(char *line) {

  int i = 0;
  while(1) {
      switch(line[i]) {
	case '@':
	case '*':
	  fprintf(stdout, "%c", line[i]);
	  printLine();
	  break;
	case '\0':
	case EOF:
	  /* New line */
	  fprintf(stdout, "\n");	
	  /* Shut down. */
	  lineNum = 0;
	  return;
	default:
	  fprintf(stdout, "%c", line[i]);
	  break;
      } 
      
    if (flagD == 1 && isNumber(line[i] == 1)) {
      printLine();
    }
    else if (flagS == 1 && line[i] == flag) {
      printLine();
    } 
    i++;
   }
   
  fprintf(stdout, "fsdfdksfhjksdahfjkn");
  lineNum = 0;
  return;
}

/**
 * Check if the character c is one of the numbers 0-9.
 */
int isNumber(char c) {
  switch (c) {
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case'9':
      return 1;
    default:
      return 0;
  }
}

void printLine() {
  fprintf(stdout, "\n%i:", lineNum++);
}