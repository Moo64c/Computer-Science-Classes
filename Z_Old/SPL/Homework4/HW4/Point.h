#ifndef __POINT_H
#define __POINT_H

/**
 *  THIS IS THE DECELERATION FILE OF CLASS POINT (Point.h)
 */
class Point {
public:
	/**
	 *default constructor initializes the point to (0,0)
	 */
	Point();
	/**
	 * constructor
	 * @param:	xval -x value
	 * 			yval -y value
	 */
	Point(double xval, double yval);
	/**
	 * _x+=dx
	 * _y+=dy
	 * @param: dx dy the value added to the point
	 */
	void move(double dx, double dy);
	/**
	 * getter for x value (notice const)
	 */
	double getX() const;
	/**
	 * getter for y value (notice const)
	 */
	double getY() const;
	/**
	 * setter for x value
	 */
	void setX(double x);
	/**
	 * setter for Y value
	 */
	void setY(double y);

private:
	/**
	 * _x value of the point (notice the under score
	 * for class memebrs)
	 */
	double _x;
	/**
	 * _y value of the point (notice the under score
	 * for class memebrs)
	 */
	double _y;
};
#endif
