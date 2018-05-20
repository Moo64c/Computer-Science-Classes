/**
 * @file Snowflake
 *
 * @author Amir Arbel
 * Creates a Snowflake and draws it on screen.
 */
public class Snowflake {
    private BasicStar basicStar;
    private int depth;

    /**
     * Basic implementation. Defaults - Depth 3, default BasicStar.
     */
    public Snowflake() {
        basicStar = new BasicStar();
        depth = 3;
    }

    /**
     * Create a snowflake at a specific center to a specific depth.
     */
    public Snowflake(BasicStar basicStar, int depth) {
        this.basicStar = basicStar;
        this.depth = depth;
    }
    /**
     * Get the original BasicStar.
     *
     * @return
     *  BasicStar.
     */
    public BasicStar getBasicStar() {
        return basicStar;
    }

    /**
     * Get the center point of the snowflake.
     *
     * @return
     *  Center Pixel of the basicStar.
     */
    public Pixel getCenter() {
        return getBasicStar().getCenter();
    }


    /**
     * Get the radius of the original BasicStar.
     *
     * @return
     *  Set radius.
     */
    public double getRadius() {
        return getBasicStar().getRadius();
    }

    /**
     * Get the depth of the snowflake.
     *
     * @return
     *  Depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Draw the Snowflake.
     */
    public void draw() {
        draw(getBasicStar(), getDepth());
    }

    /**
     * Draw the Snowflake.
     */
	public void draw(BasicStar basicStar, int depth) {
        Pixel center = basicStar.getCenter();
        double radius = basicStar.getRadius();
        double sixthOfCircle = Math.PI / 3;

        // Create target pixel.
        Pixel targetPixel = new Pixel(center.getX(), center.getY() + radius);
        // Rotate it to start at 1/12 of a circle.
        targetPixel.rotateRelativeToPixel(center, sixthOfCircle / 2);

        for (int actionNumber = 0; actionNumber < 6; actionNumber++) {
            // Rotate the target pixel relative to the center of the start.
            targetPixel.rotateRelativeToPixel(center, sixthOfCircle);
            // Draw another Snowflake at this tip.
            BasicStar basicStarAtEdge = new BasicStar(targetPixel, radius / 3);
            basicStarAtEdge.draw();

            if (depth > 0) {
                // Move to the next depth and draw this snowflake as well.
                draw(basicStarAtEdge, depth - 1);
            }
        }

        // Draw original BasicStar lines.
        basicStar.draw();
	}
}
