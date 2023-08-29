package cosc202.andie;

import java.awt.image.*;
import java.awt.*;

/**
 * Class which draws a rectangle onto the image given coordinates, dimensions and a colour
 */
public class DrawLine implements ImageOperation, java.io.Serializable{
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Color colour;
    /**
     * constructor which takes in the values of the rectangle and assigns them to datafields
     * @param x top left x coordinate
     * @param y left left y coordinate
     * @param w width of the shape
     * @param h height of the shape
     * @param c colour of the shape
     */
    DrawLine(int x, int y, int ex, int ey, Color c){
        //Constructor
        startX = x;
        startY = y;
        endX = ex;
        endY = ey;
        colour = c;
    }
    /**
     * method which draws the rectangle onto a new BufferedImage and returns this
     */
    public BufferedImage apply(BufferedImage input){
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        Graphics2D graph = output.createGraphics();
        graph.setColor(colour);
        graph.drawLine(startX, startY, (endX), (endY));
        graph.dispose();

        //return the output - photo with filter applied 
        return output;
    }
    public String toString(){
        return "Draw line";
    }
}