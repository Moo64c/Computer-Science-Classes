package assignment3;

/**
 * Holds position info in Euclidean coordinate system.
 */
public class Location {
    protected double x, y;

    public Location(double _x, double _y) {
        x = _x;
        y = _y;
    }

    /**
     * Gets the distance from the current Location to another Location.
     * @param other
     *   Other Location point.
     * @return
     *   Calculated distance.
     */
    public double calculateDistance(Location other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "Location:[" + x + ":" + y +"]";
    }
}
