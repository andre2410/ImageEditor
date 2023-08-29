package cosc202.andie;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 * ImageOperation to apply colour to a greyscale filter.
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
 * @version 1.2
 */
public class NotGrey implements ImageOperation, java.io.Serializable {
    
    

    NotGrey(){
        
    }

    /**
     * <p>
     * Apply colour to  an image.
     * </p>
     * 
     * @param input The image to apply the notGrey filter to.
     * @return The resulting image.
     */
    public BufferedImage apply(BufferedImage input) {
    Random r = new Random();
    int[] redC = new int[256];
    int[] blueC = new int[256];
    int[] greenC = new int[256];

    for(int i=0;i<redC.length;i++){
        redC[i]=r.nextInt(255); 
        blueC[i]=r.nextInt(255);
        greenC[i]=r.nextInt(255);
    }

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
    for(int x=0;x<input.getWidth(); x++ ){
        for(int y=0;y<input.getHeight();y++){
        Color c = new Color(input.getRGB(x,y));
        int blue =c.getBlue();
        int red =c.getRed();
        int green =c.getGreen();

      

        

        blue=blueC[blue];
        red=redC[red];
        green =greenC[green];
        Color repaint = new Color(red,green,blue);
        output.setRGB(x,y,repaint.getRGB());
        }
    }
    
    return output;
    }


    private int clamp(int in){
        if(in<0)return 0;
        else if(in>255)return 255;
        else return (int)in;
    }

    public String toString(){
        return "NotGrey filter";
    }
    
}


