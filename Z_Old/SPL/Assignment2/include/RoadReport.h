#ifndef ROADREPORT_H_
#define ROADREPORT_H_

#include <iostream>
#include <string>
#include "Road.h"
#include "Report.h"
#include "Simulator.h"

using namespace std;

class RoadReport: public Report

{


public:
	RoadReport(Simulator & sim, const string &_id, const string &_startJunction, const string &_endJunction, int time);
	~RoadReport(){};
	
	void printData();
	void writeReport();

	static const string ROAD_REPORT_TYPE;


private:
	string startJunction;
	string endJunction;
};

#endif
