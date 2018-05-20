import java.util.Comparator;

/*
 */
public class PointComparatorY implements Comparator<Point> {

    public int compare(Point point1, Point point2) {
        int yDiff = point1.getY() - point2.getY();

        if (yDiff == 0) {
            return point1.getX() - point2.getX();
        }
        return yDiff;
    }
}
