package cosc202.andie;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 * 
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well as zooming
 * in and out. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ImagePanel extends JPanel {
    
    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /**
     * <p>
     * The zoom-level of the current view.
     * A scale of 1.0 represents actual size; 0.5 is zoomed out to half size; 1.5 is zoomed in to one-and-a-half size; and so forth.
     * </p>
     * 
     * <p>
     * Note that the scale is internally represented as a multiplier, but externally as a percentage.
     * </p>
     */
    private double scale;

    DrawActions drawActions = new DrawActions();
    private MyMouseDraggedListener listener;
    private JComboBox shapeSelected;
    //mouse stuff for drawing
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    /**
     * <p>
     * Create a new ImagePanel.
     * </p>
     * 
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {
        image = new EditableImage();
        scale = 1.0;

        listener = new MyMouseDraggedListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * </p>
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100*scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * The zoom level is restricted to the range [50, 200].
     * </p>
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 50) {
            zoomPercent = 50;
        }
        if (zoomPercent > 200) {
            zoomPercent = 200;
        }
        scale = zoomPercent / 100;
    }


    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     * 
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a default size if no image is present.
     * </p>
     * 
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth()*scale), 
                                 (int) Math.round(image.getCurrentImage().getHeight()*scale));
        } else {
            return new Dimension(450, 450);
        }
    }
    //allows other methods to see scale
    public double getScale(){
        return scale;
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     * 
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image.hasImage()) {
            Graphics2D g2  = (Graphics2D) g.create();
            g2.scale(scale, scale);
            g2.drawImage(image.getCurrentImage(), null, 0, 0);
            g2.dispose();
        }
    }
    /** 
     * Class which responds to the user's mouse input when a shape has been selected in the drop down menu on the draw tool bar.  
     * This finds the start and end position where a user has dragged their mouse (considering the zoom) and calls the corresponsing shape drawing action based on what shape has been selected
     */
    private class MyMouseDraggedListener extends MouseInputAdapter {

        public void mousePressed(MouseEvent evt) {
            startX = evt.getX();
            startY = evt.getY();
        }
        
        @Override
        public void mouseDragged(MouseEvent evt) {
            endX = evt.getX();
            endY = evt.getY();
        }
    
        @Override
        /**
         * finds the top left corner and shape dimensions and calls the correct draw action based on the draw toolbar drop down menu
         */
        public void mouseReleased(MouseEvent evt) {
            shapeSelected = DrawToolBar.shapeSelector;
            Color selectedColour = DrawToolBar.chosenColour;
            
            endX = evt.getX();
            endY = evt.getY();
    
            int topLeftX;
            int topLeftY;
            int width;
            int height;
            if (startX < endX) {
                topLeftX = startX;
                width = endX - startX;
            } else {
                topLeftX = endX;
                width = startX - endX;
            }
            if (startY < endY) {
                topLeftY = startY;
                height = endY - startY;
            } else {
                topLeftY = endY;
                height = startY - endY;
            }

            int scaledTopLeftX = (int)(topLeftX/scale);
            int scaledTopLeftY = (int)(topLeftY/scale);

            int scaledStartX = (int)(startX/scale);
            int scaledStartY = (int)(startY/scale);
            int scaledEndX = (int)(endX/scale);
            int scaledEndY = (int)(endY/scale);

            int scaledWidth = (int)(width/scale);
            int scaledHeight = (int)(height/scale);

            if (image.hasImage()) {
                if(shapeSelected.getSelectedIndex() == 1){
                    //rectangle
                    drawActions.drawRecAction.actionPerformed(evt, scaledTopLeftX, scaledTopLeftY, scaledWidth, scaledHeight, selectedColour);
                }else if(shapeSelected.getSelectedIndex() == 2){
                    //oval
                    drawActions.drawOvalAction.actionPerformed(evt, scaledTopLeftX, scaledTopLeftY, scaledWidth, scaledHeight, selectedColour);
                }else if(shapeSelected.getSelectedIndex() == 3){
                    //line
                    drawActions.drawLineAction.actionPerformed(evt, scaledStartX, scaledStartY, scaledEndX, scaledEndY, selectedColour);
                }
            } 
        }
    }
}