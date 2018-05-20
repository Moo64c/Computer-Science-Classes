#include "Car.h"
#include "Simulator.h"
#include "SimulatorObject.h"
#include <iostream>
#include <vector>

Car::Car(Simulator & _sim, const string &_id, const vector<Road*> &_path):
SimulatorObject(_sim, _id), finished(false), path(_path), _road(_path[0]), faultTime(-1), position(0), positionInPath(0), history()
{
	this->_road->setNumOfCars(this->_road->getNumOfCars() + 1);
}

Car::~Car()
{
}

Car& Car::operator= (const Car &_other)
{
	this->_road= _other._road;
	this->path= _other.path;
	this->finished = _other.finished;
	this->position = _other.position;
	this->positionInPath = _other.positionInPath;
	this->_id=_other._id;

	return *this;

}

Road* Car::getRoad() const
{
	return this->_road;
}

int Car::getFaultTime() const
{
	return this->faultTime;
}

bool Car::getFinished() const
{
	return this->finished;
}

void Car::setPosition(double speed)
{
	this->position = min(this->position + speed, this->_road->getLength());
}

double Car::getPosition() const
{
	return this->position;
}

string Car::getHistory() {
	string historyString = "";
	for(int i =0; i < (int) this->history.size() && i <= this->sim.getTime(); i++) {
		historyString += this->history.at(i);
	}
	return historyString;
}

void Car::setFaultTime(int time)
{
	this->faultTime = time;
}

/**
 * Save car's current status as a string.
 */
void Car::saveHistory() {
	Road* currentRoad = this->getRoad();
	int time = this->sim.getTime() ;
	string historyString = string("(") + boost::lexical_cast<string>(time) + string(",") + currentRoad->getStart() + "," +
			currentRoad->getDestination() + string(",") + boost::lexical_cast<string>(this->getPosition()) +")";
	this->history.push_back(historyString);
}

/**
 * Advance the car to it's next road, if available.
 */
void Car::advanceToNextRoad() {
	// Remove this from the carMap.

	this->positionInPath++;
	if (this->positionInPath > (int) path.size() -1) {
		// No road to move to. removing car.
		this->finished = true;
		return;
	}

	// Alter variables.
	this->_road->setNumOfCars(this->_road->getNumOfCars() - 1);



	this->position = 0.0;

	this->_road = (this->path.at(positionInPath));
	this->_road->setNumOfCars(this->_road->getNumOfCars() + 1);

}

bool Car::compareCarPtr(const Car * one, const Car * other)
{
	if (one->getRoad()->getId().compare(other->getRoad()->getId()) > 0)
	{
			return true;
	}
	else if (one->getRoad()->getId().compare(other->getRoad()->getId()) < 0)
	{
			return false;
	}
	else
	{
		if (one->getPosition() > other->getPosition())
		{
			return true;
		}
		else if (one->getPosition() < other->getPosition())
		{
			return false;
		}
		else
		{
			return (one->getId().compare(other->getId()) > 0);
		}
	}
}
