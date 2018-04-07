# All Targets
all: main

main: bin/LinkedList.o bin/Run.o
	g++ -o bin/main bin/LinkedList.o bin/Run.o

# Depends on the source and header files
bin/LinkedList.o: src/LinkedList.cpp
	g++ -g -Wall -Weffc++ -c -Linclude -o bin/LinkedList.o src/LinkedList.cpp

# Depends on the source and header files 
bin/Run.o: src/Run.cpp
	g++ -g -Wall -Weffc++ -c -Linclude -o bin/Run.o src/Run.cpp

#Clean the build directory
clean: 
	rm -f bin/*
