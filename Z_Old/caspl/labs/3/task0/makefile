#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/bubblesort bin/hexaPrint.c

# Tool invocations
bin/bubblesort: lib/bubblesort.o
	gcc -g -m32 -Wall -o bin/bubblesort lib/bubblesort.o

bin/hexaPrint.c: lib/hexaPrint.o
	gcc -g -m32 -Wall -o bin/hexaPrint lib/hexaPrint.o

# Depends on the source and header files
lib/bubblesort.o: bubblesort.c 
	gcc -m32 -g -Wall -ansi -c -o lib/bubblesort.o bubblesort.c 

lib/hexaPrint.o: hexaPrint.c 
	gcc -m32 -g -Wall -ansi -c -o lib/hexaPrint.o hexaPrint.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib
