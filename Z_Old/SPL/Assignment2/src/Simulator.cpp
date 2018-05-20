/*
 * Simulator.cpp
 * 	Class Simulator as defined in Simulator.h
 *  Created on: Nov 18, 2014
 *      Author: yotamd
 */

#include <Simulator.h>
#include <iostream>
#include <algorithm> // for std::sort, min
#include "boost/property_tree/ini_parser.hpp"
#include "boost/property_tree/ptree.hpp"
#include "boost/lexical_cast.hpp"
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>
#include "Car.h"
#include <cmath>

using boost::property_tree::ptree;

	const string Simulator::CONF_FILENAME("Configuration.ini");
	const string Simulator::EVENT_FILENAME("Events.ini");
	const string Simulator::COMMAND_FILENAME("Commands.ini");
	const string Simulator::ROAD_FILENAME("RoadMap.ini");


Simulator::Simulator() :
carMapVector(), carMapByName(),reports(),
MAX_SPEED(0), DEFAULT_TIME_SLICE(0), MAX_TIME_SLICE(0), MIN_TIME_SLICE(0),
time(1), terminationTime(-1),eventPosition(0),commandPosition(0),
junctions(), roads(), events(), commands(), lastReportIndex(0)
{
	readConf(CONF_FILENAME);
	readEvents(EVENT_FILENAME);
	readRoads(ROAD_FILENAME);
	readCommands(COMMAND_FILENAME);
}

Simulator::~Simulator()
{
	// Delete events.
	for (int i = 0; i < (int) events.size(); i++)
	{
		delete events.at(i);
	}

	//	Delete report.
	for (int i = 0; i < (int) commands.size(); i++)
	{
		delete commands.at(i);
	}

	// Delete junctions.
	for (int i = 0; i < (int) junctions.size(); i++)
	{
		delete junctions.at(i);
	}

	// Delete cars.
	for (map<string, Car*>::iterator it = this->carMapByName.begin(); it != this->carMapByName.end(); it++)
	{
		delete it->second;
	}


}

void Simulator::startSimulation()
{
	for (int i= 0; (i <= terminationTime || (terminationTime == -1)) && i < 3232; i++)
	{
		timeTick();
	}
}

void Simulator::readConf(const string &_filename)
{
	  ptree confPT;
	  read_ini(_filename.c_str(), confPT);
	  this->MAX_SPEED = confPT.get<double>("Configuration.MAX_SPEED");
	  this->DEFAULT_TIME_SLICE = confPT.get<double>("Configuration.DEFAULT_TIME_SLICE");
	  this->MAX_TIME_SLICE = confPT.get<double>("Configuration.MAX_TIME_SLICE");
	  this->MIN_TIME_SLICE = confPT.get<double>("Configuration.MIN_TIME_SLICE");

}

void Simulator::readEvents(const string &_filename)
{
	  ptree eventPT;
	  read_ini(_filename.c_str(), eventPT);

	  for (ptree::const_iterator section = eventPT.begin();section != eventPT.end(); section++) {
		  string eventName(section->first);
		  if (eventPT.get<string>((eventName + string(".type")).c_str()).compare("car_fault") == 0)
		  {
			  int time = eventPT.get<int>((eventName + string(".time")).c_str());
			  string carId = eventPT.get<string>((eventName + string(".carId")).c_str());
			  int timeOfFault = eventPT.get<int>((eventName + string(".timeOfFault")).c_str());

			  CarFaultEvent * CarFault = new CarFaultEvent(time, carId, timeOfFault, *this);
			  events.push_back( (Event *) CarFault );
	      }
		  else if (eventPT.get<string>((eventName + string(".type")).c_str()).compare("car_arrival") == 0)
		  {
			  int time = eventPT.get<int>((eventName + string(".time")).c_str());
			  string carId = eventPT.get<string>((eventName + string(".carId")).c_str());
			  string roadPlan = eventPT.get<string>((eventName + string(".roadPlan")).c_str());

			  CarArrivalEvent * newCar = new CarArrivalEvent(time, carId, roadPlan, *this);
			  events.push_back( (Event*) newCar );
		  }
	    }
	  sort(events.begin(), events.end(), Event::compareEventPointers);
}



void Simulator::timeTick()
{
	executeEvents();
	sort (this->carMapVector.begin(), this->carMapVector.end(), Car::compareCarPtr);
	for (int i = 0 ; i < (int) carMapVector.size(); )
	{
		if (carMapVector[i]->getFinished())
		{
			//cout << "deleting " << carMapVector[i]->getId().c_str()<<endl;
			carMapVector[i]->getRoad()->setNumOfCars(carMapVector[i]->getRoad()->getNumOfCars() - 1);
			carMapVector.erase(this->carMapVector.begin() + i);
		}
		else
			i++;

	}

	saveHistory();
	writeReports();

	if (this->time == this->terminationTime) {
		terminate();
	}
	else
	{
		moveCarsInRoads();
		moveCarsInJunctions();
	}

	this->time++;

}

void Simulator::executeEvents()
{

	for (; eventPosition < (int) events.size() && (events[eventPosition]->getTime() <= this->time) ; ++eventPosition)
	{
		events[eventPosition]->performEvent();
	}
}

