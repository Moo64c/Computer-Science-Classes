#ifndef REPORT_H_
#define REPORT_H_

class Simulator;

#include <iostream>
#include <string>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>
#include "SimulatorObject.h"


using boost::property_tree::ptree;
using namespace std;

class Report : public SimulatorObject {
public:
	virtual ~Report() {};
	virtual void writeReport()=0;
	virtual void printData()=0;

	static bool compareReportPointers(Report * _one, Report * _two);
	int getReportTime();


protected:
	Report(Simulator & sim, const string &_reportId, const string &_type, int _time);


	string type;
	int time;
	ptree report;

private:
};

#endif
