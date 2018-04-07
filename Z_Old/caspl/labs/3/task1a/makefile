#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/virusDetector 

# Tool invocations
bin/virusDetector: lib/virusDetector.o
	gcc -g -m32 -Wall -o bin/virusDetector lib/virusDetector.o

# Depends on the source and header files
lib/virusDetector.o: virusDetector.c 
	gcc -m32 -g -Wall -ansi -c -o lib/virusDetector.o virusDetector.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib
