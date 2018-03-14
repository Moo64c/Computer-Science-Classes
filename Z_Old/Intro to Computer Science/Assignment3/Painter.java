
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
/*
 * open a frame named aShape and drew the given shape 
 */

public class Painter extends Component {
        
    private static final long serialVersionUID = 1L;
    private static int SIZE = 600;
    private static Painter painter;
    private static Graphics g;
    private static String shape = null;
    
    // Create a frame and display it
    public static void draw(String aShape) {
        shape = aShape;        
    	JFrame frame = new JFrame(shape);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        painter =  new Painter();
        frame.add(painter, null);
        frame.pack();
        frame.setVisible(true);
    }
    
    // returns the Frame's width
    public static int getFrameWidth () {
        return painter.getSize().width;
    }

    // returns the Frame's height
    public static int getFrameHeight () {
        return painter.getSize().height;
    }
    
    // changes the color of the lines to be drawn
    public static void setColor (String color) {
    	if (color.equals("red")){
			g.setColor(Color.red);
    	}			
		else if (color.equals("blue")){
			g.setColor(Color.blue);  
		}
		else if (color.equals("green")){
			g.setColor(Color.green);  
		}       
    }
   
    public static void drawLine (Pixel p1, Pixel p2) {
    	drawLine((int)Math.round(p1.getX()),(int)Math.round(p1.getY()),(int)Math.round(p2.getX()),(int)Math.round(p2.getY()));
    	
    }
    
    // Draw a line on the frame
    public static void drawLine (int x1, int y1, int x2, int y2) {
        g.drawLine(x1, getFrameHeight()-y1, x2, getFrameHeight()-y2);
        
    }
    
    // Set the default size of the window frame to SIZE*SIZE pixels
    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }

    // paint the frame - draw the shape given (call the draw method in that shape object)
    public void paint(Graphics g) {
        Painter.g = g;
        try{
        	Object myShape = (Class.forName(shape)).newInstance();
        	Object [] objs = null;
        	Class [] classes = null;
        	(Class.forName(shape)).getMethod("draw", classes).invoke(myShape, objs);
        }
        catch(Exception e)
        {
        	System.out.println("Can't handle shape " + shape);
        	System.out.println(e.toString());
        	System.out.println(e.getCause());
        	
        }
        
        
        
     }
    
}