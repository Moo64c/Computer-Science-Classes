#include "Point.h"
#include "Polygon.h"
#include <iostream>

using std::cout;
using std::endl;

const int COUNT = 4;

int main() {
	Polygon* poly1 = new Polygon();
	for (int i=0; i<COUNT; i++) {
		Point p(i,2*i);
		poly1->addPoint(&p);
	}

	for (int i=0; i<COUNT; i++) {
			cout<<poly1->getPoint(i)->getX()<<" ";
			cout<<poly1->getPoint(i)->getY()<<endl;
	}

	delete poly1;

	return 0;
}
