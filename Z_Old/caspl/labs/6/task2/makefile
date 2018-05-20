#format is target-name: target dependencies
#{-tab-}actions


# All Targets
all: dirs bin/mypipeline 

# Tool invocations
#bin/mypipeline: lib/mypipeline.o lib/LineParser.o
#	gcc -g -m32 -Wall -o bin/mypipeline lib/mypipeline.o lib/LineParser.o
bin/mypipeline: lib/mypipeline.o
	gcc -g -m32 -Wall -o bin/mypipeline lib/mypipeline.o 

lib/mypipeline.o: mypipeline.c 
	gcc -m32 -g -Wall -ansi -c -o lib/mypipeline.o mypipeline.c 

#lib/LineParser.o: LineParser.c LineParser.h
#	gcc -m32 -g -Wall -ansi -c -o  lib/LineParser.o LineParser.c 

#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib
