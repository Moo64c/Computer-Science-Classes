/*
 * Event.cpp
 *
 *  Created on: Nov 18, 2014
 *      Author: yotamd
 */


#include "Event.h"
#include "Simulator.h"
#include "SimulatorObject.h"

bool Event::compareEventPointers(Event * _one, Event * _two)
{
	return _one->time < _two->time;
}


Event::Event(int _time, const string &_type, const string &_eventId, Simulator & _sim):
		SimulatorObject(_sim , _eventId), time(_time), type(_type) {
}

int Event::getTime() const
{
	return this->time;
}

string Event::getType() const
{
	return this->type;
}
