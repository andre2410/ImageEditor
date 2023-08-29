package cosc202.andie;
import java.awt.image.*;
import java.awt.Rectangle;

/**
 * Class for Crop 
 */
public class Crop implements ImageOperation, java.io.Serializable{
    BufferedImage image;
    Rectangle r;
    //private int x, y;
    /**
     * Constructor
     */
    public Crop(BufferedImage image, Rectangle r){
        this.image = image;
        this.r = r;
    }

    public BufferedImage apply(BufferedImage image){
        
        
        double zoomScale = ImageAction.getTarget().getScale();
        try{
            BufferedImage cropped = image.getSubimage((int)(r.x/zoomScale), (int)(r.y/zoomScale), (int)(r.width/zoomScale), (int)(r.height/zoomScale)); // creates BufferedImage of the cropped area scaled to the zoom percentage
            BufferedImage output = new BufferedImage(cropped.getColorModel(), cropped.copyData(cropped.getRaster().createCompatibleWritableRaster()), cropped.isAlphaPremultiplied(), null); //copies information into new BufferedImage using fix found on https://github.com/romankh3/image-comparison/issues/136  
            return output;
        }catch(Exception e){}

        return image;
    }



    public String toString(){
        return "Crop";
    }
}
