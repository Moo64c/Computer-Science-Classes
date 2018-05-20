#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: bin/my_echo

# Tool invocations
# Executable "main" depends on the files main.o 
bin/my_echo: lib/main.o
	gcc -g -m32 -Wall -o bin/my_echo lib/main.o

# Depends on the source and header files
lib/main.o: main.c main.h
	gcc -m32 -g -Wall -ansi -c -o lib/main.o main.c 
 
# Run.o: Run.c
# 	gcc -m32 -g -Wall -ansi -c -o Run.o Run.c


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f *.o bin/main
