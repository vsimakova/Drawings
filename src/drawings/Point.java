package drawings;

import java.io.Serializable;

/**
 * Represents a cartesian point with floating-point coordinates
 */
public class Point implements Serializable {
    
    /** the point's x coordinate */
    private double x;
    /** the point's y coordinate */
    private double y; 

    /**
     * Create a Point at (0, 0)
     */
    public Point() {
        x = 0;
        y = 0;
    }

    /**
     * Create a Point with the given coordinates
     * @param initialX  the initial x-coordinate
     * @param initialY  the initial y-coordinate
     */
    public Point(double initialX, double initialY) {
        x = initialX;
        y = initialY;
    }

    /**
     * Set the x-coordinate of this Point
     * @param updateX   the new x-coordinate
     */
    public void setX(double updateX) {
        x = updateX;
    }

    /**
     * Set the y-coordinate of this Point
     * @param newY  the new y-coordinate
     */
    public void setY(double newY) {
        y = newY;
    }

    /**
     * Set the x and y coordinates of this Point
     * @param newX  the new x-coordinate
     * @param newY  the new y-coordinate
     */
    public void setPoint(double newX, double newY){
        x = newX;
        y = newY;
    }

    /**
     * Get the x-coordinate of this Point
     * @return  the point's x-coordinate
     */
    public double getX() {
        return x;
    }

    /** 
     * Get the y-coordinate of this Point
     * @return the point's y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Calculate the distance between this Point and the origin
     * @return  the distance to (0, 0)
     */
    public double distanceToOrigin() {
        return Math.sqrt(x * x + y * y);
    }

    /** Calculate the distance between this Point and some other Point
     * @param other     the other Point
     * @return          the distance between this Point and the specified one
     */
    public double distance(Point other) {
        double diffX = x - other.x;
        double diffY = y - other.y;
        return  Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Find the midpoint between this Point and another Point
     * @param   other   the other Point
     * @return          the Point midway between this Point and the specified one
     */
    public Point midPoint(Point other) {
        double midX = (x + other.x) / 2;
        double midY = (y + other.y) / 2;
        return new Point(midX, midY);
    }

    /**
     * The String version of this Point
     * @return      a String representation of this Point
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Checks whether this Point is the same as a specified Object
     *
     * @param other     the Object to compare this Point to
     * @return          true if the Points are the same, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Point)) {
            return false;
        } else {
            Point otherPoint = (Point)other;
            if (this.x == otherPoint.x && this.y == otherPoint.y) {
                return true;
            } else {
                return false;
            }
        }
    }

}

