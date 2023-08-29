package cosc202.andie;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 * ImageOperation to A cel -shading esc filter to an image.
 * </p>
 * 
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
//
public class Simplify implements ImageOperation, java.io.Serializable {
    
    

    Simplify(){
        
    }

    /**
     * <p>
     * Apply filter to  an image.
     * </p>
     * 
     * @param input The image to apply the comic filter to.
     * @return The resulting image.
     */
    public BufferedImage apply(BufferedImage input) {

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
    for(int x=0;x<input.getWidth()-1; x++ ){
        for(int y=0;y<input.getHeight()-1;y++){
        int range = 25;
        Color c = new Color(input.getRGB(x,y));
        Color c2 = new Color(input.getRGB(x+1,y+1));

        int blue =c.getBlue();
        int red =c.getRed();
        int green =c.getGreen();

        int blue2=c2.getBlue();
        int red2 =c2.getRed();
        int green2 =c2.getGreen();

        if(Math.abs(blue2-blue)<range && Math.abs(red2-red)<range &&Math.abs(green2-green)<range){
            Color repaint = new Color(red,green,blue);
            output.setRGB(x+1,y+1,repaint.getRGB());
        }

        
        }
    }
    
    
    return output;
    }

    public String toString(){
        return "Simply filter";
    }
}


