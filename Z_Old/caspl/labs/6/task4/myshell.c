#define _POSIX_C_SOURCE 1
#include <string.h>
#include <limits.h>
#include <stdlib.h>
#include <ctype.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h> 
#include "LineParser.h"

typedef struct env_var env_var;

struct env_var {
        char * name;
        char * value;
        env_var * next;
};

typedef struct history_record history_record;

struct history_record {
        cmdLine * cmd;
        history_record * next;
};

void execute(cmdLine *pCmdLine);
void execute_with_pipes(cmdLine *pCmdLine);
char * getFromUser();

void add_history(cmdLine * pCmdLine);
void destroy_history();
void execute_history(int index);
void print_history();
char *removeFirstChar (char *charBuffer);
void _del_history(history_record * record);

cmdLine * apply_vars(cmdLine * pCmdLine);
void set_var(char * name, char * value);
void rename_var(char * name, char * new_name);
void del_var(char * name);
void print_vars();
void delete_vars();
void _del_var(env_var * var);
void _delete_and_exit(int code);

int **createPipes(int nPipes);
void releasePipes(int **pipes, int nPipes);
int *leftPipe(int **pipes, cmdLine *pCmdLine);
int *rightPipe(int **pipes, cmdLine *pCmdLine);
int num_of_pipes(cmdLine *pCmdLine);

int _internal_commands(cmdLine *pCmdLine);

history_record * history;
env_var * env_vars;
char * delim;

FILE * input;
FILE * output;
char * dir;
char * buff; 
int save_in;
int save_out;

int main (int argc , char* argv[], char* envp[]) {
	char * line;
	input = stdin;
	output = stdout;
  buff = (char *) malloc((size_t) sizeof(char) *200);
  dir = getcwd(buff, 200);
  env_vars = NULL;
  delim = " ";

	while( 1 ) {
        /* Get new command. */
        line = getFromUser();
        /* Parse & execute command */
        cmdLine * pCmdLine = parseCmdLines(line);
        if (line) free(line);
        execute(pCmdLine);  
    }
    _delete_and_exit(0);

  return 0;
}

void execute(cmdLine *pCmdLine) {
  pid_t pid;
  int status;

  if (pCmdLine == NULL) {
      return;
  }
  
  if (pCmdLine->next != NULL) {
  	return execute_with_pipes(pCmdLine);
  }
	
  if (env_vars != NULL) {
      pCmdLine = apply_vars(pCmdLine); 
  }
	
	if ((pid = fork()) < 0) {     /* fork a child process  */
	    printf("*** ERROR: forking child process failed\n");
	    _delete_and_exit(1);
	}
	else if (pid == 0) {        /* for the child process:         */
		if (pCmdLine->inputRedirect != NULL) {
			fclose(stdin);
			fopen(pCmdLine->inputRedirect, "r");
		}

		if (pCmdLine->outputRedirect != NULL) {
			fclose(stdout);
			fopen(pCmdLine->outputRedirect, "w");
		}

		if (_internal_commands(pCmdLine)	!= 1) {
	    add_history(pCmdLine); 
      if (execvp(pCmdLine->arguments[0], pCmdLine->arguments) < 0) {     /* execute the command  */
        perror("\nExecution failed");
        _exit(1);
      }
    }
  }
  else {
		if(pCmdLine->blocking == 1) {
			/* Wait for blocking executions to end. */
			waitpid(pid, &status, 0); 
		}
  }
}


void set_var(char * name, char * value) {
    /* loop through the vars to see if it exists */
    env_var * it = env_vars;
    env_var * b_it = env_vars;

    char * cname = (char *) malloc(sizeof(char) * 2048);
    char * cvalue = (char *) malloc(sizeof(char) * 2048);
    strcpy(cname, name);
    strcpy(cvalue, value);

    if (env_vars == NULL) {
        env_var * new_var = (env_var * ) malloc(sizeof(struct env_var));
        new_var->name = cname;
        new_var->value = cvalue;  
        env_vars = new_var;
        new_var->next = NULL;
        return; 
    }

    while (it != NULL) {
        if (strcmp(it->name, name) == 0) {
            free(it->value);
            it->value = cvalue;
            return;
        }
        b_it = it;
        it = it->next;
    }

    /* didn't find the variable named \name\
        create a new one. */
    env_var * new_var = (env_var * ) malloc(sizeof(struct env_var));
    b_it->next = new_var;
    new_var->name = cname;
    new_var->value = cvalue;   
    new_var->next = NULL;
}

