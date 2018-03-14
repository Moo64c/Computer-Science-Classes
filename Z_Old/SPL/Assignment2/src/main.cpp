/*
 * main.cpp
 */

#include <boost/property_tree/ini_parser.hpp>
#include <boost/property_tree/ptree.hpp>
#include <boost/lexical_cast.hpp>
#include <iostream>

#include "Simulator.h"

using namespace std;

int main()
{
	//cout << "Beginning simulation." << endl;
	Simulator s;
	s.startSimulation();
	//cout << "Simulation ended." << endl;
	return 0;

}

