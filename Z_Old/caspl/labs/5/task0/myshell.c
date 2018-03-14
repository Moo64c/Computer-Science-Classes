#include <stdio.h>
#include <string.h>
#include <limits.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include "LineParser.h"

void execute(cmdLine *pCmdLine);
char * getFromUser();

FILE * input;
FILE * output;

int main (int argc , char* argv[], char* envp[]) {
	char * line;
	input = stdin;
	output = stdout;

	while( 1 ) {
		/* Get new command. */
		line = getFromUser();
		/* Parse & execute command */
        cmdLine * pCmdLine = parseCmdLines(line);
        if (strcmp(pCmdLine->arguments[0],"quit") == 0) {
            break;
        }
		execute(pCmdLine);
        freeCmdLines(pCmdLine);
    }

  return 0;
}


void execute(cmdLine *pCmdLine)  {
    if (pCmdLine == NULL) {
        return;
    }
    int i;
    printf("\nargs:");
    for (i=0; i< pCmdLine->argCount; i++) {
        printf("\n%s", pCmdLine->arguments[i]);
    }

    int result = execvp(pCmdLine->arguments[0], pCmdLine->arguments);
    printf("\n\n RESULT: \n %d \n\n ", result);
    if (result == -1) {
        perror("\nExecution failed");
        return;
    }
    execute(pCmdLine->next);
}


/**
 * Gets a single line input from the user.
 */
char * getFromUser() {
    char * line = malloc(sizeof(char) * 100);
        char * linep = line;
    size_t lenmax = 100;
        size_t len = lenmax;
    int c;

    if(line == NULL)
        return NULL;

    for(;;) {
        c = fgetc(input);
        if(c == EOF || c == '\n')
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