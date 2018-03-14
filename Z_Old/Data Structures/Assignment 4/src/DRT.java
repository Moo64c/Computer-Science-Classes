
public interface DRT {
	
//	void build(Point[] points);
//
//	void buildSlow(Point[] points);
	
	int numOfPointsInRectangle(Point p1, Point p2);

	Point[] getPointsInRectangle(Point p1, Point p2);
	
	//O(logn). will be called up to logn times
	void addPoint(Point point); 

	//O(logn). will be called up to logn times
	boolean removePoint(Point point); 
	
	boolean existsPoint(Point point);
	
	int numOfPointsInHalfPlaneX(int X, boolean greaterThan);
	
	int numOfPointsInHalfPlaneY(int Y, boolean greaterThan);
	
	Point[] getAllPoints();

}
