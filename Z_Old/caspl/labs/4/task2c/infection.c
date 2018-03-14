extern int system_call();

int main (int argc , char* argv[], char* envp[]) {
  system_call(4, 1, "Hello, Infected File\n", 20); 
  return 0;
}