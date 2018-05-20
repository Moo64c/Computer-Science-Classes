#ifndef JUNCTIONREPORT_H_
#define JUNCTIONREPORT_H_

#include <iostream>
#include <string>
#include "Report.h"
#include "Junction.h"
#include "Simulator.h"

using namespace std;

class JunctionReport: public Report
{


public:
	JunctionReport(Simulator & sim, const string &_reportId, const string &_junctionId, int time);
	~JunctionReport(){};

	void printData();
	void writeReport();

	static const string JUNCTION_REPORT_TYPE;


private:
	string junctionId;

};

#endif
