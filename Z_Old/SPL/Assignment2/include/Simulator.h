#ifndef SIMULATOR_H_
#define SIMULATOR_H_

#include "boost/property_tree/ini_parser.hpp"
#include "boost/property_tree/ptree.hpp"
#include "boost/lexical_cast.hpp"
#include <Simulator.h>
#include <Junction.h>
#include <Road.h>
#include <Car.h>
#include <vector>
#include <string>
#include <Event.h>
#include <Report.h>

#include <CarArrivalEvent.h>
#include <CarFaultEvent.h>
#include <CarReport.h>
#include <JunctionReport.h>
#include <RoadReport.h>
#include <map>
using namespace std;
using boost::property_tree::ptree;

class Simulator {
public:

	Simulator();
	~Simulator();

	void timeTick();

	/// Brief start the simulation loop.
	void startSimulation();

	// Debug:
	void testReadConf();
	void testReadEvents();
	void testReadCommands();
	void testReadRoads();

	Junction* findJunctionByName(const string &_name) const;
	Road* findRoadByName (const string &_name) const;
	Car* findCarByName(const string &_name) const;
	int getTime() const;

	// Map the cars by road and then length in the road.
	vector<Car*> carMapVector;
	map<string, Car* > carMapByName;

	void addCar(Car *addedCar);

	ptree reports;
	void writeReports();

	double getMAX_SPEED();
	double getDEFAULT_TIME_SLICE();
	double getMAX_TIME_SLICE();
	double getMIN_TIME_SLICE();

	void forceAllReports(int time);
private:
	double MAX_SPEED;
	double DEFAULT_TIME_SLICE;
	double MAX_TIME_SLICE;
	double MIN_TIME_SLICE;
	int time;
	int terminationTime;
	int eventPosition;
	int commandPosition;
	vector<Junction *> junctions;
	vector<Road *> roads;
	vector<Event*> events;
	vector<Report*> commands;
	int lastReportIndex;

	void executeEvents();
	void moveCarsInRoads();
	void moveCarsInJunctions();

	void readConf(const string &_filename);
	void readEvents(const string &_filename);
	void readRoads(const string &_filename);
	void readCommands(const string &_filename);

	void terminate();

	void saveHistory();

	static const string CONF_FILENAME;
	static const string EVENT_FILENAME;
	static const string COMMAND_FILENAME;
	static const string ROAD_FILENAME;


};


#endif /* SIMULATOR_H_ */
