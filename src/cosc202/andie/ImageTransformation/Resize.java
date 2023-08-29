package cosc202.andie.ImageTransformation;



import java.awt.Graphics2D;
import java.awt.Image.*;
import java.awt.image.AffineTransformOp;
//import java.awt.Image.AffineTransformOp;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

import java.awt.geom.AffineTransform;

public class Resize implements ImageOperation, java.io.Serializable {
    private int width;
    private int height;
    
    /**
     * Resizes the image
     * @param width values above or below 
     * @param height
     */
    public Resize(int width, int height) { // error handling - must be positive values
        this.width = width;
        this.height = height;
    }
    public BufferedImage apply(BufferedImage input) {
        double scaleX = (1.00/(100.00/width));
        double scaleY = (1.00/(100.00/height));// scaling works, just requires some tweaking with the scale factors and image sizes

        double imageW = input.getWidth();
        double imageH = input.getHeight();                                                            

        int scaledImageX = (int)(imageW*scaleX);
        int scaledImageY = (int)(imageH*scaleY);

        //creates a new buffer image with the scaled dimensions of the original to store the newly scaled image
        BufferedImage output = new BufferedImage(scaledImageX, scaledImageY, BufferedImage.TYPE_INT_ARGB);

        //creates a scale transform to scale the image bilinearly
        AffineTransform a = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp aop = new AffineTransformOp(a, AffineTransformOp.TYPE_BILINEAR);
        output = aop.filter(input, output);

        /*Graphics2D g = output.createGraphics();
        g.drawImage(output, aop, 200, 0);
        g.dispose();*/
        return output;//returns a new image which is a scaled version of the original
    }    
}
