#ifndef EVENT_H_
#define EVENT_H_

class Simulator;

#include <iostream>
#include <string>
#include "SimulatorObject.h"
#include "Car.h"

using namespace std;



class Event : public SimulatorObject{
public:
	virtual ~Event() {};
	virtual const void printData() = 0;
	virtual void performEvent() = 0;

	static bool compareEventPointers(Event * _one, Event * _two);
	int getTime() const;
	string getType() const;

protected:
	int time;
	string type;

	Event(int _time, const string &_type, const string &_eventId, Simulator &_sim);
};

#endif
