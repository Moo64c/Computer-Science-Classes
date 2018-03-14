/*
 * CarArrivalEvent.cpp
 *
 *  Created on: Nov 18, 2014
 *      Author: yotamd
 */

using namespace std;

#include <CarArrivalEvent.h>
#include "Simulator.h"

const string CarArrivalEvent::CAR_ARRIVAL_TYPE("car_arrival");

CarArrivalEvent::~CarArrivalEvent()
{

}

CarArrivalEvent::CarArrivalEvent(int _time, const string &_id, const string &_roadPlan, Simulator &_sim):
Event(_time, CAR_ARRIVAL_TYPE, _id, _sim),
		roadPlan(_roadPlan)
{

}


const void CarArrivalEvent::printData()
{
	cout << "CarArrivalEvent - time: " << this->time << " carId: " << this->getId().c_str()
			<< " roadPlan: " << this->roadPlan.c_str()<<endl;
}

 void CarArrivalEvent::performEvent()
{

	try {
		vector<Road *> carRoads;
		vector<string> roadNames = splitString(roadPlan,",");
		for (vector<string>::iterator it = roadNames.begin(); it != roadNames.end()-1; it++)
		{
			carRoads.push_back(sim.findRoadByName(*it+(*(it+1))));
		}

		Car *newCar = new Car(sim, _id, carRoads);

		this->sim.addCar(newCar);
	}
	catch (int e)
	{
		return;
	}
}


 vector<string> CarArrivalEvent::splitString(string heap, string needle) {
 	int start = 0;
 	int foundAt = 0;
 	vector<string> strings;
 	string found;
 	while (heap.find(needle, start) != string::npos) {
 		foundAt = heap.find(needle, start);
 		found = heap.substr(start, foundAt-needle.length() - start + 1);
 		strings.push_back(found);
 		start=foundAt+needle.length();
 	}

   if (start != (int) heap.size()) {
 		// Add last word.
 		found = heap.substr(start);
 		strings.push_back(found);
 	}

 	return strings;
 }
