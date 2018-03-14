#ifndef CAR_H_
#define CAR_H_

#include <SimulatorObject.h>
#include <Junction.h>
#include <vector>
#include <string>
#include <Road.h>

class Simulator;

using namespace std;

class Road;


class Car : public SimulatorObject
{
public:
	Car();
	Car(const Car &other);
	Car(Simulator & sim, const string &_id, const vector<Road*> &_path);
	~Car();

	Road* getRoad() const;
	Road* getNextRoad();
	int getFaultTime() const;
	void setPosition(double speed);
	double getPosition() const;
	void setFaultTime(int time);
	void advanceToNextRoad();
	void saveHistory();
	string getHistory();
	bool operator() (Car * one, Car * other);
	bool getFinished() const;

	Car& operator= (const Car &_other);

	static bool compareCarPtr(const Car * one, const Car * other);


private:
	bool finished;
	vector<Road *> path;
	Road *_road;
	int faultTime;
	double position;
	int positionInPath; // where we are in the vector.
	vector<string> history;
};




#endif /* CAR_H_ */
