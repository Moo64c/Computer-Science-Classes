#include "util.h"

#define STDIN	 0
#define STDOUT 1

/*System function numbers. */
#define SYS_EXIT  1 
#define SYS_READ  3
#define SYS_WRITE 4
#define SYS_OPEN  5
#define SYS_LSEEK 19

void writeLine(char * line, int length);
void writeOut(char * str, int length);
extern int system_call();
int gLineNumber=0;

int main (int argc , char* argv[], char* envp[]) {
	int length = 0, index, startPos=0;
	char buf[1000];

	system_call(SYS_READ, STDIN, buf, sizeof(buf));

	for (index = 0; index < 1000; index++) {
		if (buf[index] == '\0') {
			/* Cancel order exit */;
			break;
		}
		
		length++;
		if (buf[index] == '@' || buf[index] == '!') {
			writeLine(buf+startPos, length);
			startPos=index+1;
			length=0;
		}
	}	

	writeLine(buf+startPos, length);
	writeOut("\n", 1);
  return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE, STDOUT, str, length);
}

void writeLine(char * line, int length) {
	length = (length == 0) ? strlen(line) : length; 
	writeOut("\n", 1);
	char * num = itoa(gLineNumber);
	writeOut(num, strlen(num));
	writeOut(":", 1);
	writeOut(line, length);
	gLineNumber++;
}
