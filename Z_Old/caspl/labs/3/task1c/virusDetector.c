#include <stdio.h>
#include <stdlib.h>

typedef struct virus virus;
 
struct virus {
        int length;
        unsigned char *signature;
        char *name;
        virus *next;
};

int getLength(unsigned char * a, int offset);
void PrintHex(unsigned char * buffer,int length);
virus * getVirusesInfo(unsigned char * buffer,int length, virus * v_list);
virus * getVirusInfo(unsigned char * buffer,int * offset, virus * v_list);
void list_print(virus *virus_list); 
void list_free(virus *virus_list); 
virus *list_append(virus *virus_list, virus *data); 
void print_virus(virus *virus);
void delete_virus(virus *virus);
virus * createVirus(char * name, unsigned char * signature, int length);
void detect_virus(unsigned char *buffer, virus *virus_list, unsigned int size);

struct virus empty = {0};

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
	 
	 
	 virus* v_list = &empty;
	 v_list = getVirusesInfo(filecontent, fsize, v_list);
	
	 detect_virus(filecontent, v_list, fsize);
	 fclose(rm);
	 list_free(v_list);
	 free(filecontent);
	}
  else {
		printf("File Not Found.\r\n");
	}
	return 0;
}


virus * getVirusesInfo(unsigned char * buffer,int length, virus * v_list) {
	int j =0;
	int * i = &j;
	/* 4 bytes = X = length of the signature; signature size X, then string. */
	for ((*i) = 0; (*i) < length; (*i)++) { 
		v_list = getVirusInfo(buffer, i, v_list); 
	}
	list_print(v_list);
	printf("\n");
	return v_list;
}

virus * getVirusInfo(unsigned char * buffer,int * offset, virus * v_list) {
	int i;
	char c;
	int length = getLength(buffer, (*offset));
	unsigned char * signature = (unsigned char*) malloc(sizeof(unsigned char)*length);
	char * name = (char*) malloc(sizeof(char)*102);

	/* Read four bytes. */
	(*offset)+=4;

	/* Save the data. */
	for (i = 0; i < length; i++) {
	    (*(signature+i)) = (*(buffer+i+(*offset)));
	}
	
	/* Read the signature; get the name. */
	(*offset) += length;
	c = (*(buffer + ((*offset))));

	for(i = 0; i <= 100 && c != '\0'; i++) {
		(*(name + i)) = c;
		(*(name + i+1)) = '\0';
		c = (*(buffer + (++(*offset))));
	}

	virus * v = createVirus(name, signature, length);
	v_list = list_append(v_list, v);
	return v_list;
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

/* Print the data of every link in list. Each item followed by a newline character. */
void list_print(virus *virus_list) {
	print_virus(virus_list);
}

/* Add a new link with the given data to the list 
	(either at the end or the beginning, whichever you think is more convenient),
	and return a pointer to the list (i.e., the first link in the list).
	If the list is null - create a new entry and return a pointer to the entry. */
virus *list_append(virus *virus_list, virus *data) {
	data->next = virus_list;
	return data;
}

void list_free(virus *virus_list) {
	virus * v = virus_list;
	if(v != &empty){
		list_free(virus_list->next);
		delete_virus(v);
	}
}

void print_virus(virus *v) {
	/* print it out */
	if (v->next != &empty) {
		print_virus(v->next);
	}
	
	printf("Virus name: %s \n", v->name);	
	printf("Virus size: %i \n", v->length );
	printf("signature:\n");
  PrintHex(v->signature, v->length );
	printf("\n");
}

void delete_virus(virus *v) {
	/* print it out */
	free(v->signature);
	free(v->name);
	free(v);
}


virus * createVirus(char * name, unsigned char * signature, int length) {
	virus * v = (virus * ) malloc(sizeof(struct virus));
	v->name = name;
	v->length = length;
	v->signature = signature;
	v->next = &empty;
	return v;
}

void detect_virus(unsigned char *buffer, virus *virus_list, unsigned int size) {
	int i,j;
	int is_virus=0;
	virus * v_run;
	
	for(i =0; i < size; i++) {
		/* check buffer for viruses. */
		v_run = virus_list;
		while(v_run != &empty) {
			if ((*(v_run->signature)) == (*(buffer+i))) {
				is_virus=1;
				for(j=0; is_virus==1&&j<v_run->length;j++) {
					if ((*(v_run->signature+j)) != (*(buffer+j+i)))
						is_virus =0;
				}
				
				if (is_virus) {
					printf("Found a match at %i\n", i);
					printf("To virus %s\n", v_run->name);
					printf("Virus size: %i\n\n", v_run->length);
				}
			}
			v_run = v_run->next;
		}		
	}
	
}