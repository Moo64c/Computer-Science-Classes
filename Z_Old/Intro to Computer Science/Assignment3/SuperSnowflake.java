/**
 * @file SuperSnowflake
 *
 * @author Amir Arbel
 * Creates a SuperSnowflake and draws it on screen.
 */
public class SuperSnowflake {
    private Snowflake snowflake;
    private int depth;

    /**
     * Basic implementation. Defaults - Depth 3, default BasicStar.
     */
    public SuperSnowflake() {
        snowflake = new Snowflake();
        depth = 3;
    }

    /**
     * Create a snowflake at a specific center to a specific depth.
     */
    public SuperSnowflake(Snowflake snowflake, int depth) {
        this.snowflake = snowflake;
        this.depth = depth;
    }
    /**
     * Get the original BasicStar.
     *
     * @return
     *  BasicStar.
     */
    public BasicStar getBasicStar() {
        return snowflake.getBasicStar();
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
     * Get the depth of the SuperSnowflake.
     *
     * @return
     *  Depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Draw the SuperSnowflake.
     */
	public void draw() {
        double sixthOfCircle = Math.PI / 3;

        Pixel center = getCenter();
        double snowflakeRadius;
        double circleRadius = getRadius();

        for (int circleDepth = 1; circleDepth <= depth; circleDepth++) {
            snowflakeRadius = getRadius() / (2 * circleDepth);
            circleRadius = circleRadius + (snowflakeRadius * 3);

            // Create target pixel.
            Pixel targetPixel = new Pixel(center.getX(), center.getY() + circleRadius);
            // Rotate it to start at 1/12 of a circle.
            targetPixel.rotateRelativeToPixel(center, sixthOfCircle / 2);

            for (int actionNumber = 0; actionNumber < 6; actionNumber++) {
                // Rotate the target pixel relative to the center of the start.
                targetPixel.rotateRelativeToPixel(center, sixthOfCircle);
                // Draw another Snowflake at this tip.
                BasicStar basicStarAtEdge = new BasicStar(targetPixel, snowflakeRadius / 3);
                basicStarAtEdge.draw();

                if (depth > 0) {
                    // Move to the next depth and draw this snowflake as well.
                    new Snowflake(basicStarAtEdge, depth - 1).draw();
                }
            }
        }

        // Draw the original snowflake.
        snowflake.draw();
    }
}
