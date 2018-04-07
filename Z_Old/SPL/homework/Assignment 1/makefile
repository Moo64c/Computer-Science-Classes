all: main

main: bin/main.o 
	g++ -Wall -o bin/main bin/main.o
bin/main.o:src/main.cpp include/road.h
	g++ -c -Wall -Weffc++ -c -Linclude -o bin/main.o src/main.cpp
clean: 
	rm -f bin/*
