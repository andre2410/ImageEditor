package cosc202.andie;

//import java.awt.Graphics2D;
//import java.awt.Image.*;
import java.awt.image.AffineTransformOp;
//import java.awt.Image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

/**
 * Class written by Ben that does Rotation
 */
public class Rotate implements ImageOperation, java.io.Serializable {
    private int angle;

    /**
     * Rotates the image
     * @param angle the angle to rotate the image by in degrees
     */
    public Rotate(int angle) { 
        this.angle = angle;
    }

    public BufferedImage apply(BufferedImage input) { // theta must be an angle with intervals of 90 deg (for error testing)
        int newImageW = 0;
        int newImageH = 0;
        //image dimensions will change under most rotations except 180 degree ones
        if (angle == 180 || angle == -180) {
            newImageW = input.getWidth(); 
            newImageH = input.getHeight();
        } else {
            newImageW = input.getHeight(); //used for rotations such as 90 deg.
            newImageH = input.getWidth();
        }
        

        // creates a new buffer image with the flipped dimensions of the original to
        // store the rotated image
        BufferedImage output = new BufferedImage(newImageW, newImageH, BufferedImage.TYPE_INT_ARGB);

        
        AffineTransform a = new AffineTransform(); //transform for performing image rotations
        
        //rotates the transform before translating it appropriately 'fit' the new buffered image

        a.rotate(Math.toRadians(angle));
        if(angle == 90 || angle == -270) {            
            a.translate(0, -input.getHeight());
        } else if(angle == 180 || angle == -180) {
            a.translate(-input.getWidth(), -input.getHeight());
        } else if(angle == 270 || angle == -90) {
            a.translate(-input.getWidth(), 0);
        }
        //applies the rotated transform to the image itself and writes this to the output buffered image
        AffineTransformOp aop = new AffineTransformOp(a, AffineTransformOp.TYPE_BILINEAR); //billinear isn't that important with 90deg rotations
        output = aop.filter(input, output);

        
        return output;// returns a new image which is rotated to the angle desired
    }

    public String toString(){
        return "Rotate " + angle + " degrees";
    }
}
