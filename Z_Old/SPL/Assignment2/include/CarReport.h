#ifndef CARREPORT_H_
#define CARREPORT_H_

#include <iostream>
#include <string>
#include "Report.h"
#include "Car.h"
#include <map>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>

using boost::property_tree::ptree;

using namespace std;

class CarReport: public Report
{


public:
	CarReport(Simulator & sim, const string &_id, const string &_carId, int time);
	~CarReport(){};

	void printData();
	void writeReport();

	static const string CAR_REPORT_TYPE;


private:
	string carId;

};

#endif
