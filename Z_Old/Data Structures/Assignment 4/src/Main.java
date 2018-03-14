import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;



public class Main {

	public static void main(String[] args) {
//        testOneD();
		testA();
		testB();
		testC();
	}

    public static void printPointArray(Point[] points) {
        System.out.println("Size: " + points.length);
        for (int i=0; i < points.length; i++) {
            System.out.println(i+"(" + points[i].getX() + ", " + points[i].getY() + ")");
        }
    }

    private static void testOneD() {
        Point[] points = readPointsFile("72_Hidden_Bands.txt");
        OneDRangeTree odrt = new OneDRangeTree(points);
        odrt.addPoint(new Point(10,10));
        odrt.addPoint(new Point(130,100));
        odrt.addPoint(new Point(70, 268));
        System.out.println(odrt.removePoint(new Point(70, 268)));
        System.out.println(odrt.removePoint(new Point(70, 268)));
        System.out.println(odrt.removePoint(new Point(70,268)));
    }

	private static void testA()
	{

		Point[] points = {
				new Point(3, 0),
				new Point(5, 5),
				new Point(4, 1),
				new Point(1, 5),
				};

		DRT t = new TwoDRangeTree(points);

		String testName;
		int expected;
		int result;

		testName = "A0";
		testExpectedPoints(testName, points, t.getAllPoints());

		Point p1 = new Point(2, 1);
		Point p2 = new Point(4, 2);
		testName = "A1";
		expected = 1;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		p1 = new Point(2, 0);
		p2 = new Point(4, 4);
		testName = "A2";
		expected = 2;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		Point point = new Point(4,0);
		t.addPoint(point);

		p1 = new Point(0, 0);
		p2 = new Point(5, 5);
		testName = "A3";
		expected = 5;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		p1 = new Point(2, 2);
		p2 = new Point(5, 4);
		testName = "A4";
		expected = 0;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		t.removePoint(point);

		p1 = new Point(0, 0);
		p2 = new Point(1, 0);
		testName = "A5";
		expected = 0;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		p1 = new Point(0, 5);
		p2 = new Point(6, 5);
		testName = "A6";
		expected = 2;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);


		testName = "A7";
		expected = 3;
		result = t.numOfPointsInHalfPlaneX(2, true);
		testExpected(testName, expected, result);

		t.addPoint(point);
		point = new Point(3,2);
		t.addPoint(point);
		point = new Point(7,1);
		t.addPoint(point);

		testName = "A8";
		expected = 6;
		result = t.numOfPointsInHalfPlaneX(3, true);
		testExpected(testName, expected, result);

		testName = "A9";
		expected = 2;
		result = t.numOfPointsInHalfPlaneY(3, true);
		testExpected(testName, expected, result);

		testName = "A10";
		expected = 5;
		result = t.numOfPointsInHalfPlaneY(3, false);
		testExpected(testName, expected, result);

		testName = "A11";
		p1 = new Point(0, 5);
		p2 = new Point(6, 5);
		Point[] expectedPoints1=
				{
				new Point(5,5),
				new Point(1,5),
				};
		testExpectedPoints(testName, expectedPoints1, t.getPointsInRectangle(p1, p2));

		testName = "A12";
		p1 = new Point(0, 0);
		p2 = new Point(5, 5);

		Point[] expectedPoints2=
			{
			new Point(3,0),
			new Point(4,1),
			new Point(1,5),
			new Point(4,0),
			new Point(5,5),
			new Point(3,2),
			};
		testExpectedPoints(testName, expectedPoints2, t.getPointsInRectangle(p1, p2));

		testName = "A13";
		p1 = new Point(2, 0);
		p2 = new Point(4, 4);
		Point[] expectedPoints3=
		{
			new Point(3,0),
			new Point(4,1),
			new Point(4,0),
			new Point(3,2),
		};
		testExpectedPoints(testName, expectedPoints3, t.getPointsInRectangle(p1, p2));

	}

	private static void testB()
	{

        System.out.println("========================================");

		final int NUM = 100;

		Point[] points = new Point[NUM];

		for (int i=0; i<NUM; ++i)
		{
			points[i] = new Point(i, NUM-i);
		}

		DRT t = new TwoDRangeTree(points);

		String testName;
		int expected;
		int result;


		Point p1 = new Point(NUM/2-1, NUM/2-1);
		Point p2 = new Point(NUM/2+1, NUM/2+1);
		testName = "B1";
		expected = 3;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);


		p1 = new Point(2, 0);
		p2 = new Point(4,100);
		testName = "B2";
		expected = 3;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

