#include "string.h"
#include "stdio.h"

int digit_cnt(char *str)
{
  int ans = 0, i = 0;
  if (strlen(str) == 0)
    return ans;
  
  while (i < strlen(str))
  {
    if (str[i] >= 48 && str[i] <= 57)
      ans++;
    i++;
  }
    return ans;
}

int main(int argc, char **argv) {
    if (argc > 1)
    {
      printf("the number of digits are: %d\n", digit_cnt(*(argv+1)));
    }
    else
    {
      printf("string not given\n");
    }
    
    return 0;
}
 
