#include "Road.h"
#include "Car.h"
#include "Simulator.h"
#include "SimulatorObject.h"

using namespace std;
#include <iostream>

Road::Road(Simulator & _sim, const string &_to, const string &_from, double length):
		SimulatorObject(_sim, _from + _to), to(_to), from(_from), length(length), numOfCars(0)
{
}

bool Road::compareRoadPointers(Road * _one, Road * _two)
{
	return _one->_id < _two->_id;
}

void Road::printData()
{
	cout << "road from: " << this->from.c_str() << " to: " << this->to.c_str()
			<< " length: " << this->length << endl;
}


double Road::getLength()
{
	return this->length;
}

double Road::getCarSpeed()
{
	return ceil(this->length / this->numOfCars);
}

int Road::getNumOfCars()
{
	return this->numOfCars;
}

void Road::setNumOfCars(int num)
{
	this->numOfCars = num;
}


bool Road::equals(string start, string end)
{
	return (start == from) && (end == to);
}

bool Road::equals(Road &otherRoad)
{
	return (this->from == otherRoad.from) && (this->to == otherRoad.to);
}

const string& Road::getToJunctionName()
{
	return this->to;
}

string Road::getStart() {
	return this->from;
}

string Road::getDestination() {
	return this->to;
}

/**
 * Get all cars on the road.
 * No time limit specified.
 * Could be better if road had a pointer to the first car in carMap.
 */
vector<Car *> Road::getCarsOnRoad() {
	vector<Car *> carsFound;
	bool foundRoad = false;
	for (int i = 0; i < (int) sim.carMapVector.size(); i++)
	{

		// Iterate over all of the cars.

			if (!foundRoad) {
				foundRoad = (sim.carMapVector[i]->getRoad()->getId().compare(this->getId()) == 0);
			}

			if (foundRoad) {
				if (sim.carMapVector[i]->getRoad()->getId().compare(this->getId()) != 0) {
					// Out of road bounds.
					break;
				}
				// Add to the list.
				carsFound.push_back(sim.carMapVector[i]);
			}
		}
	return carsFound;
}