void rename_var(char * name, char * new_name) {
    /* loop through the vars to see if it exists */
    env_var * it = env_vars;
    while (it != NULL) {
        if (strcmp(it->name, name) == 0) {
            free(it->name);
            it->name = new_name;
            return;
        }
        it = it->next;
    }
    perror("Var not found");
}

void del_var(char * name) {
    /* loop through the vars to see if it exists */

    if (env_vars == NULL) {
        perror("Var not found");
        return;
    }

    env_var * b_it  = env_vars;
    env_var * it = env_vars;
    if (strcmp(env_vars->name, name)  == 0) {
        env_vars = env_vars->next;
        _del_var(it);
        return;
    }

    it = it->next;
    while (it != NULL) {
        if (strcmp(it->name, name) == 0) {
            b_it->next = it->next;
            _del_var(it);
            return;
        }
        b_it = it;
        it = it->next;
    }
    perror("Var not found");
}

void print_vars() {
    if (env_vars == NULL) {
        printf("No vars are set.: \n");
        return;
    }

    /* loop through the vars to see if it exists */
    env_var * it = env_vars;
    printf("List of vars: \n");
     while (it != NULL) {
        printf("%s : %s \n", it->name, it->value);
        it = it->next;
    }
}
/* delete all vars without regard to order */
void delete_vars() {
    if (env_vars == NULL) {
        return;
    }
    env_var * b_it;
    env_var * it = env_vars;
    while (it != NULL) {
        b_it = it;
        it = it->next;
        _del_var(b_it);
    }
}

void _del_var(env_var * var){
    free(var->name);
    free(var->value);
    free(var);
}

cmdLine * apply_vars(cmdLine * pCmdLine) {
    int i;
    env_var * it = NULL;
    for(i = 0; i < pCmdLine->argCount; i++) {
        if (pCmdLine->arguments[i][0] == '$') {
            char * index = removeFirstChar(pCmdLine->arguments[i]);
            it = env_vars;
            while (it != NULL) {
                if (strcmp(it->name, index) == 0) {
                    replaceCmdArg(pCmdLine, i, it->value);
                }
                it = it->next;
            }
        }
    }

    /* rebuild args */

    char new_line[2048];
    strcpy(new_line, pCmdLine->arguments[0]);
    for (i=1; i< pCmdLine->argCount; i++) {
        strcat(new_line, delim);
        strcat(new_line, pCmdLine->arguments[i]);
    }

    printf("%s\n",new_line);
    cmdLine * l = parseCmdLines(new_line);
    freeCmdLines(pCmdLine);
    return l;
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
    printf("---------\n");
}



void destroy_history() {
    history_record * line = history;
    history_record * temp;
    while (line != NULL) {
        temp = line;
        line = line->next;
        _del_history(temp);
    }
}

void _del_history(history_record * record) {
    freeCmdLines(record->cmd);
    free(record);
}

/**
 * Gets a single line input from the user.
 */
char * getFromUser() {
    printf("\n%s $:", dir);
    char * line = malloc(sizeof(char) * 2049);
    memset(line, '\0', sizeof(char) * 2049);
    char c;
    int len;
    for(len = 0; len < 2047; len++) {
        c = fgetc(input);
        if(c == EOF || c == '\n') {
            break;
        }
        line[len] = c;
    }
    
    return line;
}

char *removeFirstChar (char * charBuffer) {
  char *str;
  if (strlen(charBuffer) == 0)
    str = charBuffer;
  else
    str = charBuffer + 1;
  return str;
}


void _delete_and_exit(int code) {
    if (buff) free(buff);
    destroy_history();
    delete_vars();
    exit(code);
}


