#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/countwords

# Tool invocations
bin/countwords: lib/countwords.o
	gcc -g -m32 -Wall -o bin/countwords lib/countwords.o

# Depends on the source and header files
lib/countwords.o: countwords.c 
	gcc -m32 -g -Wall -ansi -c -o lib/countwords.o countwords.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f *.o bin/countwords

dirs: 
	mkdir -p bin lib