/*
 * CarFaultEvent.cpp
 *
 *  Created on: Nov 18, 2014
 *      Author: yotamd
 */


using namespace std;

#include "Car.h"
#include "CarFaultEvent.h"
#include "Simulator.h"

const string CarFaultEvent::CAR_FAULT_TYPE("car_fault");

CarFaultEvent::~CarFaultEvent()
{

}


CarFaultEvent::CarFaultEvent(int _eventTime, const string &_id,
		int _timeOfFault,
		Simulator & _sim):
Event(_eventTime, CAR_FAULT_TYPE ,_id, _sim),
		timeOfFault(_timeOfFault)
{

}

const void CarFaultEvent::printData()
{
	cout << "carFaultEvent - time: " << this->time << " carId: " << this->getId().c_str()
			<< " timeOfFault: " << this->timeOfFault<<endl;
}

 void CarFaultEvent::performEvent()
{
	Car *faultyCar = sim.findCarByName(this->getId());
	faultyCar->setFaultTime(this->timeOfFault + sim.getTime() - 1);
}
