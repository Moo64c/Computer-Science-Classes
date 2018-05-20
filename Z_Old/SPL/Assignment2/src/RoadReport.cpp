#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>
#include <RoadReport.h>
#include "boost/lexical_cast.hpp"

using boost::property_tree::ptree;
using namespace std;

const string RoadReport::ROAD_REPORT_TYPE("road_report");

RoadReport::RoadReport(Simulator & sim, const string &_id, const string &_startJunction, const string &_endJunction, int time):
		Report(sim,  _id, ROAD_REPORT_TYPE, time), startJunction(_startJunction),  endJunction(_endJunction)
{
}


void RoadReport::printData()
{
	cout<< "road_report id " << this->getId().c_str() << " from " << startJunction.c_str() << " to " << this->endJunction.c_str()
			<< " time " << this->time << endl;
}

void RoadReport::writeReport()
{
	// Find the relevant road.
	Road* reportedRoad = sim.findRoadByName(startJunction + endJunction);

	if (reportedRoad == 0) {
		// Car not found.
		cout << "Report error from road " << startJunction.c_str() << " to " << this->endJunction.c_str() << " not found." << endl;
		return;
	}

	vector<Car *> carsOnRoad = reportedRoad->getCarsOnRoad();
	string carString = "";
	Car * currentCar;
	for (int i=0; i < (int) carsOnRoad.size(); i++) {
		currentCar = carsOnRoad.at(i);
		carString += "(" + currentCar->getId() + "," + boost::lexical_cast<string>(currentCar->getPosition()) + ")";
	}

	string basePath = getId() + (string) ".";
	sim.reports.put(basePath + "startJunction", startJunction.c_str() );
	sim.reports.put(basePath + "endJunction", endJunction.c_str() );
	sim.reports.put(basePath + "cars", carString);

	return;
}

