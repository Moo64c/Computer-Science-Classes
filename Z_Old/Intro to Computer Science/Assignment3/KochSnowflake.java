/**
 * @file KochSnowflake class.
 * Draw a KochSnowflake
 *
 * @author Amir Arbel
 */
public class KochSnowflake {
    Pixel upperLeftCorner;
    Pixel upperRightCorner;
    Pixel bottomCorner;
    int depth;

    /**
     * Create a base triangle by the definitions given in the exercise.
     */
    public KochSnowflake() {
        double height = Painter.getFrameHeight();
        double width = Painter.getFrameWidth();
        Pixel center = new Pixel(width / 2, height / 2);
        depth = 5;

        // Create corners.
        bottomCorner = new Pixel(width / 2, 40);
        upperRightCorner = new Pixel(width / 2, 40);
        upperRightCorner.rotateRelativeToPixel(center, Math.PI * 2 / 3);
        upperLeftCorner = new Pixel(width / 2, 40);
        upperLeftCorner.rotateRelativeToPixel(center, Math.PI * 4 / 3);
    }


    /**
     * Draw the KochSnowflake.
     */
    public void draw() {
        Painter.setColor("blue");
        new KochCurve(upperLeftCorner,  upperRightCorner, depth).draw();
        new KochCurve(upperRightCorner, bottomCorner,     depth).draw();
        new KochCurve(bottomCorner,     upperLeftCorner,  depth).draw();
    }
}