void execute_with_pipes(cmdLine *pCmdLine) {
  pid_t pid;
  int result = 0, pipeNumber = 0;
  int *clPipe = NULL, *crPipe = NULL;
  int **pipes;
  
  pipeNumber = num_of_pipes(pCmdLine);
  pipes = createPipes(pipeNumber);
  
  while (pCmdLine != NULL)  {
    clPipe = leftPipe(pipes, pCmdLine);
    crPipe = rightPipe(pipes, pCmdLine);
    
		if((pid = fork()) == -1) {
			_exit(11);
			return;
		}
		else if (pid == 0) {
		/* child, redirect output/input*/
			if (crPipe != 0) {
				fclose(stdout);
				result = dup(crPipe[1]);
				close(crPipe[1]);
			}
			else if (pCmdLine->outputRedirect != 0) {
				fclose(stdout);
				fopen(pCmdLine->outputRedirect, "w");
			}
			if (clPipe != 0)	{
				fclose(stdin);
				result = dup(clPipe[0]);
				close(clPipe[0]);
			}
			else if (pCmdLine->inputRedirect != 0) {
				fclose(stdin);
				fopen(pCmdLine->inputRedirect, "r");
			}
			result = execvp(pCmdLine->arguments[0], pCmdLine->arguments);
			perror("Process error ");
			_exit(1); /*exiting since this process is useless now.*/
			return;
		}
		else {
			if (clPipe != 0) {
				close(clPipe[0]);
			}
			if (crPipe != 0) {
				close(crPipe[1]);
			}
			if (pCmdLine->blocking == 1) {
				waitpid(pid, &result, 0);
			}
		
			pCmdLine = pCmdLine->next;
    }
  }
  
  releasePipes(pipes, pipeNumber);
  
  return;
}

int **createPipes(int num) {
  int **pipes;
  int index;
  pipes = (int **) malloc(num*sizeof(int*));
  for (index = 0; index < num; index++) {
    pipes[index] = malloc(sizeof(int)*2);
    pipe(pipes[index]);
  }
  return pipes;
}

void releasePipes(int **pipes, int num) {
  int i;
  for (i = 0; i < num; i++) {
    close(pipes[i][0]);
    close(pipes[i][1]);
    free(pipes[i]);
  }
  free(pipes);
}

int *leftPipe(int **pipes, cmdLine *pCmdLine) {
  if (pCmdLine->idx == 0) {
    return NULL;
  }
  
  return pipes[pCmdLine->idx - 1];
}

int *rightPipe(int **pipes, cmdLine *pCmdLine) {
  if (pCmdLine->next == 0) {
    return NULL;
  }
  return pipes[pCmdLine->idx];
}

int _internal_commands(cmdLine *pCmdLine) {
	int result = 0;
	
	if (strcmp(pCmdLine->arguments[0], "quit") == 0) {
		result = 1;
    add_history(pCmdLine);
    _delete_and_exit(0);
	}
	else if (strcmp(pCmdLine->arguments[0], "cd") == 0) {
			result = 1;
      int applied = chdir(pCmdLine->arguments[1]);
      add_history(pCmdLine);
      if (applied < 0) {
          perror("Failed changing directory");
          return 1;	
      }
      dir = getcwd(buff, 100);
  }
  else if (strcmp(pCmdLine->arguments[0], "history") == 0) {
    	result = 1;
      add_history(pCmdLine);  
      print_history();
  }
  else if (strcmp(pCmdLine->arguments[0], "set") == 0) {
    	result = 1;
      add_history(pCmdLine);  
      int i;
      char value[2048];
      strcpy(value, pCmdLine->arguments[2]);
      for (i=3; i < pCmdLine->argCount; i++) {
          strcat(value, delim);
          strcat(value, pCmdLine->arguments[i]);
      }
      set_var(pCmdLine->arguments[1], value);
  }
  else if (strcmp(pCmdLine->arguments[0], "env") == 0) {
	  	result = 1;
      add_history(pCmdLine);  
      print_vars();
  }
  else if (strcmp(pCmdLine->arguments[0], "rename") == 0) {
	  	result = 1;
      add_history(pCmdLine);  
      rename_var(pCmdLine->arguments[1], pCmdLine->arguments[2]);
  }
  else if (strcmp(pCmdLine->arguments[0], "delete") == 0) {
	  	result = 1;
      add_history(pCmdLine);  
      del_var(pCmdLine->arguments[1]);
  }
  else if (pCmdLine->arguments[0][0] == '!') {
  	result = 1;
      /* Execute a command from history */
      char * index = removeFirstChar(pCmdLine->arguments[0]);
      execute_history(atoi(index));
  }
  return result;
}



int num_of_pipes(cmdLine *pCmdLine) {
  int i = 0;
  while (pCmdLine->next != 0) {
    i++;
    pCmdLine = pCmdLine->next;
  }
  return i;
}

