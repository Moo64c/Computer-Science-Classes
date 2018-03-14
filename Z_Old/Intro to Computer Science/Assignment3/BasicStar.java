/**
 * @file: BasicStar
 *
 * @author Amir Arbel
 * Creates a BasicStar and draws it on screen.
 */
public class BasicStar {
    private Pixel center;
    private double radius;

    /**
     * Basic implementation.
     */
    public BasicStar() {
        double height = Painter.getFrameHeight() / 2;
        double width = Painter.getFrameWidth() / 2;
        this.center = new Pixel(width, height);
        double maxRadius = Math.min(width, height) / 2;
        this.radius = maxRadius / 4;
    }

    /**
     * Create a BasicStar from a specific location.
     *
     * @param center
     *  Pixel determining the center.
     * @param radius
     *  Radius of the BasicStar.
     */
    public BasicStar(Pixel center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Get the center point of the star.
     *
     * @return
     *  Center Pixel of the star.
     */
    public Pixel getCenter() {
        return this.center;
    }

    /**
     * Get the radius of the star.
     *
     * @return
     *  Set radius.
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * Draw the BasicStar.
     */
	public void draw() {
        Pixel center = this.getCenter();
        double radius = this.getRadius();
        double sixthOfCircle = Math.PI / 3;

        // Create target pixel & Rotate it to start at 1/12 of a circle.
        Pixel targetPixel = new Pixel(center.getX(), center.getY() + radius);
        targetPixel.rotateRelativeToPixel(center, sixthOfCircle / 2);

        for (int actionNumber = 0; actionNumber < 6; actionNumber++) {
            // Rotate the target pixel relative to the center of the start.
            targetPixel.rotateRelativeToPixel(center, sixthOfCircle);
            Painter.drawLine(center, targetPixel);
        }
	}
}
