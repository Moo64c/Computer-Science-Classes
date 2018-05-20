### 
# Makefile for the Polygon project
###

COMPILER = g++

Main : Main.o Polygon.o Point.o
	g++ -o $@ Main.o Polygon.o Point.o
Polygon.o  : Polygon.cpp Polygon.h Point.h
	$(COMPILER) -o $@ -c Polygon.cpp
Point.o  : Point.cpp Point.h
	$(COMPILER) -o $@ -c Point.cpp

Main.o  : Main.cpp Polygon.h Point.h
	$(COMPILER) -o $@ -c Main.cpp

clean : 
	rm -f *.o Main


###
# MAKE OUTPUT:
# g++ -o Main.o -c Main.cpp
# g++ -o Polygon.o -c Polygon.cpp
# g++ -o Point.o -c Point.cpp
# g++ -o Main Main.o Polygon.o Point.o
###
