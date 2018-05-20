#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/can_link 

# Tool invocations
bin/can_link: lib/can_link.o elf.h
	gcc -g -m32 -Wall -o bin/can_link lib/can_link.o elf.h

# Depends on the source and header files
lib/can_link.o: can_link.c 
	gcc -m32 -g -Wall -ansi -c -o lib/can_link.o can_link.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib 
	
	
