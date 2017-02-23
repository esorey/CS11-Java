/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }
    
    /** Checks for equality with another object. **/
    public boolean equals(Object o) {
    	if (o instanceof Location) {
    		Location l2 = (Location) o;
    		return xCoord == l2.xCoord && yCoord == l2.yCoord;
    	}
    	else
    		return false;
    }
    
    /** Generate a hashcode for the Location object. **/
    public int hashCode() {
    	int hash = 11;
    	hash = 31 * hash + xCoord;
    	hash = 31 * hash + yCoord;
    	return hash;
    }
}
