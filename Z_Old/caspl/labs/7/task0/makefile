#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/hexeditplus 

# Tool invocations
bin/hexeditplus: lib/hexeditplus.o
	gcc -g -m32 -Wall -o bin/hexeditplus lib/hexeditplus.o

# Depends on the source and header files
lib/hexeditplus.o: hexeditplus.c 
	gcc -m32 -g -Wall -ansi -c -o lib/hexeditplus.o hexeditplus.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib 
	
	
