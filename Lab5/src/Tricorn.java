import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    /** 
     * Maximum number of iterations before declaring a point in the
     * Tricorn set.
     **/
    public static final int MAX_ITERATIONS = 2000;
    
    /** Specify an interesting region of the complex plane for the fractal. **/
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }
    
    /**
     *  This function computes the number of iterations needed
     * for c = a + bi to escape (ie, have magnitude 2 or greater).
     * If the point does not escape within MAX_ITERATIONS, -1 is returned
     * to indicate as such. For the Tricorn set, the iterating function is
     * z_n = conjugate(z_{n-1})^2 + c.
     **/
    public int numIterations(double a, double b) {
        /** The square of the magnitude of c. **/
        double magSq;
        /** The real part of z_i. **/
        double re = a;
        /** The imaginary part of z_i. **/
        double im = b;
        /** The real part of z_{i+1}. **/
        double nextRe;
        /** The imaginary part of z_{i+1}. **/
        double nextIm;
        /** A variable for counting iterations. **/
        int i = 0;
        
        while (i < MAX_ITERATIONS) {
            i += 1;
            nextRe = a + re * re - im * im;
            nextIm = b + -2 * re * im;
            re = nextRe;
            im = nextIm;
            magSq = re * re + im * im;
            if (magSq > 4) {
                return i;
            }
        }
        return -1;
    }
    
    public String toString() {
    	return "Tricorn";
    }
}