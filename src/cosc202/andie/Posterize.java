package cosc202.andie;

import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to posterize image (Restricts number of colours in image)
 * Has five levels; each level doubles the number of RGB values available
 * </p>
 * 
 * @author Tony Cenaiko
 * @version 1.0
 */
public class Posterize implements ImageOperation, java.io.Serializable{
    private int level;
    ArrayList<Integer> posVals = new ArrayList<Integer>();

    /**
     * <p>
     * Creates new Posterize operation
     * </p>
     * 
     * @param level How many discrete colours are available
     */
    Posterize(int level){
        this.level = level;
        setIncrements();
    }

    /**
     * Sets increments of possible RGB values between 0 and 255 (inclusive) using level
     */
    private void setIncrements(){
        int increments = (int)(256 / Math.pow(2, level));

        int x = 0;
        while(x < 255){
            posVals.add(x);
            x += increments;
        }
        posVals.add(255);
    }

    /**
     * <p>
     * Restricts number of discrete colours in image
     * </p>
     * 
     * @param input The image to be adjusted
     * @return The adjusted image.
     */
    public BufferedImage apply(BufferedImage input){
        for(int y = 0; y < input.getHeight(); ++y){
            for(int x = 0; x < input.getWidth(); ++x){
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                int newR = newRGBValue(r);
                int newG = newRGBValue(g);
                int newB = newRGBValue(b);

                argb = (a << 24) | (newR << 16) | (newG << 8) | newB;
                input.setRGB(x, y, argb);
            }
        }
       
        return input;
    }

    /**
     * <p>
     * Receives red, green or blue value of pixel and adjusts it to closest
     * valid increment between 0 and 255 in posVals array
     * </p>
     * 
     * @param rgbVal Either red, green or blue value of individual pixel
     * @return Adjusted red, green or blue value of individual pixel
     */
    private int newRGBValue(int val){
        int minDistance = 256;
        int output = 0;
        
        for(int i: posVals){
            if(Math.abs(val - i) < minDistance){
                minDistance = Math.abs(val - i);
                output = i;
            }
        }
        
        return output;
    }

    public String toString(){
        return "Posterize with level " + level;
    }
}
