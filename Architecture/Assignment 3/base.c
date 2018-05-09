#include <stdio.h>
#include <stdlib.h>

#define MAX_PHYSICAL_MEMORY 59

static long MACHINE_MEMORY[MAX_PHYSICAL_MEMORY] = {
  56, 54, 36, 55, 55, 6, 55, 52, 9, 55,
  53, 12, 52, 52, 15, 51, 51, 18, 51, 53,
  21, 52, 51, 24, 53, 53, 27, 53, 55, 30,
  55, 55, 33, 55, 54, 0, 0, 0, 39, 51,
  51, 42, 51, 52, 45, 0, 51, 48, 0, 0,
  0, 0, 0, 1, 1, 0, 30
};

int main() {

  int index = 0;
  int printIndex = 0;
  long sbn_result = 0;
  long A = MACHINE_MEMORY[index];
  long B = MACHINE_MEMORY[index + 1];
  long C = MACHINE_MEMORY[index + 2];

  while (A || B || C) {

    sbn_result = (MACHINE_MEMORY[A] -= MACHINE_MEMORY[B]);
    if (sbn_result < 0) {
      // Jump to C.
      index = C;
    }
    else {
      index += 3;
    }

    A = MACHINE_MEMORY[index];
    B = MACHINE_MEMORY[index + 1];
    C = MACHINE_MEMORY[index + 2];
  }

  for (printIndex = 0; printIndex < MAX_PHYSICAL_MEMORY; ++printIndex) {
    printf("%ld ", MACHINE_MEMORY[printIndex]);
  }

  printf("\n");
  return 0;
}
