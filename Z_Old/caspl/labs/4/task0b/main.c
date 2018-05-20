#include "util.h"

#define STDOUT 1

/*System function numbers. */
#define SYS_EXIT 1 
#define SYS_READ 3
#define SYS_WRITE 4
#define SYS_OPEN 5
#define SYS_LSEEK 19
extern int system_call();
void writeOut(char * str, int length);

int main (int argc , char* argv[], char* envp[]) {
	int i =	0;
	char * filepath = argv[1];
	char buffer[2500] = " ";
	char * name = argv[2];
	int file = system_call(SYS_OPEN, filepath, 2, 0777);
	int result = system_call(SYS_READ, file, buffer, 2500);
	
	if (result == 0) {
		system_call(SYS_EXIT, 0x55, 0, 0);
	}
		
	for(i =0; i < 2500; i++) {
		result = -1;
		if ((*(buffer+i)) == 'S' &&
			  (*(buffer+i+1)) == 'h' &&
				(*(buffer+i+2)) == 'i' &&
			  (*(buffer+i+3)) == 'r' &&
			  (*(buffer+i+4)) == 'a') {
			writeOut("\nFound Shira at ", 16);
			writeOut(itoa(i), 4);
			writeOut("\n", 3);
			result = i;
			break;
		}
	}	
	
	if (result == -1) {
/* Not found */
		system_call(SYS_EXIT, 0x55, 0, 0);
	}
	
	system_call(SYS_LSEEK, file, result, 0);
	system_call(SYS_WRITE, file, name, strlen(name));
	
	writeOut("\n\n", 2);
  return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE, STDOUT, str, length);
}
