import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lab1 {
   
	/**
     * Obtains one double-precision floating point number from
     * standard input.
     *
     * @return return the inputted double, or 0 on error.
     */
    public static double getDouble() {

        // There's potential for the input operation to "fail"; hard with a
        // keyboard, though!
        try {
            // Set up a reader tied to standard input.
            BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

            // Read in a whole line of text.
            String s = br.readLine();

            // Conversion is more likely to fail, of course, if there's a typo.
            try {
                double d = Double.parseDouble(s);

                //Return the inputted double.
                return d;
            }
            catch (NumberFormatException e) {
                // Bail with a 0.  (Simple solution for now.)
                return 0.0;
            }
        }
        catch (IOException e) {
            // This should never happen with the keyboard, but we'll
            // conform to the other exception case and return 0 here,
            // too.
            return 0.0;
        }
    }
    
    private static double computeArea(Point3d p1, Point3d p2, Point3d p3) {
    	// Compute the contained area using Heron's formula
    	double sideA = p1.distanceTo(p2);
    	double sideB = p2.distanceTo(p3);
    	double sideC = p1.distanceTo(p3);
    	double s = 0.5 * (sideA + sideB + sideC);
    	return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC));
    }
    
    public static void main(String[] args) {
    	// Prompt for three points
    	System.out.println("Input Point 1's coordinates one at a time:");
    	double point1X = getDouble();
    	double point1Y = getDouble();
    	double point1Z = getDouble();
    	
    	System.out.println("Input Point 2's coordinates one at a time:");
    	double point2X = getDouble();
    	double point2Y = getDouble();
    	double point2Z = getDouble();
    	
    	System.out.println("Input Point 3's coordinates one at a time:");
    	double point3X = getDouble();
    	double point3Y = getDouble();
    	double point3Z = getDouble();
    	
    	// Instantiate the points
    	Point3d point1 = new Point3d(point1X, point1Y, point1Z);
    	Point3d point2 = new Point3d(point2X, point2Y, point2Z);
    	Point3d point3 = new Point3d(point3X, point3Y, point3Z);
    	
    	// If two of the points are equal, bail
    	if (point1.equals(point2) || point2.equals(point3) || point1.equals(point3)) {
    		System.out.println("Two points are equal; not computing area");
    		return;
    	}
    	
    	// Otherwise print out the area of the triangle
    	else {
    		String output = String.format("The area is %f", computeArea(point1, point2, point3));
    		System.out.println(output);
    	}
    	
    }
}