/**
 * Move the cars according to the specified rules.
 */
void Simulator::moveCarsInRoads()
{

	int numOfFaultyCars = 0;
	string lastRoadId("");

	// Iterate cars from end to beginning.
	for (int i = 0 ; i < (int) this->carMapVector.size(); i++)
	{
		Car *currentCar = this->carMapVector[i];
		if (currentCar->getFinished())
		{
			continue;
		}
		Road *currentRoad = currentCar->getRoad();

		if (currentRoad->getId().compare(lastRoadId) != 0) {
			// If the road is new, reset number of faulty cars.
			numOfFaultyCars = 0;
			// This works since when iterating over the car map once we are in a car that's on a new road,
			// the others after it won't be on the old road again.
		}

		double baseSpeed = currentRoad->getCarSpeed();

		if (currentCar->getFaultTime() >= this->time) {
			// If car is faulty, increment number of faulty cars in the road.
			numOfFaultyCars++;
		}

		// If car isn't faulty and isn't in the end of the road, move it according to formula.
		else if (currentCar->getPosition() < currentRoad->getLength())
		{
			// Move the car.
			double speed = ceil(min(baseSpeed / pow(2.0, numOfFaultyCars), this->getMAX_SPEED()));
			currentCar->setPosition(speed);
			if (currentCar->getPosition() == currentRoad->getLength())
			{
				// If car is at end of road, move it to junction's queue.
				Junction *carJunc = this->findJunctionByName(currentRoad->getToJunctionName());
				carJunc->addCarToQueue(currentCar);
			}

			// Reinsert the car in a position before current. since the car will still stay ahead.

		}
		// For road comparison.
		lastRoadId = currentRoad->getId();
	}
}

/**
 * Use binary search to find a junction by it's ID.
 */
Junction* Simulator::findJunctionByName(const string &_name) const
{
	if (junctions.size() == 0) return 0;

	int min = 0;
	int max = (int) junctions.size();
	int position = (max + min) / 2;
	while (junctions[position]->getId().compare(_name) != 0 && (max - min > 0))
	{
		if (junctions[position]->getId().compare(_name) > 0)
		{
			max = position;
			position = (max + min) / 2;
		}
		else if (junctions[position]->getId().compare(_name) < 0)
		{
			min = position;
			position = (max + min) / 2;
		}
	}

	if (junctions[position]->getId().compare(_name) == 0)
		return junctions[position];
	else
		return 0;
}

/**
 * Use binary search to find a road by it's ID.
 */
Road* Simulator::findRoadByName(const string &_name) const
{
	if (roads.size() == 0) return 0;

	int min = 0;
	int max = (int) roads.size();
	int position = (max + min) / 2;
	while (roads[position]->getId().compare(_name) != 0 && (max - min > 0))
	{
		if (roads[position]->getId().compare(_name) > 0)
		{
			max = position;
			position = (max + min) / 2;
		}
		else if (roads[position]->getId().compare(_name) < 0)
		{
			min = position;
			position = (max + min) / 2;
		}
	}

	if (roads[position]->getId().compare(_name) == 0) {
		return roads[position];
	}

	return 0;
}

/**
 * Use built-in search to find a car by it's ID.
 */
Car* Simulator::findCarByName(const string &_name) const
{
	if (carMapByName.empty()) {
		return 0;
	}
	map<string, Car*>::const_iterator found = carMapByName.find(_name);
	if (found == carMapByName.end()) {
		// Not found.
		return 0;
	}
	return found->second;
}

void Simulator::moveCarsInJunctions()
{
	// Let the Junction class handle this.
	for (int i= 0; i < (int) this->junctions.size(); i++) {
		this->junctions[i]->moveCarsInJunction();
	}
}


void Simulator::readRoads(const string &_filename)
{
	  ptree roadPT;
	  read_ini(_filename.c_str(), roadPT);

	  for (ptree::const_iterator section = roadPT.begin();section != roadPT.end(); section++) {
		  string junctionId(section->first);
		  Junction *newJunction = new Junction(*this, junctionId, this->DEFAULT_TIME_SLICE, this->MAX_TIME_SLICE, this->MIN_TIME_SLICE);
		  for (ptree::const_iterator property =    section->second.begin();property != section->second.end(); property++) {
			  Road * newRoad = new Road(*this, junctionId, property->first, boost::lexical_cast<int>(property->second.data()));
			  newJunction->addRoad(newRoad);
			  roads.push_back(newRoad);
		  }
		  this->junctions.push_back(newJunction);
	    }
	  sort(junctions.begin(), junctions.end(), Junction::compareJunctions);
	  sort(roads.begin(), roads.end(), Road::compareRoadPointers);
}

/**
 * Read and store the commands from the configuration files.
 */
