#include <stdio.h>
#include <stdlib.h>

int getLength(unsigned char * a, int offset);
void PrintHex(unsigned char * buffer,int length);
void getVirusesInfo(unsigned char * buffer,int length);
void getVirusInfo(unsigned char * buffer,int * offset);


int main(int argc, char **argv) {
  FILE *rm;
	long fsize;
	unsigned char *filecontent;
	
/*	char * filename = argv[1]; 
	rm = fopen(filename, "r"); */

  rm = fopen("signatures", "r"); 

  if (rm != NULL) {
	 fseek(rm, 0, SEEK_END);	
	 fsize = ftell(rm);
	 fseek(rm, 0, SEEK_SET);

	 filecontent = (unsigned char*) malloc(fsize + 1);
	 fread(filecontent, fsize, 1, rm);
		
	 getVirusesInfo(filecontent, fsize);

	 fclose(rm);
	 free(filecontent);
	}
  else {
		printf("File Not Found.\r\n");
	}
	return 0;
}


void getVirusesInfo(unsigned char * buffer,int length) {
	int j =0;
	int * i = &j;
	/* 4 bytes = X = length of the signature; signature size X, then string. */
	for ((*i) = 0; (*i) < length; (*i)++) { 
		getVirusInfo(buffer, i); 
	}
	printf("\n");
}

void getVirusInfo(unsigned char * buffer,int * offset) {
	int i;
	char c;
	int length = getLength(buffer, (*offset));
	unsigned char signature[length];
	char * name = (char*) malloc(sizeof(char)*102);

	/* Read four bytes. */
	(*offset)+=4;

	/* Save the data. */
	for (i = 0; i < length; i++) {
	    signature[i] = (*(buffer+i+(*offset)));
	}
	
	/* Read the signature; get the name. */
	(*offset) += length;
	c = (*(buffer + ((*offset))));

	for(i = 0; i <= 100 && c != '\0'; i++) {
		(*(name + i)) = c;
		(*(name + i+1)) = '\0';
		c = (*(buffer + (++(*offset))));
	}

	/* print it out */
	printf("Virus name: %s \n", name);	
	printf("Virus size: %i \n", length);
	printf("signature:\n");
	PrintHex(signature, length);
	printf("\n");
	free(name);
}

int getLength(unsigned char * a, int offset) {
	return (a[offset + 3] << 24) | (a[offset + 2] << 16) | (a[offset + 1] << 8) | a[offset];
}

void PrintHex(unsigned char * buffer, int length) {
	int i;
	for (i = 0; i < length; ++i) {
		printf("%02X ", (*(buffer+i)));		
	}
	printf("\n");
}

