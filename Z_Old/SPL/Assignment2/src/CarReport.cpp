#include <CarReport.h>
#include "boost/lexical_cast.hpp"
#include <map>
#include "Simulator.h"
using namespace std;

const string CarReport::CAR_REPORT_TYPE("car_report");

CarReport::CarReport(Simulator & sim, const string &_id, const string &_carId, int time):
		Report(sim, _id, CAR_REPORT_TYPE, time), carId(_carId)
{
}


void CarReport::printData()
{
	cout<< "car_report id " << this->getId().c_str() << " car " << this->carId.c_str()
			<< " time " << this->time << endl;
}

void CarReport::writeReport()
{
	// Find the relevant car.
	Car* reportedCar = sim.findCarByName(carId);
	if (reportedCar == 0) {
		// Car not found.
		cout << "Report error from car: " << carId << " not found." << flush << endl;
		return;
	}

	string basePath = getId() + (string) ".";
	sim.reports.put(basePath + "carId", carId.c_str());
	sim.reports.put(basePath + "history", reportedCar->getHistory());
	sim.reports.put(basePath + "faultyTimeLeft", boost::lexical_cast<string>(max(0,reportedCar->getFaultTime() - sim.getTime()+1)));
	return;
}
