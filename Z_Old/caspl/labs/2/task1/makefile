#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/addresses 

# Tool invocations
bin/addresses: lib/addresses.o
	gcc -g -m32 -Wall -o bin/addresses lib/addresses.o

# Depends on the source and header files
lib/addresses.o: addresses.c 
	gcc -m32 -g -Wall -ansi -c -o lib/addresses.o addresses.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib