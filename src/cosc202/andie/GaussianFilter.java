package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.util.Arrays;

/**
 * <p>
 * ImageOperation to apply a Gaussian filter.
 * </p>
 * 
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Steven Mills
 * @version 1.0
 */
public class GaussianFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Gaussian filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed GaussianFilter
     */
    GaussianFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Gaussian filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Gaussian filter has radius 1.
     * </p>
     * 
     * @see GaussianFilter(int)
     */
    GaussianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Gaussian filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Gaussian filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.  
     * Larger radius lead to stronger blurring.
     * to account for border - the image was increaed in size by the radius with the original placed centerd on top of it so that the bordering colours would match. 
     * The filter was applied and the image was cropped down to its original size
     * </p>
     * 
     * @param input The image to apply the Gaussian filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage apply(BufferedImage input) {
        int size = (2*radius+1) * (2*radius+1);
        double [] array = new double[size];
        double sd = radius / 3.0;
        int indexCount = 0;

        //The Gaussian equation is applied to each of the coordinates in the kernal
        for(int y = -radius; y <= radius; y++){
            for(int x = -radius; x <= radius; x++){
                array[indexCount] = (float) ((1/(2 * Math.PI * Math.pow(sd, 2))) * (Math.pow(Math.E, -((Math.pow(x, 2) +  Math.pow(y, 2))/(2 * Math.pow(sd, 2))))));
                indexCount++;
            }
        }

        double kernalSum = Arrays.stream(array).sum(); //Sum of the kernal

        //Each element in the kernal is divided by the sum to normalise it 
        float[] floatArray = new float[size];
        for(int i = 0; i < array.length; i++){
            floatArray[i] = (float)(array[i]/kernalSum);
        }

        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, floatArray);

        //dealing with the border
        BufferedImage newSource = new BufferedImage((input.getWidth() + (2*radius)), (input.getHeight() + (2*radius)), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newSource.createGraphics();
        g2.drawImage(input, 0, 0, newSource.getWidth(), newSource.getHeight(), null);
        g2.drawImage(input, radius, radius, null);
        g2.dispose();

        ConvolveOp convOp = new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP, null);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        output = convOp.filter(newSource, null);
        //convOp.filter(newSource, output);

        BufferedImage originalSize = output.getSubimage(radius, radius, input.getWidth(), input.getHeight());
        return new BufferedImage(originalSize.getColorModel(), originalSize.copyData(originalSize.getRaster().createCompatibleWritableRaster()), originalSize.isAlphaPremultiplied(), null);
    }

    public String toString(){
        return "Gaussian filter with radius of " + radius;
    }
}