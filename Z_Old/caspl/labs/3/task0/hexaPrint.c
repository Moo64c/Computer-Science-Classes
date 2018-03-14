#include <stdio.h>
#include <stdlib.h>

void PrintHex(unsigned char * buffer,int length) {
	int i;
	for (i = 0; i < length; ++i) {
		printf("%02X ", (*(buffer+i)));		
	}
	printf("\n");
}


int main(int argc, char **argv) {
  FILE *rm;
	char * filename = argv[1];
  unsigned char buf[20];
	int res = 0;
	
  rm = fopen(filename, "r"); 
  if (rm != NULL) {
	  while (!feof(rm)) {
	      res = fread(buf, 1, (sizeof buf)-1, rm);
	      PrintHex(buf, res);
		}
		fclose(rm);
	}
  else {
      printf("File Not Found.\r\n");
	}
	
	return 0;
}

