#include "util.h"
#include <dirent.h>

#define STDIN	 0
#define STDOUT 1

/*System function numbers. */
#define SYS_EXIT  1 
#define SYS_READ  3
#define SYS_WRITE 4
#define SYS_OPEN  5
#define SYS_LSEEK 19
#define SYS_GETDENTS 141

void writeOut(char * str, int length);
extern int system_call();
 struct linux_dirent {
    long d_ino;
    long d_off;
    unsigned short d_reclen;
    char d_name[];
 } linux_dirent;

int main (int argc , char* argv[], char* envp[]) {
	writeOut("\n", 1);
	int fd, bpos;
	char * dirname = ".";
	char buffer[8192];
	struct linux_dirent * d;
  fd = system_call(SYS_OPEN, dirname, 0, 0777);	
	int count = system_call(SYS_GETDENTS, fd, buffer, 8192);
  char d_type;		
  
  writeOut("Name:\n", 6);
  writeOut("-----------------------------\n", 30);
  for (bpos = 0; bpos < count;) {
    d = (struct linux_dirent *) (buffer + bpos);
    d_type = *(buffer + bpos + d->d_reclen - 1);
    writeOut(d->d_name, strlen(d->d_name));
		writeOut("\n", 1);
    bpos += d->d_reclen;
	}
	
	writeOut("\n", 1);
	return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE, STDOUT, str, length);
}
