#ifndef CARARRIVALEVENT_H_
#define CARARRIVALEVENT_H_

#include <iostream>
#include <string>
#include "Event.h"
#include "Car.h"

using namespace std;

class CarArrivalEvent : public Event {
public:
	CarArrivalEvent(int _time, const string &_id, const string &_roadPlan, Simulator & _sim);
	~CarArrivalEvent();

	const void printData();

	void performEvent();

	static const string CAR_ARRIVAL_TYPE;

private:
	// Road plan as plaintext.
	string roadPlan;


	vector<string> splitString(string heap, string needle);

};

#endif
