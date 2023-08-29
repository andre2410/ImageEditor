package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to adjust brightness and contrast of an image.
 * </p>
 * 
 * @author Tony Cenaiko
 * @version 1.0
 */
public class AdjustBrightnessAndContrast implements ImageOperation, java.io.Serializable{
    private int brightness;
    private int contrast;
    
    /**
     * <p>
     * Create a new AdjustBrightnessAndContrast operation.
     * </p>
     * 
     * @param brightness The percentage of brightness that the image will be adjusted by
     * @param contrast The percentage of contrast that the image will be adjusted by
     */
    AdjustBrightnessAndContrast(int brightness, int contrast){
        this.brightness = brightness;
        this.contrast = contrast;
    };

    /**
     * <p>
     * Adjust brightness and contrast of an image.
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
     * Receives red, green or blue value of pixel and adjusts it using brightness and contrast
     * data fields in accordance with formula
     * </p>
     * 
     * @param rgbVal Either red, green or blue value of individual pixel
     * @return Adjusted red, green or blue value of individual pixel
     */
    private int newRGBValue(int rgbVal){
        int output = (int) Math.round((1 + contrast/100.0)*(rgbVal - 127.5) + 127.5*(1 + brightness/100.0));
        if(output > 255) return 255;
        if(output < 0) return 0;

        return output;
    }

    public String toString(){
        return "Adjust brightness by " + brightness + "%" + " and contrast by " + contrast + "%";
    }
}