#include "../include/road.h"
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <stdlib.h>
#include <sstream>

using namespace std;

int main() {
	// Hold information about junctions.
	vector<int> appearances;
	// Hold information about the roads.
	vector<road> *roads = new vector<road>();
	
	// Read roads file.
	readRoadFile("Roads.conf", roads);
	// Read routes file.
	readRoutesFile("Routes.conf", roads);
	// Write output file.
	writeDataToFile("RoadStress.out", roads);
	// Done! :)
	return 0;
}

void readRoadFile(string fileName, vector<road> *roads) {
	ifstream roadFile(fileName.c_str());
	string line;
	while(getline(roadFile, line)) {
		vector<string> parsedLine;
		parsedLine = splitString(line, ",");
		// Structure: [0], [1] - junctions
		// [2] - length.
		road lineRoad;
		lineRoad.from = parsedLine[0];
		lineRoad.to = parsedLine[1];
		lineRoad.length = atof(parsedLine[2].c_str()); 
		lineRoad.stress = 0;
		lineRoad.appearances = 0;
		roads->push_back(lineRoad);
	}
}

void readRoutesFile(string fileName, vector<road> *roads) {
	ifstream roadFile(fileName.c_str());
	string line;
	
	// Calculate appearances.
	while(getline(roadFile, line)) {
		vector<string> parsedLine;
		parsedLine = splitString(line, ",");
		// Structure: junctionI_1, junctionI_2, ..., junctionI_n
		
		for(int i = 1; i< (int) parsedLine.size(); i++) {
			addAppearance(roads, parsedLine[i-1], parsedLine[i]);
		}
	}
	
	// Calculate stress.
	for (int i =0; i< (int) roads->size(); i++) {
		roads->at(i).stress = roads->at(i).appearances / roads->at(i).length;
	}
}

void writeDataToFile(string filename, vector<road> * roadData)
{
    ofstream stressFile(filename.c_str());
    for (int i = 0; i < (int) roadData->size(); i++) {
        stringstream out;
        out << roadData->at(i).from.c_str() << "," << roadData->at(i).to.c_str() << "," << roadData->at(i).stress << "\n";
        stressFile.write(out.str().c_str(), out.str().length());
    }
    stressFile.close();
}


void addAppearance(vector<road> *roads, string from, string to) {
	bool flag = false;
	for(int i=0; !flag && i < (int) roads->size(); i++) {
		if (roads->at(i).from == from && roads->at(i).to == to) {
			roads->at(i).appearances++;
			flag = true;
		}
	}
}


vector<string> splitString(string heap, string needle) {
	int start = 0;
	int foundAt = 0;
	vector<string> strings;
	string found;
	while (heap.find(needle, start) != string::npos) {
		foundAt = heap.find(needle, start);
		found = heap.substr(start, foundAt-needle.length() - start + 1);
		strings.push_back(found);
		start=foundAt+needle.length();
	}
	
  if (start != (int) heap.size()) {
		// Add last word.
		found = heap.substr(start);
		strings.push_back(found);
	}
	
	return strings;
}
