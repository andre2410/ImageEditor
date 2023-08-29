package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * Draw actions contains the different shape actions to add a drawing operation onto the stack
 * To prove the concept only two shapes were added - rectangle and oval. 
 * More can be added in the future using this framework 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Rosie Bicknell
 * @version 1.0
 */
public class DrawActions {
    
    /** A list of actions for the Draw menu. 
     * Instead of using this list I wanted to access the actions directly as the actionPerformed method I want to use is unique to the shapes
     * a new interface could have been created for shapes if given more time as this would allow keeping an array of them and looping through
    */
    protected ArrayList<Action> actions;
    protected DrawRectangleAction drawRecAction;
    protected DrawOvalAction drawOvalAction;
    protected DrawLineAction drawLineAction;

    /**
     * <p>
     * Create a set of drawing actions.
     * </p>
     */
    public DrawActions() {
        actions = new ArrayList<Action>();
        drawRecAction = new DrawRectangleAction("Draw rectangle", null, "draw a rectangle", Integer.valueOf(KeyEvent.VK_2));
        drawOvalAction = new DrawOvalAction("Draw oval", null, "draw an oval", Integer.valueOf(KeyEvent.VK_3));
        drawLineAction = new DrawLineAction("Draw line", null, "draw a line", Integer.valueOf(KeyEvent.VK_4));
        //actions.add(new DrawTreeAction("Draw tree", null, "draw a tree", Integer.valueOf(KeyEvent.VK_4))); // a tree was going to be added but was not due to not overcomplicating before a solid proof of concept
    }


    /**
     * DrawRectangle action class
     */
    public class DrawRectangleAction extends ImageAction{
        /**
         * <p>
         * Create a new draw rectangle action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        DrawRectangleAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        /**
         * 
         * <p>
         * This method is called whenever the DrawRectangleAction is triggered.
         * Draws a rectangle onto the image where the user has started and finished dragging their mouse
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            //this is empty
        }
        /**
         * Separate action performed method for drawing shapes. 
         * This takes in coordinates and a colour
         * @param evt
         * @param x the x coordinate of the shape
         * @param y the y coordinate of the shape
         * @param w the width of the shape
         * @param h the height of the shape
         * @param c the colour of the shape
         */
        public void actionPerformed(MouseEvent evt, int x, int y, int w, int h, Color c){
            //System.out.println(DrawToolBar.fill.isSelected());
            target.getImage().apply(new DrawRectangle(x, y, w, h, c,DrawToolBar.fill.isSelected()));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class DrawOvalAction extends ImageAction{
        /**
         * <p>
         * Create a new draw oval action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        DrawOvalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        /**
         * 
         * <p>
         * This method is called whenever the DrawOvalAction is triggered.
         * Draws an oval onto the image where the user has started and finished dragging their mouse
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            //this is empty
        }
        /**
         * Separate action performed method for drawing shapes. 
         * This takes in coordinates and a colour
         * @param evt
         * @param x the x coordinate of the shape
         * @param y the y coordinate of the shape
         * @param w the width of the shape
         * @param h the height of the shape
         * @param c the colour of the shape
         */
        public void actionPerformed(MouseEvent evt, int x, int y, int w, int h, Color c){
            target.getImage().apply(new DrawOval(x, y, w, h, c,DrawToolBar.fill.isSelected()));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class DrawLineAction extends ImageAction{
        /**
         * <p>
         * Create a new draw oval action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        DrawLineAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        /**
         * 
         * <p>
         * This method is called whenever the DrawOvalAction is triggered.
         * Draws an oval onto the image where the user has started and finished dragging their mouse
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            //this is empty
        }
        /**
         * Separate action performed method for drawing shapes. 
         * This takes in coordinates and a colour
         * @param evt
         * @param x the x coordinate of the shape
         * @param y the y coordinate of the shape
         * @param w the width of the shape
         * @param h the height of the shape
         * @param c the colour of the shape
         */
        public void actionPerformed(MouseEvent evt, int x, int y, int w, int h, Color c){
            target.getImage().apply(new DrawLine(x, y, w, h, c));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
