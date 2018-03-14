#include <stdio.h>
#include <stdlib.h>

struct fun_desc {
  char *name;
  void (*fun)(char*, int, void*);
};

void quit(char *filename, int size, void *mem_buffer);
void loadIntoMemory(char *filename, int size, void *mem_buffer);
void saveIntoMemory(char *filename, int size, void *mem_buffer);
void memoryDisplay(char *filename, int size, void *mem_buffer);

struct fun_desc menu[] = { 
  { "Mem Display", memoryDisplay }, 
  { "Save Into Memory", saveIntoMemory },
  { "Load Into File", loadIntoMemory }, 
  { "Quit", quit }, 
  { NULL, NULL } 
};

void print_menu() {
  /* Print menu */
  int mItem = 0;
  printf("\nPlease choose a function:\n");
  while(1) {
    if (menu[mItem].name == NULL) 
      break;
    printf("%i: %s\n", mItem, menu[mItem].name);
    mItem++;
  }
  printf("Option: ");
}
 
 
int main(int argc, char **argv) {
	if (argc < 2) {
		printf("please specify a file to load.\n");
		exit(0);
	}
	
	/* Handle input params */
	char * filename = argv[1];	
	char * buffer;
	int bufferSize; 
	int unitSize;


	bufferSize = 4000;
	if (argc >= 2) {
		unitSize = (int) argv[2] - '0';
	}
	else {
	 unitSize = 1;
  }
	
	buffer = "";(char *) malloc(sizeof(char) * bufferSize);
	
	if (file == NULL) {
		perror("failed to open file");
		quit(filename,unitSize,buffer);
	}
	
  while(1) {
    print_menu();
    char sel = fgetc(stdin);
    if (sel < '0' || sel > '4') 
      exit(0);
    int selN =  sel - '0';
    printf("\n%i\n", selN);
    (menu[selN].fun)(filename, unitSize, buffer);
  }
  
  return 0;
}

void memoryDisplay(char *filename, int size, void *mem_buffer) {
	/* Display whatever is in the mem buffer*/
		
}

void loadIntoMemory(char *filename, int size, void *mem_buffer) {
	if (mem_buffer) free(mem_buffer);

	mem_buffer = (char *) malloc(sizeof(char) * bufferSize);
	FILE * file = fopen(filename, "r");
	if (file == NULL) {
		perror("Failed to open file");
		quit(filename, size, mem_buffer);
	}
	
	fgets(buffer, size, file);
	fclose(file);
}


void saveIntoMemory(char *filename, int size, void *mem_buffer) {};
/**
 * Ends the program.
 */
void quit(char *filename, int size, void *mem_buffer) {
	free(buffer);
  exit(0);
}
