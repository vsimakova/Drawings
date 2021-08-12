package drawings;

import java.io.Serializable;

/**
 * Represents a single shape, represented as a polygon built on a collection of points
 * 
 * @author             Viktoryia Simakova
 * @version            2020-07-12
 */
public class Shape implements Serializable {

    /** the name of the shape, e.g., "circle" */
    private String name;
    /** the points that make up the shape */
    private ArrayList<Point> points;

    /**
     * Constructor
     *
     * @param       name        the name of the shape; must not be null or empty
     */
    public Shape(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Shape names must not be null or empty");
        }
        this.name = name;
        points = new ArrayList<Point>();
    }

    /**
     * Adds a new point to this shape
     *
     * @param       point       point to be added; must not be null
     */
    public void addPoint(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("point cannot not be null");
        }
        points.add(point);
    }

    /**
     * Retrieves the number of points in the collection
     *
     * @return      the count of points in this shape
     */
    public int getPointCount() {
        return points.size();
    }

    /**
     * Retrieves a specific point by index
     *
     * @param       idx         the index of the point
     * @return                  the point at the specified index
     */
    public Point getPoint(int idx) {
        return points.get(idx);
    }

    /**
     * Retrieves the name of the shape
     *
     * @return                  the shape's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves a string representation of the shape and its points
     *
     * @return                  string representation of the shape
     */
    public String toString() {
        String result = "Shape name : " + name + "\n" + "points:\n";
        for (int idx = 0; idx < points.size(); idx++){
            result += getPoint(idx) + "\n";
        }
        return result;
    }
}
