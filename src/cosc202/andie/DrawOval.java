package cosc202.andie;

import java.awt.image.*;
import java.awt.*;

/**
 * Class which draws an oval onto the image given coordinates, dimensions and a colour
 */
public class DrawOval implements ImageOperation, java.io.Serializable{
    private int topLeftX;
    private int topLeftY;
    private int width;
    private int height;
    private Color colour;
    boolean filled;
    /**
     * constructor which takes in the values of the oval and assigns them to datafields
     * @param x top left x coordinate
     * @param y left left y coordinate
     * @param w width of the shape
     * @param h height of the shape
     * @param c colour of the shape
     */
    DrawOval(int x, int y, int w, int h, Color c, boolean f){
        //Constructor
        topLeftX = x;
        topLeftY = y;
        width = w;
        height = h;
        colour = c;
        filled = f;
    }
    /**
     * method which draws the oval onto a new BufferedImage and returns this
     */
    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        Graphics2D graph = output.createGraphics();
        graph.setColor(colour);
        //System.out.println(filled);
        if(filled){
            graph.fillOval(topLeftX, topLeftY, width, height);
        }else{
            graph.drawOval(topLeftX, topLeftY, width, height);;
        }
        
        graph.dispose();

        //return the output - photo with filter applied 
        return output;
    }
    public String toString(){
        return "Draw oval";
    }
}