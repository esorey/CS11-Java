/**
 * A three-dimensional point class.
 */
public class Point3d {
    
    /** X coordinate of the point */
    private double xCoord;
    
    /** Y coordinate of the point */
    private double yCoord;
    
    /** Z coordinate of the point */
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. */
    public Point3d(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. */
    public Point3d() {
        // Call three-argument constructor and specify the origin.
        this(0, 0, 0);
    }
    
    /** Compare two points for equality. */
    public boolean equals(Object o) {
    	if (o.getClass() == Point3d.class) {
    		Point3d pt = (Point3d) o;
        	return (xCoord == pt.getX()) && (yCoord == pt.getY()) && (zCoord == pt.getZ());
    	}
    	else 
    		return false;
    }
    
    /** Compute the straight-line distance between two points. */
    public double distanceTo(Point3d pt) {
    	double deltaX = xCoord - pt.getX();
    	double deltaY = yCoord - pt.getY();
    	double deltaZ = zCoord - pt.getZ();
    	return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    /** Return the X coordinate of the point. */
    public double getX() {
        return xCoord;
    }

    /** Return the Y coordinate of the point. */
    public double getY() {
        return yCoord;
    }
    
    /** Return the Z coordinate of the point. */
    public double getZ() {
        return zCoord;
    }

    /** Set the X coordinate of the point. */
    public void setX(double val) {
        xCoord = val;
    }

    /** Set the Y coordinate of the point. */
    public void setY(double val) {
        yCoord = val;
    }
    
    /** Set the Z coordinate of the point. */
    public void setZ(double val) {
        zCoord = val;
    }
}