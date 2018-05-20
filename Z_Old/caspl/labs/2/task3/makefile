#format is target-name: target dependencies
#{-tab-}actions

# All Targets
all: dirs bin/menu 

# Tool invocations
bin/menu: lib/menu.o
	gcc -g -m32 -Wall -o bin/menu lib/menu.o

# Depends on the source and header files
lib/menu.o: menu.c 
	gcc -m32 -g -Wall -ansi -c -o lib/menu.o menu.c 


#tell make that "clean" is not a file name!
.PHONY: clean

#Clean the build directory
clean: 
	rm -f lib/* bin/*

dirs: 
	mkdir -p bin lib