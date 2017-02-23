import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author esorey
 *
 * This class provides a GUI for displaying fractals. It also houses
 * a main function that runs the application.
 */
public class FractalExplorer {
    /** Side-length of our square display area. **/
    private int dispSize;
    /** Image area for our fractal. **/
    private JImageDisplay img;
    /** Used to generate fractals of a specified kind. **/
    private FractalGenerator fGen;
    /** The current viewing area in our image. **/
    private Rectangle2D.Double range;
    /** Various GUI components. **/
    JFrame frame;
    JButton resetButton;
    JButton saveButton;
    JLabel label;
    JComboBox<FractalGenerator> fractalCBox;
    JPanel cbPanel;
    JPanel buttonPanel;
    /** Number of rows still being drawn. **/
    int rowsRemaining;
    
    /** Basic constructor. Initializes the display image, fractal generator,
     * and initial viewing area.
     */
    public FractalExplorer(int dispSize) {
        this.dispSize = dispSize;
        this.fGen = new Mandelbrot();
        this.range = new Rectangle2D.Double(0, 0, 0, 0);
        fGen.getInitialRange(this.range);
        
    }
    
    /**
     * Sets up and displays the GUI.
     */
    public void createAndShowGUI() {
        /** Create the GUI components. **/
        frame = new JFrame("Fractal Explorer");
        img = new JImageDisplay(dispSize, dispSize);
        resetButton = new JButton("Reset Display");
        resetButton.setActionCommand("reset");
        saveButton = new JButton("Save Image");
        saveButton.setActionCommand("save");
        label = new JLabel("Fractal: ");
        fractalCBox	= new JComboBox<FractalGenerator>();
        cbPanel = new JPanel();
        cbPanel.add(label);
        cbPanel.add(fractalCBox);
        buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        fractalCBox.addItem(new Mandelbrot());
        fractalCBox.addItem(new BurningShip());
        fractalCBox.addItem(new Tricorn());
        
        /** Add listeners to components. **/
        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        resetButton.addActionListener(aHandler);
        saveButton.addActionListener(aHandler);
        img.addMouseListener(mHandler);
        fractalCBox.addActionListener(aHandler);
        
        /** Put all of the components into the Frame. **/
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(img, java.awt.BorderLayout.CENTER);
        frame.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        frame.add(cbPanel, java.awt.BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /** Display the image. **/
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    /** Enable or disable UI interactivity. **/
    public void enableUI(boolean val) {
    	saveButton.setEnabled(val);
    	resetButton.setEnabled(val);
    	fractalCBox.setEnabled(val);
    }
    
    /** Use the fractal generator to draw the fractal row by row. **/
    private void drawFractal() {
    	/** Disable UI interactivity during drawing. **/
    	enableUI(false);
    	// Draw the fractal line by line.
    	rowsRemaining = dispSize;
        for (int i = 0; i < dispSize; i++) {
            FractalWorker rowDrawer = new FractalWorker(i);
            rowDrawer.execute();
        }
    }
    
    /** Run the application. **/
    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(800);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
    
    /** 
     * Handler for resetting the camera, changing fractal type,
     *  and saving images 
     **/
    public class ActionHandler implements ActionListener {
    	/** Handle various actions. **/
        public void actionPerformed(ActionEvent e) {    
        	/** If the reset button is clicked, reset the camera view. **/
        	if (e.getActionCommand() == "reset") {
                fGen.getInitialRange(range);
                drawFractal();
        	}
        	/** If the save button is clicked, save the image. **/
        	else if (e.getActionCommand() == "save") {
        		JFileChooser fileChooser = new JFileChooser();
        		FileFilter filter 
        			= new FileNameExtensionFilter("PNG Images", "png");
        		fileChooser.setFileFilter(filter);
        		fileChooser.setAcceptAllFileFilterUsed(false);
        		int res = fileChooser.showSaveDialog(img);
        		
        		if (res == JFileChooser.APPROVE_OPTION) {
        			try {
						javax.imageio.ImageIO.write(img.getBufferedImage(),
								"png", fileChooser.getSelectedFile());
					} catch (NullPointerException | IOException e1) {
						javax.swing.JOptionPane.showMessageDialog(img,
								e1.getMessage(), "Cannot Save Image",
								JOptionPane.ERROR_MESSAGE);
					}
        		}
        		else {
        			return;
        		}
        	}
        	/** If the combobox is clicked, change fractal types. **/
        	else if (e.getSource() == (Object) fractalCBox) {
        		fGen = (FractalGenerator) fractalCBox.getSelectedItem();
                fGen.getInitialRange(range);
                drawFractal();
        	}
        }
    }
    
    
    /** Simple handler to zoom in on the clicked pixel. **/
    class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
        	// If rows are being drawn, ignore mouse input.
        	if (rowsRemaining != 0) {
        		return;
        	}
            double xCoord = FractalGenerator.getCoord(range.x, 
                    range.x + range.width, dispSize, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, 
                    range.y + range.width, dispSize, e.getY());
            fGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
    
    /** This class draws a single row of the fractal. **/
    private class FractalWorker extends SwingWorker<Object, Object> {
    	/** Y-coordinate for the row **/
    	int rowY;
    	/** Array of rgb values for the pixels in this row. **/
    	int[] rgbVals;
    	
    	/** Constructor. **/
    	public FractalWorker(int yCoord) {
    		rowY = yCoord;
    	}
    	
    	/**
    	 *  Computes the rgb values for the row and stores them in
    	 *  the array.
    	 **/
    	public Object doInBackground() {
    		rgbVals = new int[dispSize];
		    double yCoord = FractalGenerator.getCoord(range.y, 
		            range.y + range.width, dispSize, rowY);
		    
		    for (int i = 0; i < dispSize; i++) {
		    	double xCoord = FractalGenerator.getCoord(range.x, 
	                    range.x + range.width, dispSize, i);
			    double numIters = fGen.numIterations(xCoord, yCoord);
			    
			    if (numIters == -1) {
			        /** The pixel is not in the set. Color it black. **/
			        rgbVals[i] = 0;
			    }
			    else {
			        /** The pixel is in the fractal set.
			         *  Color the pixel based on the number of iterations
			         *  it took to escape. 
			         */
			        float hue = 0.7f + (float) numIters / 200f;
			        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
			        rgbVals[i] = rgbColor;
			    }
		    }
		    return null;
    	}
    	
    	/**
    	 * Draws the rgb values to the screen.
    	 */
    	public void done() {
    		for (int i = 0; i < dispSize; i++) {
    			img.drawPixel(i, rowY, rgbVals[i]);
    		}
    		img.repaint(0, 0, rowY, dispSize, 1);
    		// Decrement the number of rows still being drawn.
    		rowsRemaining -= 1;
    		// If all rows are done, reenable the UI.
    		if (rowsRemaining == 0) {
    			enableUI(true);
    		}
    	}
    }
}
