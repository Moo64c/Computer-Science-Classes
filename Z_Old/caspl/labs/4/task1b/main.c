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
int flagI=0;
int flagO=0;
int input = STDIN;	
int output = STDOUT;	
int files[5];	

int main (int argc , char* argv[], char* envp[]) {
	int length = 0, index, startPos=0, i, k;
	char buf[2500];

  for(i=1; i<argc; i++){
    if(strcmp(argv[i],"-i")==0) {
      input= system_call(SYS_OPEN, argv[i+1], 2, 0777);
      i++;
    }
    else if(flagO == 0 && strcmp(argv[i],"-o")==0) {
	    flagO = positive_atoi(argv[i+1]);
	    i++;
			for (k=0; k<flagO; k++) {
				writeOut("Enter output file ", 19);
				char * c = itoa(k+1);
				writeOut(c, strlen(c));
				writeOut(":\n", 2);
				system_call(SYS_READ, STDIN, buf, 2500);
				files[k] =  system_call(SYS_OPEN, buf, 64 | 2, 0644);
			}
    }
    else {
			writeOut("invalid parameter", 14);
			return 1;
    }
  }

	system_call(SYS_READ, input, buf, sizeof(buf));

	for (index = 0; index < 2500; index++) {
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
	output = STDOUT;
	writeOut("\n", 1);
  return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE, output, str, length);
}

void writeLine(char * line, int length) {
	if (flagO != 0) {
		output = files[gLineNumber % flagO];
	}

	length = (length == 0) ? strlen(line) : length; 
	if (gLineNumber >= flagO) {
		writeOut("\n", 1);
	}

	char * num = itoa(gLineNumber);
	writeOut(num, strlen(num));
	writeOut(":", 1);
	writeOut(line, length);
	gLineNumber++;
}
