//Note: WE HAVE NOT IMPLEMENTED IN THIS
//CLASS A COPY CONSTRUCTOR AND OPERATOR=
#ifndef __POLYGON_H
#define __POLYGON_H

#include "Point.h"
#include <vector>
#include <string>

class Polygon {
public:
	/**
	 *default constructor
	 */
	Polygon();
	/**
	 *default constructor
	 */
	Polygon(int color);
	/**
	 *copy constructor
	 */
	Polygon(Polygon& p);
	/*
	 * Destructor- notice the "deep" delete
	 */
	virtual ~Polygon();
	/**
	 * Adds point p to _points
	 * @param: p the point to be added
	 */
	void addPoint(Point* p);
	/**
	 * @return a pointer to the point at _points(index)
	 */
	Point* getPoint(int index);
	/**
	 * @return the size of _points
	 */
	int getNumOfPoints();
	/*
	 * Polygon = operator.
	 */
	Polygon& operator=(const Polygon &p);

	int getColor();
	virtual std::string getType();
private:
	/**
	 * The actual vector the points are held in.
	 */
	std::vector<Point*> _points;
	const int color;
	std::string type;
};

#endif
