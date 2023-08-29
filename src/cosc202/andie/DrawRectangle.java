package cosc202.andie;

import java.awt.image.*;
import java.awt.*;

/**
 * Class which draws a rectangle onto the image given coordinates, dimensions and a colour
 */
public class DrawRectangle implements ImageOperation, java.io.Serializable{
    private int topLeftX;
    private int topLeftY;
    private int width;
    private int height;
    private Color colour;
    private boolean filled;
    /**
     * constructor which takes in the values of the rectangle and assigns them to datafields
     * @param x top left x coordinate
     * @param y left left y coordinate
     * @param w width of the shape
     * @param h height of the shape
     * @param c colour of the shape
     */
    DrawRectangle(int x, int y, int w, int h, Color c, boolean f){
        //Constructor
        topLeftX = x;
        topLeftY = y;
        width = w;
        height = h;
        colour = c;
        filled = f;
    }
    /**
     * method which draws the rectangle onto a new BufferedImage and returns this
     */
    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        Graphics2D graph = output.createGraphics();
        graph.setColor(colour);
        if(filled){
            graph.fillRect(topLeftX, topLeftY, width, height);
        }else{
            graph.drawRect(topLeftX, topLeftY, width, height);;
        }
        //graph.fill(new Rectangle(topLeftX, topLeftY, width, height));
        graph.dispose();

        //return the output - photo with filter applied 
        return output;
    }
    public String toString(){
        return "Draw rectangle";
    }
}