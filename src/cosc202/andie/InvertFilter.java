package cosc202.andie;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * <p>
 * ImageOperation to apply a inverted colour filter.
 * </p>
 * 
 * <p>
 * A invert filter changes images RGB values by replacing each pixels colour values with inversions of it.
 * 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * 
 * @author Nicky Patterson
 * @version 1.1
 */
public class InvertFilter implements ImageOperation, java.io.Serializable {
    
    

    InvertFilter(){
        
    }

    /**
     * <p>
     * Apply an inversion filter to an image.
     * </p>
     * 
     * @param input The image to apply the Mean filter to.
     * @return The resulting inverted image.
     */
    public BufferedImage apply(BufferedImage input) {
    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
    for(int x=0;x<input.getWidth(); x++ ){
        for(int y=0;y<input.getHeight();y++){
        Color c = new Color(input.getRGB(x,y));
        int blue =c.getBlue();
        int red =c.getRed();
        int green =c.getGreen();

        blue=255-blue;
        red=255-red;
        green =255 - green;
        Color repaint = new Color(red,green,blue);
        output.setRGB(x,y,repaint.getRGB());
        }
    }
    
    return output;
    }




    public String toString(){
        return "Invert filter";
    }
}


