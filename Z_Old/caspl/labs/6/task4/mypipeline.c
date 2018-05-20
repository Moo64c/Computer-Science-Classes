#include "stdio.h"
#include "stdlib.h"
#include "unistd.h"
#include "fcntl.h"
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv)
{
 int pipefd[2];
 int childpid,childpid2;
 char* cmd[3]={"ls","-l", NULL};
 char* cmd2[3]={"tail","--lines=2", NULL};
 pipe(pipefd);

 childpid = fork();
 if(childpid < 0) {
 	printf("Fork failed\n");
 	_exit(0);
 }
 else if (childpid == 0){  
   close(pipefd[0]);
   dup2(pipefd[1],STDOUT_FILENO);
   execvp(cmd[0], cmd);
 }
 childpid2=fork();
 if(childpid2 < 0) {
 	printf("Fork failed\n");
 	_exit(0);
 }
 else if (childpid2 == 0) {
   close(pipefd[1]);
   dup2(pipefd[0],STDIN_FILENO);
   execvp(cmd2[0],cmd2);
 }
 close(pipefd[0]);
 close(pipefd[1]);

 wait(NULL); 
 wait(NULL);
 return 0;
}