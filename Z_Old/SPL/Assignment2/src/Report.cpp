#include "Report.h"
#include <string>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>
using boost::property_tree::ptree;

using namespace std;

Report::Report(Simulator & _sim, const string &_reportId, const string &_type, int _time):
		SimulatorObject(_sim, _reportId), type(_type), time(_time), report()
	{	}

bool Report::compareReportPointers(Report * _one, Report * _two)
{
	return _one->time < _two->time;
}

int Report::getReportTime() {
	return this->time;
}

