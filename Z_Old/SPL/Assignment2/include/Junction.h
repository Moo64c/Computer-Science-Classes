/*
 * Junction.h
 *
 *  Created on: Nov 17, 2014
 *      Author: yotamd
 */

#ifndef JUNCTION_H_
#define JUNCTION_H_

using namespace std;

#include "Road.h"
#include "Car.h"
#include "SimulatorObject.h"
#include <vector>
#include <string>

class Simulator;

class Junction: public SimulatorObject {
public:
	Junction();
	Junction(Junction &_other);
	Junction(Simulator & _sim, const string &_junctionId, int _greenLightTime, int _maxGreenLightTime, int _minGreenLightTime);
	~Junction();

	void addRoad(Road * _toAdd);
	void addRoad(Road * _toAdd, int timeSlice);

	// Set which road has a green light.
	void setLights();

	static bool compareJunctions(Junction *_one, Junction *_two);

	void printData();


	/**
	 * Return all incoming roads in this junction.
	 */
	//vector<Road*> getIncomingRoads();
	const vector<Road*>& getIncomingRoads();

	/**
	 * Whether a road has a green light.
	 */
	const bool hasGreenLight(int roadNumber);

	/**
	 * Return road time slices.
	 */
	 vector<int> getTimeSlices();
	 void addCarToQueue(Car * carPointer);

	 void moveCarsInJunction();
	 void increaseTimeSlice(int index);
	 void decreaseTimeSlice(int index);
	 string getGreenLightTimeLeft();
	 /**
	  * Get the current green lighted road.
	  */
	 Road* getGreenLightRoad();

private:
	int greenTimeLeft;
	int  _greenLightRoadIndex;
	int maxGreenLightTime;
	int minGreenLightTime;
	int timeSliceIndex;
	bool carPassedInTimeSlice;
	bool timeSliceFullyUsed;

	vector<Road*> roads;
	vector<Car*> waitingCars;

	// Denotes current green light, as referred to in the roads array.
	vector<int > timeSlices;
};


#endif /* JUNCTION_H_ */
