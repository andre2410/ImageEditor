package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.image.*;

public class Sharpen implements ImageOperation, java.io.Serializable{
    Sharpen(){
        //Constructor
    }
    /**
     * Applies sharpen to the image using ConvolveOp
     * to account for border - the image was increaed in size by the radius with the original placed centerd on top of it so that the bordering colours would match. 
     * The filter was applied and the image was cropped down to its original size
     */
    public BufferedImage apply(BufferedImage input){
        // Application of sharpen - returning output of type BufferedImage
        // Values of the Kernel 
        float [] array = { 0 , -1/2.0f, 0 ,
                         -1/2.0f, 3, -1/2.0f,
                         0   , -1/2.0f,   0   };

        // 3x3 filter from array
        Kernel kernel = new Kernel(3, 3, array);
        int radius = 1;

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
        return "Sharpen image";
    }
}