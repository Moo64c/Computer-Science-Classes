#ifndef CARFAULTEVENT_H_
#define CARFAULTEVENT_H_


#include <iostream>
#include <string>
#include "Event.h"
#include "Simulator.h"
#include "Car.h"


using namespace std;

class CarFaultEvent : public Event {
public:
	CarFaultEvent();
	CarFaultEvent(const CarFaultEvent& other);
	~CarFaultEvent();

	CarFaultEvent(int _eventTime, const string &_id, int _timeOfFault, Simulator & _sim);

	const void printData();

	void performEvent();

	static const string CAR_FAULT_TYPE;

private:
	// How long the fault lasts.
	int timeOfFault;
};

#endif
