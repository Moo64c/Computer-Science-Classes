/*
the class example draw a line. 
*/
public class example {

	
	public void draw(){
		int width = Painter.getFrameHeight()/2;	// find the x coordinate of the center of the frame
		int height = Painter.getFrameWidth()/2; // find the y coordinate of the center of the frame
		int maxRadius = Math.min(width, height)/2;
		// change the color of the line to be drawn to red
		Painter.setColor("red");
		// draw a line from (width, height) to (width+maxRadius, height+maxRadius)
		Painter.drawLine(width, height, width+maxRadius, height+maxRadius);
	}
	
}
