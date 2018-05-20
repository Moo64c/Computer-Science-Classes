#include "SimulatorObject.h"
#include <string>

using namespace std;

SimulatorObject::SimulatorObject(Simulator & _sim, const string &id):
		_id(id), sim(_sim) {
}
string SimulatorObject::getId() const {
	return this->_id;
}
