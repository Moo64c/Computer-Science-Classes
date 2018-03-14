/**
 * @file KochCurve class.
 *
 * @author Amir Arbel
 * Draw a KochCurve.
 */
public class KochCurve {
    private Pixel start;
    private Pixel end;
    private int depth;

    /**
     * Create the basic KochCurve.
     */
    public KochCurve() {

        // Place start in the middle of the frame.
        double height = Painter.getFrameHeight() / 2;
        double width = Painter.getFrameWidth() / 2;
        this.start = new Pixel(width, height);

        // Place end in the center upper quarter.
        this.end = new Pixel(width * 3 / 2, height * 3 / 2);

        depth = 4;
    }

    /**
     * Create a custom Koch curve.
     *
     * @param start
     *  Pixel to start at.
     * @param end
     *  Pixel to end at.
     * @param depth
     *  Depth of the KochCurve.
     */
    public KochCurve(Pixel start, Pixel end, int depth) {
        this.start = start;
        this.end = end;
        this.depth = depth;
    }

    /**
     * Get depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Draw KochCurve recursively.
     */
    public void draw() {
        draw(start, end, getDepth());
    }

    /**
     * Draw KochCurve recursively.
     */
    public void draw(Pixel start, Pixel end, int depth) {
        // Find two points to divide the segment to three equal ones.
        double yDifference = end.getY() - start.getY();
        double xDifference = end.getX() - start.getX();
        Pixel p1 = new Pixel(start.getX() + (xDifference / 3),     start.getY() + (yDifference / 3));
        Pixel p2 = new Pixel(start.getX() + (xDifference * 2 / 3), start.getY() + (yDifference * 2 / 3));

        // Find a point that will make the new points an equilateral triangle.
        Pixel p3 = new Pixel(p2.getX(), p2.getY());
        p3.rotateRelativeToPixel(p1, Math.PI / 3);

        if (depth > 0) {
            // Create curves between every two consecutive points.
            draw(start, p1, depth - 1);
            draw(p2, end, depth - 1);
            draw(p1, p3, depth - 1);
            draw(p3, p2, depth - 1);
        }
        else {
            // Draw the curve.
            Painter.drawLine(start, end);
        }
    }
}
