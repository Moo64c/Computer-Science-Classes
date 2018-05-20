/*
 * Road.h
 *
 *  Created on: Nov 17, 2014
 *      Author: yotamd
 */

#ifndef ROAD_H_
#define ROAD_H_

class Junction;

using namespace std;

#include "Junction.h"
#include "SimulatorObject.h"
#include <string>
#include <vector>

class Simulator;
class Car;

class Road: public SimulatorObject {
public:
	Road();
	Road(Road &_other);
	Road(Simulator & _sim, const string &_to, const string &_from, double _length);
	~Road(){};

	static bool compareRoadPointers(Road * _one, Road * _two);

	bool equals(string start, string end);
	bool equals(Road &otherRoad);
	void printData();
	double getLength();
	int getNumOfCars();
	double getCarSpeed();
	const string& getToJunctionName() ;
	vector<Car *> getCarsOnRoad();
	void setNumOfCars(int num);

	string getDestination();
	string getStart();

private:
	string to;
	string from;
	double length;
	int numOfCars;
};


#endif /* ROAD_H_ */
