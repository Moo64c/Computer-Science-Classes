#include "Polygon.cpp";

class Rectangle: public Polygon {

  public:
    int area ()	{
    	return width * height;
    }

    std::string getType() {
    	return "Rectangle";
    }

	private:
	  int width, height;
};
