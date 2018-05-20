#include "Simulator.h"
#include "SimulatorObject.h"
#include "Junction.h"
#include <iostream>
using namespace std;

Junction::Junction(Simulator & _sim, const string &_junctionId, int _greenLightTime, int _maxGreenLightTime, int _minGreenLightTime):
SimulatorObject(_sim, _junctionId), greenTimeLeft(_greenLightTime),  _greenLightRoadIndex(0),
maxGreenLightTime(_maxGreenLightTime), minGreenLightTime(_minGreenLightTime), timeSliceIndex(0),
carPassedInTimeSlice(false), timeSliceFullyUsed(true), roads(), waitingCars(), timeSlices()
{
}

Junction::~Junction()
{
	for (int i=0; i < (int) roads.size(); i++)
	{
		delete roads.at(i);
	}
}


bool Junction::compareJunctions(Junction *_one, Junction *_two)
{
	return _one->getId() < _two->getId();
}

void Junction::addRoad(Road * road)
{
	// Default time slice.
	int defaultTime = (int) sim.getDEFAULT_TIME_SLICE();
	this->addRoad(road, defaultTime);
}

void Junction::addRoad(Road * road, int timeSlice)
{
	roads.push_back(road);
	timeSlices.push_back(timeSlice);
	//timeSliceIndex++;
}

void Junction::printData()
{
}


vector<int> Junction::getTimeSlices() {
	return this->timeSlices;
}

const vector<Road*>& Junction::getIncomingRoads() {
	return (this->roads);
}

/**
 * Whether a road has a green light.
 */
const bool Junction::hasGreenLight(int roadNumber) {
	return (this->_greenLightRoadIndex == roadNumber );
}

string Junction::getGreenLightTimeLeft() {
	return boost::lexical_cast<string>(this->greenTimeLeft);
}

void Junction::addCarToQueue(Car * carPointer)
{
	this->waitingCars.push_back(carPointer);
}

Road* Junction::getGreenLightRoad() {
	return this->roads[this->_greenLightRoadIndex];
}


void Junction::moveCarsInJunction() {
	// Find the car that can pass.
	Road * openRoad = this->roads[_greenLightRoadIndex];

	int i = 0;

	for ( ; i < (int) this->waitingCars.size(); i++) {
		// The first car in the incoming road that currently has green light will move
		// into its destination outgoing road (with location = 0).
		if (this->waitingCars[i]->getRoad()->equals(*openRoad)) {
			this->waitingCars[i]->advanceToNextRoad();
			this->carPassedInTimeSlice = true;

			// At each time unit, only one car can pass the junction.
			break;
		}
		// Not found.

	}
	// If a car was moved, erase it from the vector.
	// else - no car was moved, thus the time slot was not fully used.
	if (i < (int) this->waitingCars.size())
		waitingCars.erase (waitingCars.begin()+i);
	else
		this->timeSliceFullyUsed = false;

	// Reduce green light time.
	this->greenTimeLeft--;

	if (this->greenTimeLeft == 0) {
		// Time slice is over.

		// Change time slice according to specified logic.
		if (timeSliceFullyUsed) {
			this->increaseTimeSlice(_greenLightRoadIndex);
		}
		else if(!carPassedInTimeSlice) {
			this->decreaseTimeSlice(_greenLightRoadIndex);
		}

		// Rotate the green light.
		this->_greenLightRoadIndex = (this->_greenLightRoadIndex == (int) this->timeSlices.size() -1) ?
				0 : this->_greenLightRoadIndex+1;

		// Reset green light tracking variables.
		this->greenTimeLeft = this->timeSlices[_greenLightRoadIndex];
		this->timeSliceIndex = this->timeSlices[_greenLightRoadIndex];
		this->timeSliceFullyUsed = true;
		this->carPassedInTimeSlice = false;
	}
}

void Junction::increaseTimeSlice(int index) {
	this->timeSlices[index] = min(timeSlices[index] +1, (int) sim.getMAX_TIME_SLICE());
}

void Junction::decreaseTimeSlice(int index) {
	this->timeSlices[index] = max(timeSlices[index] -1, (int) sim.getMIN_TIME_SLICE());

}
