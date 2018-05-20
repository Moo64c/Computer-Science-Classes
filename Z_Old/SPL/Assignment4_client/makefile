CFLAGS:=-c -Wall -Weffc++ -g -Iinclude/
LDFLAGS:=-lboost_system -lboost_locale -lboost_thread

all: WhatsappClient
	g++ -o bin/client bin/main.o bin/Message.o bin/WhatsappMessangerConnectionHandler.o bin/BackgroundReader.o bin/FlowManagement.o  $(LDFLAGS)
	echo finished!

WhatsappClient: bin/main.o bin/Message.o  bin/BackgroundReader.o bin/FlowManagement.o bin/WhatsappMessangerConnectionHandler.o

bin/WhatsappMessangerConnectionHandler.o: src/WhatsappMessangerConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/WhatsappMessangerConnectionHandler.o src/WhatsappMessangerConnectionHandler.cpp
	
bin/Message.o: src/Message.cpp
	g++ $(CFLAGS) -o bin/Message.o src/Message.cpp

bin/main.o: src/main.cpp
	g++ $(CFLAGS) -o bin/main.o src/main.cpp
	
bin/FlowManagement.o: src/FlowManagement.cpp
	g++ $(CFLAGS) -o bin/FlowManagement.o src/FlowManagement.cpp
	
bin/BackgroundReader.o: src/BackgroundReader.cpp
	g++ $(CFLAGS) -o bin/BackgroundReader.o src/BackgroundReader.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
