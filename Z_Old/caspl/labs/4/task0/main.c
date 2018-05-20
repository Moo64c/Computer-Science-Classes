#include "util.h"

#define SYS_WRITE 4
#define STDOUT 1

void writeOut(char * str, int length);
extern int system_call();

int main (int argc , char* argv[], char* envp[]) {
	writeOut("Hello World\n", 13);
  int i;
  char * str;
  str = itoa(argc);
  writeOut(str,strlen(str));
  writeOut(" arguments \n", 12);

  for (i = 0 ; i < argc ; i++) {
		writeOut("argv[", 5);
		str = itoa(i);
		writeOut(str,strlen(str));
		writeOut("] = ", 4);
		writeOut(argv[i], strlen(argv[i]));
		writeOut("\n", 1);
  }

  return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE,STDOUT, str, length);
}
