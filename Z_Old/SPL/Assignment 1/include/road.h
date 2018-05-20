#ifndef road_H_
#define road_H_
#include <vector>
#include <string>

using namespace std;

struct road {
	string from, to;
	double length, stress;
	int appearances;
};

void readRoadFile(string fileName, vector<road> *roads);
void readRoutesFile(string fileName, vector<road> *roads);
vector<string> splitString(string heap, string needle);
void writeDataToFile(string filename, vector<road> * roadData);
void addAppearance(vector<road> *roads, string from, string to);

#endif 
