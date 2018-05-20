#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs glue bin/task0 

# Tool invocations
bin/task0: lib/main.o lib/util.o
	ld -melf_i386 lib/start.o lib/main.o lib/util.o -o bin/task0

# Depends on the source and header files
lib/main.o: main.c 
	gcc -m32 -Wall -ansi -c -nostdlib -fno-stack-protector main.c -o lib/main.o

lib/util.o: util.c
	gcc -m32 -Wall -ansi -c -nostdlib -fno-stack-protector util.c -o lib/util.o

#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib

glue:
	nasm -f elf start.s -o lib/start.o
