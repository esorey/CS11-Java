import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/** This graphics widget is used in displaying our fractals. **/
public class JImageDisplay extends JComponent {
    /** This is an image we can manage. **/
    private BufferedImage img;
    
    /** This constructor sets up our image with the given width and height. **/
    public JImageDisplay(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dim = new Dimension(width, height);
        super.setPreferredSize(dim);        
    }
    
    /** Simple drawing code. Just display the image. **/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }
    
    /** Set every pixel in the image to black. **/
    public void clearImage() {
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.setRGB(i, j, 0);
            }
        }
    }
    
    /** Set a specific pixel to a given RGB value. **/
    public void drawPixel(int x, int y, int rgbColor) {
        img.setRGB(x, y, rgbColor);
    }
    
    
}
