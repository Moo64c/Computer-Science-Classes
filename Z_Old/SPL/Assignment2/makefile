###
# Assignment 2 makefile
###

all: Assignment2

Assignment2: bin/Car.o  bin/Junction.o  bin/Road.o bin/Report.o bin/CarReport.o bin/RoadReport.o bin/JunctionReport.o bin/Event.o bin/CarFaultEvent.o bin/CarArrivalEvent.o bin/Simulator.o bin/SimulatorObject.o bin/main.o
	g++ -o bin/Assignment2  bin/Car.o  bin/Junction.o  bin/Road.o bin/Report.o bin/CarReport.o bin/RoadReport.o bin/JunctionReport.o bin/Event.o bin/CarFaultEvent.o bin/CarArrivalEvent.o bin/Simulator.o bin/SimulatorObject.o bin/main.o

bin/Car.o: src/Car.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Car.o src/Car.cpp

bin/Junction.o: src/Junction.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Junction.o src/Junction.cpp

bin/Road.o: src/Road.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Road.o src/Road.cpp

bin/Report.o: src/Report.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Report.o src/Report.cpp

bin/CarReport.o: src/CarReport.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/CarReport.o src/CarReport.cpp

bin/RoadReport.o: src/RoadReport.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/RoadReport.o src/RoadReport.cpp

bin/JunctionReport.o: src/JunctionReport.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/JunctionReport.o src/JunctionReport.cpp

bin/Event.o: src/Event.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Event.o src/Event.cpp

bin/CarFaultEvent.o: src/CarFaultEvent.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/CarFaultEvent.o src/CarFaultEvent.cpp

bin/CarArrivalEvent.o: src/CarArrivalEvent.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/CarArrivalEvent.o src/CarArrivalEvent.cpp

bin/Simulator.o: src/Simulator.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/Simulator.o src/Simulator.cpp
	
bin/SimulatorObject.o: src/SimulatorObject.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/SimulatorObject.o src/SimulatorObject.cpp

bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -c -I$../include -o bin/main.o src/main.cpp

clean:
	rm -f bin/*
