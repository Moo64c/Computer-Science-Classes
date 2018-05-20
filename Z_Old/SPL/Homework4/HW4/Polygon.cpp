#include "Point.h"
#include "Polygon.h"
#include <iostream>

using std::cout;
using std::endl;
using std::vector;


Polygon::Polygon() {}

/**
 *default constructor
 */
Polygon::Polygon(const int c) {
	color = c;
	std:cout << "enter x, y coordinates, 'fi' to cancel" << endl;
	std::string input1, input2;
	input1 << std::cin;
	input1 << std::cin;
	while (input1 != "fi" && input2 != "fi") {
		addPoint(new Point((int)input1,(int)input2));
		std:cout << "enter x, y coordinates, 'fi' to stop" << endl;
		input1 << std::cin;
		input1 << std::cin;
	}
}
Polygon::~Polygon() {
	vector<Point*>::iterator iter = _points.begin();
	cout << "DELETING POLYGON: BEGIN" << endl;
	while (iter != _points.end()) {
		cout << "  deleting point: ";
		cout<<(*iter)->getX()<<" ";
		cout<<(*iter)->getY()<<" ";
		cout << endl;
		delete (*iter);
		iter++;
	}
	cout << "DELETING POLYGON: END" << endl;
}

int Polygon::getNumOfPoints() {
	return _points.size();
}


Polygon::Polygon(Polygon& p) {
	int size = p.getNumOfPoints();
	for (int i = 0; i < size; i++ ) {
		Point* newPoint = p.getPoint(i);
		addPoint(newPoint);
	}
}

void Polygon::addPoint(Point* p) {
	Point* newp = new Point; //create a copy of the original pt
	newp->setX(p->getX());
	newp->setY(p->getY());
	_points.push_back(newp);
}

Point* Polygon::getPoint(int index) {
	return _points.at(index);
}


Polygon & Polygon::operator=(const Polygon &p) {
  if (this == &p) {
	return *this;
  }
  return *this;
}

int Polygon::getColor() {
	return color;
}
std::string Polygon::getType() {
	return "Polygon";
}
