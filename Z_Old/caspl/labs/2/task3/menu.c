#include <stdio.h>
#include <stdlib.h>

struct fun_desc {
  char *name;
  char (*fun)(char);
};

char censor(char c) {
  if(c == '!')
    return '.';
  else if(c == 0)
    return 0;
  
  return c;
} 
/**
 * Gets a character c, and returns it in lower case. 
 * Characters that do not have a lower case, are returned unchanged 
 */
char to_lower(char c) {
    return c = (c>'A'&& c<'Z') ? c|0x60 : c;
}

/**
 * Prints the value of c, followed by a new line, and returns c unchanged 
 */
char cprt(char c) {
  printf("%c\n", c);
  return c;
}

/**
 * Ignores c, reads and returns a character from stdin using fgetc. 
 * Returns 0 when a new line character is read. 
 */
char my_get(char c) {
  char p = fgetc(stdin);
  return p == '\n' ? 0 : p;
}

/**
 * Gets a character c, ignores it, and ends the program.
 */
char quit(char c) {
  exit(0);
  return c;
}

void for_each(char *array, char (*f) (char)){
  int i = 0;
  char p = array[i];
  while (p!=0) {
    array[i] = (f)(array[i]);
    p = array[i];
    i++;
  }
}

struct fun_desc menu[] = { 
  { "To lower case", to_lower },
  { "Censor", censor},
  {"Print", cprt}, 
  {"Get string", my_get}, 
  {"Quit", quit},
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
 
int main(int argc, char **argv){
  char carray[100] = "HELLO!0";
  char * array = carray;
  char sel = 'n';
  int selN = 0;
  while(1) {
    print_menu();
    sel = fgetc(stdin);
    if (sel < '0' || sel > '4') 
      exit(0);
    selN =  sel - '0';
    printf("\n%i\n", selN);
    for_each(array, menu[selN].fun);   
  }
  
  return 0;
}