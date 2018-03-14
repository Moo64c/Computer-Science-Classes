import java.util.Scanner;

/*
 * the class main get from the user the shape he wish to draw,
 * and call the drew method of the desired shape .
 */
public class Main {

	
	public static void main(String[] args) {		
		
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("Please enter the number of the shape you wish to draw:\n" +
				" 1-example\n" +
				" 2-BasicStar\n" +
				" 3-Snowflake\n" +
				" 4-SuperSnowflake\n" +
				" 5-KochCurve\n" +
				" 6-KochSnowflake\n");
		int shape = sc.nextInt();

		// chooses which shape to draw based on the number received
		switch(shape){
		/*
		 *  An example given to you so you can see how the painted works.
		 *  This example opens a frame, and draws a red line.
		 */
		case 1:
			drawExample();
			break;
		case 2:
			drawBasicStar();
			break;
		case 3:
			drawSnowflake();
			break;
		case 4:
			drawSuperSnowflake();
			break;
		case 5:
			drawKochCurve();
			break;
		case 6:
			drawKochSnowflake();
			break;
		default: System.out.println("invalid shape");
		}
		
		sc.close();
	}
	
	// Draw the example line
	public static void drawExample(){
		Painter.draw("example");
	}

	// Draw a BasicStar
	public static void drawBasicStar(){
		Painter.draw("BasicStar");
	}

	// Draw a Snowflake
	public static void drawSnowflake(){
		Painter.draw("Snowflake");
	}
	
	// Draw a SuperSnowflake
	public static void drawSuperSnowflake(){
		Painter.draw("SuperSnowflake");
	}
	
	// Draw a KochCurve
	public static void drawKochCurve(){
		Painter.draw("KochCurve");
	}
	
	// Draw a KochSnowflake
	public static void drawKochSnowflake(){
		Painter.draw("KochSnowflake");
	}
	
}
