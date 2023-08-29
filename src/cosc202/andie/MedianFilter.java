package cosc202.andie;

import java.awt.image.*;
import java.util.*;

//import javax.print.attribute.standard.Media;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 * 
 * <p>
 * A Median filter blurs an image by finding the median a, r, g, and b values from neighbouring pixels and assigning it to the new pixel value.
 * The filter iterates through all pixels in an image, and for each it will find the median of the neighbouring values (the amount of values it iterates over depends on the radius)
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Steven Mills
 * @version 1.0
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Median filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    MedianFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Median filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     * 
     * @see MedianFilter(int)
     */
    MedianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     * 
     * <p>
     * The Median filter assings each pixel the median colour values of neighbouring pixels, the amount of neighbouring pixels depends on {@link radius}.  
     * Larger radius lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Median filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage apply(BufferedImage input) {
        int size = (2*radius+1) * (2*radius+1);

        //Iterates through all the pixels in picture
        //Leaves out a border the size of the radius - this is to stop trying to find the colour values of pixels that don't exist (e.g (-1,-1))
        for(int y = radius; y < input.getHeight() - 1 - radius; y++){
            for(int x = radius; x < input.getWidth() - 1 - radius; x++){
                //For each pixel within area inside radius, create array for all colour channels for neighbouring pixels
                int[] neighbourA = new int[size];
                int[] neighbourR = new int[size];
                int[] neighbourG = new int[size];
                int[] neighbourB = new int[size];
                int indexCount = 0;
                int argb = input.getRGB(x, y);
                for(int ny = y - radius; ny <= y + radius; ny++){
                    for(int nx = x - radius; nx <= x + radius; nx++){
                        //For each neighbour - get a, r, g, and b values
                        int neighbourArgb = input.getRGB(nx, ny);
                        int a = (neighbourArgb & 0xFF000000) >> 24;
                        int r = (neighbourArgb & 0x00FF0000) >> 16;
                        int g = (neighbourArgb & 0x0000FF00) >> 8;
                        int b = (neighbourArgb & 0x000000FF);

                        //Update array with colour values for neighbour
                        neighbourA[indexCount] = a;
                        neighbourR[indexCount] = r;
                        neighbourG[indexCount] = g;
                        neighbourB[indexCount] = b;
                        indexCount++;
                    }
                }
                //Sort each arrays by numerical value
                Arrays.sort(neighbourA);
                Arrays.sort(neighbourR);
                Arrays.sort(neighbourG);
                Arrays.sort(neighbourB);

                //Select the median value from each 
                int medianIndex = (int)(size / 2);
                argb = (neighbourA[medianIndex] << 24) | (neighbourR[medianIndex] << 16) | (neighbourG[medianIndex] << 8) | neighbourB[medianIndex];
                
                //Testing
                //Print out the colour channels for each pixel, along with new assigned pixel value
                //System.out.println("a: " + Arrays.toString(neighbourA));
                //System.out.println("r: " + Arrays.toString(neighbourR));
                //System.out.println("g: " + Arrays.toString(neighbourG));
                //System.out.println("b: " + Arrays.toString(neighbourB));
                //System.out.println("a: " + neighbourA[medianIndex] + " r: " + neighbourR[medianIndex] + " g: " + neighbourG[medianIndex] + " b: " + neighbourB[medianIndex]);
                //End of Testing

                input.setRGB(x, y, argb);
            }
        }
        
        return input;
    }

    public String toString(){
        return "Median filter with radius of " + radius;
    }
}
