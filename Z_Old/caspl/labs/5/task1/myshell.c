#include <stdio.h>
#include <string.h>
#include <limits.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h> 

#include "LineParser.h"

void execute(cmdLine *pCmdLine);
char * getFromUser();

void add_history(cmdLine * pCmdLine);
void destroy_history();
void execute_history(int index);
void print_history();
char *removeFirstChar (char *charBuffer);

typedef struct history_record history_record;

struct history_record {
        cmdLine * cmd;
        history_record * next;
};

history_record * history;

FILE * input;
FILE * output;
char * dir;
char * buff; 

int main (int argc , char* argv[], char* envp[]) {
	char * line;
	input = stdin;
	output = stdout;
    buff = (char *) malloc((size_t) sizeof(char) *200);
    dir = getcwd(buff, 100);

	while( 1 ) {
    	/* Get new command. */
    	line = getFromUser();
        /* Parse & execute command */
        cmdLine * pCmdLine = parseCmdLines(line);
        if (line) free(line);
        execute(pCmdLine);  
    }
    free(buff);
    destroy_history();

  return 0;
}


void execute(cmdLine *pCmdLine)  {
    pid_t pid;
    int status;

    if (pCmdLine == NULL) {
        return;
    }

    if (strcmp(pCmdLine->arguments[0], "quit") == 0) {
        freeCmdLines(pCmdLine);
        destroy_history();
        exit(0);
    }
    else if (strcmp(pCmdLine->arguments[0], "cd") == 0) {
        int result = chdir(pCmdLine->arguments[1]);
        if (result < 0) {
            perror("Failed changing directory");
            add_history(pCmdLine);
            return;
        }
        dir = getcwd(buff, 100);
    }
    else if (strcmp(pCmdLine->arguments[0], "history") == 0) {
        print_history();
        add_history(pCmdLine);  
    }
    else if (pCmdLine->arguments[0][0] == '!') {
        /* Execute a command from history */
        char * index = removeFirstChar(pCmdLine->arguments[0]);
        execute_history(atoi(index));
    }
    else {
        add_history(pCmdLine); 
        if ((pid = fork()) < 0) {     /* fork a child process           */
            printf("*** ERROR: forking child process failed\n");
            destroy_history();
            exit(1);
        }
        else if (pid == 0) {          /* for the child process:         */
            if (execvp(pCmdLine->arguments[0], pCmdLine->arguments) < 0) {     /* execute the command  */
                perror("\nExecution failed");
                _exit(1);
            }
        }     
        else {
            if(pCmdLine->blocking == 1) {
              /* Wait for blocking executions to end. */
              waitpid(pid, &status, 0); 
            }
        }
    }
}



void add_history(cmdLine * pCmdLine) {
    history_record * record = (history_record * ) malloc(sizeof(struct history_record));
    record->next = history;
    history = record;
    record->cmd = pCmdLine;
}

void execute_history(int index) {
    int p=0;
    history_record * line = history;
    while (line != NULL) {
        if (p == index) {
            execute(line->cmd);
            return;
        }
        p++;
        line = line->next;
    }
    perror("Saved history isn't that long\n");
}

void print_history() {
    int i;
    history_record * line = history;
    printf("---------\n");
    while (line != NULL) {
        for (i=0; i < line->cmd->argCount; i++) {
            printf("%s ",line->cmd->arguments[i]);
        }
        printf("\n");
        line = line->next;
    }
    printf("--------\n");
}

void destroy_history() {
    history_record * line = history;
    history_record * temp;
    while (line != NULL) {
        freeCmdLines(line->cmd);
        temp = line;
        line = line->next;
        free(temp);
    }

    free(dir);
}

/**
 * Gets a single line input from the user.
 */
char * getFromUser() {
    printf("\n%s $:", dir);
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

char *removeFirstChar (char *charBuffer) {
  char *str;
  if (strlen(charBuffer) == 0)
    str = charBuffer;
  else
    str = charBuffer + 1;
  return str;
}