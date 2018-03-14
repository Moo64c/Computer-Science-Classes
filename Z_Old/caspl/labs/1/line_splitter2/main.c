#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "main.h"


int main(int argc, char **argv) {
  lineNum = 1;
  /* Get parameters */    
  int i = 0;
	flagD = 0;
	flagS = 0;
	flagO = 0;
	currentOutput=-1;
  flag = '.';
  input = stdin;
	output = stdout;

	char filename[255];
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
    else if(flagO == 0 && strcmp(argv[i],"-o")==0) {
      flagO = atoi(argv[i+1]);
      i++;
			int k;
			for (k=0; k<flagO; k++) {
				
				printf("Enter file name for file %i:", k+1);
				scanf("%s", filename);
				files[k] = fopen(filename, "w");
			}
    }
    else {
	printf("invalid parameter - %s\n",argv[i]);
	return 1;
    }
  }
  
  /* Get input from user. */
  char *line = read();
	printLine();
  workLine(line);
  
  fprintf(stdout, "\n");	
  return 0;
}

/**
 * Gets a single line input from the user.
 */
char * read() {
    char * line = malloc(100);
		char * linep = line;
    size_t lenmax = 100;
		size_t len = lenmax;
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
    return linep;
}

void workLine(char *line) {

  int i = 0;
  while(1) {
      switch(line[i]) {
	case '@':
	case '*':
	  fprintf(output, "%c", line[i]);
	  printLine();
	  break;
	case '\0':
	case EOF:
	  /* Shut down. */
	  lineNum = 1;
	  return;
	default:
	  fprintf(output, "%c", line[i]);
	  break;
      } 
      
    if (flagD == 1 && isNumber(line[i]) == 1) {
      printLine();
    }
    else if (flagS == 1 && line[i] == flag) {
      printLine();
    } 
    i++;
   }
   
  lineNum = 1;
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
	if(flagO > 0) {
	 currentOutput++;
		if (currentOutput >= flagO) {
			currentOutput = 0;
		}
	output = files[currentOutput];
  fprintf(output, "\n%i:", lineNum++);
	}
}
