/*
 * SimulatorObject.h
 *
 *  Created on: Nov 17, 2014
 *      Author: yotamd
 */

#ifndef SIMULATOROBJECT_H_
#define SIMULATOROBJECT_H_

using namespace std;

#include <string>
class Simulator;

class SimulatorObject {
public:
	SimulatorObject(Simulator & _sim, const string &id);
	virtual ~SimulatorObject(){};
	string getId() const;

protected:
	string _id;
	Simulator &sim;
};


#endif /* SIMULATOROBJECT_H_ */
