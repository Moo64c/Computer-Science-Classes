#include <stdio.h>
#include <stdlib.h>

#define DEBUG 1
#define MAX_PHYSICAL_MEMORY 200

static long MACHINE_MEMORY[MAX_PHYSICAL_MEMORY] = {
  102, 106, 3, 103, 102, 6, 102, 101, 9, 104, 102, 12, 102, 101, 15, 105, 102, 18, 103, 101, 27, 103, 100, 57, 100, 99, 57, 103, 100, 57, 104, 101, 39,
  104, 100, 57, 100, 99, 57, 104, 100, 57, 105, 101, 51, 105, 100, 57, 100, 99, 57, 105, 100, 57, 100, 99, 96, 103, 104, 63, 102, 101, 69, 102, 102, 66,
  102, 105, 69, 103, 103, 72, 103, 102, 75, 102, 101, 78, 104, 104, 81, 104, 102, 84, 102, 101, 87, 105, 105, 90, 105, 102, 93, 100, 99, 18, 0, 0, 0,
  0, -1, 1, 0, 0, 0, 0, 106,
  1,1,3, 0,0,0
  // 56, 54, 36, 55, 55, 6, 55, 52, 9, 55,
  // 53, 12, 52, 52, 15, 51, 51, 18, 51, 53,
  // 21, 52, 51, 24, 53, 53, 27, 53, 55, 30,
  // 55, 55, 33, 55, 54, 0, 0, 0, 39, 51,
  // 51, 42, 51, 52, 45, 0, 51, 48, 0, 0,
  // 0, 0, 0, 1, 1, 0, 30
};

int main() {

  int index = 0;
  int printIndex = 0;
  long sbn_result = 0;
  long A = MACHINE_MEMORY[index];
  long B = MACHINE_MEMORY[index + 1];
  long C = MACHINE_MEMORY[index + 2];
int runs = 0;
  if (DEBUG) {
    printf("%d: sbn %ld=%ld, %ld=%ld, %ld=%ld\n",index, A,MACHINE_MEMORY[A],B,MACHINE_MEMORY[B],C,MACHINE_MEMORY[C]);
  }


  while (A || B || C) {


    sbn_result = (MACHINE_MEMORY[A] -= MACHINE_MEMORY[B]);
    if (sbn_result < 0) {
      // Jump to C.
      index = C;
    }
    else {
      index += 3;
    }

    // for (printIndex = 0; printIndex < MAX_PHYSICAL_MEMORY; ++printIndex) {
    //   printf("%ld ", MACHINE_MEMORY[printIndex]);
    // }

    printf("\n");

    A = MACHINE_MEMORY[index];
    B = MACHINE_MEMORY[index + 1];
    C = MACHINE_MEMORY[index + 2];

    if (DEBUG) {
      printf("%d: sbn %ld=%ld, %ld=%ld, %ld=%ld\n",index, A,MACHINE_MEMORY[A],B,MACHINE_MEMORY[B],C,MACHINE_MEMORY[C]);
    }
    runs++;
    if (runs > 1000){
      break;
      printf("too many runs\n");
    }
  }

  for (printIndex = 0; printIndex < MAX_PHYSICAL_MEMORY; ++printIndex) {
    printf("%ld ", MACHINE_MEMORY[printIndex]);
  }

  printf("\n");
  return 0;
}