void Simulator::readCommands(const string &_filename)
{
	  ptree commandPT;
	  read_ini(_filename.c_str(), commandPT);

	  for (ptree::const_iterator section = commandPT.begin();section != commandPT.end(); section++) {
		  string commandName(section->first);
		  string commandType =commandPT.get<string>((commandName + string(".type")).c_str());

		  if (commandType.compare("termination") == 0)
		  {
			  if (this->terminationTime == -1)
				  this->terminationTime = commandPT.get<int>((commandName + string(".time")).c_str());
			  else
				  this->terminationTime = min(this->terminationTime, commandPT.get<int>((commandName + string(".time")).c_str()));
	      }
		  else if (commandType.compare("car_report") == 0)
		  {
			  string reportId = commandPT.get<string>((commandName + string(".id")).c_str());
			  int reportTime = commandPT.get<int>((commandName + string(".time")).c_str());
			  string carId = commandPT.get<string>((commandName + string(".carId")).c_str());

			  Report * rep = new CarReport(*this, reportId, carId, reportTime);
			  commands.push_back((Report *) rep);
		  }
		  else if (commandType.compare("junction_report") == 0)
		  {
			  string reportId = commandPT.get<string>((commandName + string(".id")).c_str());
			  int reportTime = commandPT.get<int>((commandName + string(".time")).c_str());
			  string junctionId = commandPT.get<string>((commandName + string(".junctionId")).c_str());

			  Report * rep= new JunctionReport(*this, reportId, junctionId, reportTime);
			  commands.push_back((Report *) rep);

		  }
		  else if (commandType.compare("road_report") == 0)
		  {
			  string reportId = commandPT.get<string>((commandName + string(".id")).c_str());
			  int reportTime = commandPT.get<int>((commandName + string(".time")).c_str());
			  string startJunction = commandPT.get<string>((commandName + string(".startJunction")).c_str());
			  string endJunction = commandPT.get<string>((commandName + string(".endJunction")).c_str());

			  Report * rep = new RoadReport(*this, reportId, startJunction, endJunction, reportTime);
			  commands.push_back((Report *) rep);
		  }
	    }
	  sort(commands.begin(), commands.end(), Report::compareReportPointers);
}

void Simulator::addCar(Car *addedCar)
{
	this->carMapByName.insert(pair<string, Car *>(addedCar->getId(), addedCar));

	this->carMapVector.push_back(addedCar);
}

int Simulator::getTime() const
{
	return this->time;
}


/**
 * Writes the reports for the current time step.
 */
void Simulator::writeReports() {
	bool passedLastCommand = false;
	for (int i = this->lastReportIndex; i < (int) this->commands.size(); i++) {
		if (this->commands.at(i)->getReportTime() == this->time) {
			passedLastCommand = true;
			this->commands.at(i)->writeReport();
		}
		else if (passedLastCommand) {
			lastReportIndex = i;
			break;
		}
	}
}

/**
 * For every car, save it's current status.
 */
void Simulator::saveHistory() {
	for (int i = 0 ; i < (int) this->carMapVector.size(); i++) {
		this->carMapVector[i]->saveHistory();
	}
}

/**
 * Terminate the simulation.
 *  - Write reports.
 *  - Delete variables.
 *	- Print stuff.
 *	- Be cool about it, yo. most def.
 */
void Simulator::terminate() {
	// Output the INI file.
	write_ini("Reports.ini", this->reports);
}

// Forces all the objects to report.
void Simulator::forceAllReports(int time) {
	for (int i =0; i < (int) this->junctions.size(); i++) {
		JunctionReport *report = new JunctionReport(*this,"report_j" + boost::lexical_cast<string>(i), this->junctions.at(i)->getId(),time);
		report->writeReport();
	}

	for (int i =0; i < (int) this->roads.size(); i++) {
		RoadReport *report = new RoadReport(*this,"report_r" + boost::lexical_cast<string>(i), this->roads.at(i)->getStart(),  this->roads.at(i)->getDestination(),time);
		report->writeReport();
	}




	write_ini("reports_forced.ini", reports);
}


double Simulator::getMAX_SPEED() {
	return this->MAX_SPEED;
}
double Simulator::getDEFAULT_TIME_SLICE() {
	return this->DEFAULT_TIME_SLICE;
}
double Simulator::getMAX_TIME_SLICE() {
	return this->MAX_TIME_SLICE;
}
double Simulator::getMIN_TIME_SLICE() {
	return this->MIN_TIME_SLICE;
}


/***************DEBUG SECTION*******************/

void Simulator::testReadConf()
{
	cout << "MAX_SPEED: " << this->MAX_SPEED << endl;
	cout << "DEFAULT_TIME_SLICE: " << this->DEFAULT_TIME_SLICE << endl;
	cout << "MAX_TIME_SLICE: " << this->MAX_TIME_SLICE << endl;
	cout << "MIN_TIME_SLICE: " << this->MIN_TIME_SLICE << endl;

}

void Simulator::testReadEvents()
{
	for (int i=0; i < (int) events.size(); i++)
	{
		events.at(i)->printData();
	}

}


void Simulator::testReadCommands()
{
	cout << "termination time: " << this->terminationTime;
	for (int i=0; i < (int) commands.size(); i++)
	{
		commands.at(i)->printData();
	}

}


void Simulator::testReadRoads()
{
	for (int i=0; i < (int) junctions.size(); i++)
	{
		junctions.at(i)->printData();
	}

}
