#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/examine 

# Tool invocations
bin/examine: lib/examine.o
	gcc -g -m32 -Wall -o bin/examine lib/examine.o

# Depends on the source and header files
lib/examine.o: examine.c 
	gcc -m32 -g -Wall -ansi -c -o lib/examine.o examine.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib 
	
	
