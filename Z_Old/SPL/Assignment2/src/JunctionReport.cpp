#include <JunctionReport.h>
#include "boost/lexical_cast.hpp"
using namespace std;

const string JunctionReport::JUNCTION_REPORT_TYPE("junction_report");

JunctionReport::JunctionReport(Simulator & _sim,  const string &_reportId, const string &_junctionId, int time):
		Report(_sim, _reportId, JUNCTION_REPORT_TYPE, time), junctionId(_junctionId)
{
}


void JunctionReport::printData()
{
	cout<< "junction_report id " << this->getId().c_str() << " junction " << this->junctionId.c_str()
			<< " time " << this->time << endl;
}

void JunctionReport::writeReport()
{
	// Find the relevant junction.
	Junction* reportedJunction = sim.findJunctionByName(junctionId);

	if (reportedJunction == 0) {
		// Junction not found.
		cout << "Report error from junction: " << junctionId << " not found." << endl;
		return;
	}

	vector<int> timeSlices = reportedJunction->getTimeSlices();
	string timeSliceString = "";

	// Create timeSliceString.
	for (int i=0; i < (int) timeSlices.size(); i++) {
		timeSliceString += "(" + boost::lexical_cast<string>(timeSlices.at(i)) + ",";
		if (reportedJunction->hasGreenLight(i)) {
			timeSliceString += reportedJunction->getGreenLightTimeLeft() + ")";
		}
		else {
			timeSliceString += "-1)";
		}
	}

	// JID -> junction string


	map<string,string > incomingJunctionStrings;
	vector<Road *> junctionRoads = reportedJunction->getIncomingRoads();
	for(int j = 0; j < (int) junctionRoads.size() ; j++) {
		string junctionString = "";


		vector<Car *> roadCars = junctionRoads[j]->getCarsOnRoad();
		for (int k = 0; k < (int) roadCars.size(); k++) {
			junctionString += boost::lexical_cast<string>("(") + roadCars[k]->getId() + boost::lexical_cast<string>(")");
		}
		incomingJunctionStrings.insert(pair<string, string>(junctionRoads[j]->getStart(), junctionString));
	}

	string basePath = getId() + (string) ".";
	sim.reports.put(basePath + "junctionId", junctionId.c_str() );
	sim.reports.put(basePath + "timeSlices", timeSliceString);

	for(map<string, string>::const_iterator it = incomingJunctionStrings.begin(); it != incomingJunctionStrings.end(); it++) {
		sim.reports.put(basePath + it->first,  it->second);
	}

	return;
}