//        printXTree((TwoDRangeTree)t);
		p1 = new Point(0, 0);
		p2 = new Point(5, 5);
		testName = "B3";
		expected = 0;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);


		p1 = new Point(2, 2);
		p2 = new Point(5, 95);
		testName = "B4";
		expected = 1;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		t.addPoint(new Point(1,0));
		p1 = new Point(0, 0);
		p2 = new Point(1, 0);
		testName = "B5";
		expected = 1;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		t.addPoint(new Point(1,1));
		t.addPoint(new Point(2,1));
		t.addPoint(new Point(3,3));
		t.removePoint(new Point(1,0));

		p1 = new Point(0, 0);
		p2 = new Point(6, 5);
		testName = "B6";
		expected = 3;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);
	}



	private static void testC()
	{

		System.out.println("========================================");


		Point[] points = readPointsFile("US and Canada cities.txt");

		if (points.length == 0) {
			return;
		}

		DRT t = new TwoDRangeTree(points);


		String testName;
		int expected;
		int result;




		Point p1 = new Point(20,13);
		Point p2 = new Point(55,40);
		testName = "C1";
		expected = 13;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		p1 = new Point(50,1);
		p2 = new Point(100,50);
		testName = "C2";
		expected = 82;                
                result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);

		p1 = new Point(100,100);
		p2 = new Point(200,200);
		testName = "C3";
		expected = 0;
		result = t.numOfPointsInRectangle(p1, p2);
		testExpected(testName, expected, result);


	}


	private static <T> void testExpected(String testName, T expected, T actual)
	{
		if (!actual.equals(expected)) {
			System.out.println("Test " +testName+ ": wrong! expected=" + expected + ", actual=" + actual);
		} else {
			System.out.println("Test " +testName+ ": passed :)");
		}

	}

	private static void testExpectedPoints(String testName, Point[] expectedPoints,	Point[] actualPoints)
	{
		if (!equalAsSets(actualPoints,expectedPoints)) {
			System.out.println("Test " +testName+ ": wrong! expected=" + Arrays.toString(expectedPoints) + ", actual=" + Arrays.toString(actualPoints));
		} else {
			System.out.println("Test " +testName+ ": passed :)");
		}
	}

	//checks (unefficiently) that the sets are equal
	//NOTE: if one of the sets contains 2 points with same coordinates, might return erroneous result
	private static boolean equalAsSets(Point[] actualPoints, Point[] expectedPoints)
	{
		if (actualPoints.length!=expectedPoints.length) return false;
		for (int i=0;i<expectedPoints.length;i++)
		{
			boolean exists=false;
			for (int j=0;j<actualPoints.length;j++)
			{
				if (expectedPoints[i].equals(actualPoints[j])) exists=true;
			}
			if (!exists) return false;
		}
		return true;
	}


	private static Point[] readPointsFile(String fileName)
	{
		Vector<Point> points = new Vector<Point>();;
		BufferedReader input;
		FileReader fileReader;

		try {
			fileReader = new FileReader(fileName);
			input = new BufferedReader(fileReader);
			String line = null;
			String name;
			int x, y;

			while ((line = input.readLine()) != null) {
				String[] lineArray = line.split(";");
				name = lineArray[0];
				x = Integer.parseInt(lineArray[1]);
				y = Integer.parseInt(lineArray[2]);

				Point p = new Point(x, y, name);
				points.add(p);
			}
			input.close();
			fileReader.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Point[] result = new Point[points.size()];
		points.toArray(result);
		return result;
	}


    public static void printXTree(TwoDRangeTree t) {
        printXTree(t.root, 0);
    }

    public static void printXTree(TwoDNode root, int level){
        if (level ==0) {
            System.out.println("=========================");
        }
        if(root==null)
            return;
        printXTree(root.right, level + 1);
        if(level!=0){
            for(int i=0;i<level-1;i++)
                System.out.print("|\t");
            System.out.println("|-------"+root.data.toString());
        }
        else
            System.out.println(root.data.toString());
        printXTree(root.left, level + 1);
    }

    public static void printYTree(TwoDRangeTree t) {
        printYTree(t.root.yTree.root, 0);
    }
    public static void printYTree(OneDNode root, int level){
        if (level ==0) {
            System.out.println("=========================");
        }
        if(root==null)
            return;
        printYTree(root.right, level + 1);
        if(level!=0){
            for(int i=0;i<level-1;i++)
                System.out.print("|\t");
            System.out.println("|-------"+root.data.toString());
        }
        else
            System.out.println(root.data.toString());
        printYTree(root.left, level + 1);
    }

}
