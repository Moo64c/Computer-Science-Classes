/**
 * @file Pixel class.
 *
 * @author Amir Arbel
 * Define a dot in a two dimensional space.
 */
public class Pixel {
    private double x;
    private double y;

    /**
     * Create a pixel at 0,0;
     */
    public Pixel() {
        x = 0;
        y = 0;
    }

    /**
     * Create a pixel at x,y;
     *
     * @param x
     *   X coordinate.
     * @param y
     *   Y coordinate.
     */
    public Pixel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter function for y variable.
     *
     * @return
     *  Value of y.
     */
    public double getY() {
        return y;
    }

    /**
     * Getter function for x variable.
     *
     * @return
     *  Value of x.
     */
    public double getX() {
        return x;
    }

    /**
     * Translate the current pixel according to provided pixel.
     *
     * @param pixel
     *   Pixel to translate according to.
     */
    public void translate(Pixel pixel) {
        x = x + pixel.getX();
        y = y + pixel.getY();
    }

    /**
     * Rotate the pixel relatively to the axes origin (0,0).
     *
     * @param angle
     *  Angle to rotate by.
     */
    public void rotateRelativeToAxesOrigin(double angle) {
        // Use existing function.
        this.rotateRelativeToPixel(new Pixel(), angle);
    }

    /**
     * Rotate the pixel relatively to another pixel.
     *
     * @param pixel
     *   Pixel to rotate relatively to.
     * @param angle
     *   Angle to rotate by.
     */
    public void rotateRelativeToPixel(Pixel pixel, double angle) {
        // Hold the sin and cos values of the angle.
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        // We know only how to rotate according to the axes origin, so we'll
        // create a point with the same difference between the points as to the
        // axis origin, rotate it, and add the result to the pixel rotated by.
        Pixel relativePixel = new Pixel (x - pixel.getX(), y - pixel.getY());
        Pixel rotatedPixel = new Pixel(
                (relativePixel.getX() * cos) - (relativePixel.getY() * sin),  // x formula.
                (relativePixel.getX() * sin) + (relativePixel.getY() * cos)); // y formula.

        // Move it to the rotated location.
        x = x - relativePixel.getX() + rotatedPixel.getX();
        y = y - relativePixel.getY() + rotatedPixel.getY();
    }
}
