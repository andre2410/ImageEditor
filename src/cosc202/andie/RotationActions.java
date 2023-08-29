package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Class to provide rotation functions
 * Combined with Andrew's and Ben's codes
 */
public class RotationActions{

    protected ArrayList<Action> actions;
    //TONY ADDED
    protected int angle;

    public RotationActions() {
        actions = new ArrayList<Action>();
        actions.add(new EdgeDetectAction("Edge Detect", null, "Bring out the edges", null));
        actions.add(new ResizeAction("Resize Image", null, "Scales image to new size", null));
        actions.add(new RotateAction("Rotate Image", null, "Rotates the image in degrees", null));
        actions.add(new FlipVertical("Flip Vertical", null, "Flip Vertical", null));
        actions.add(new FlipHorizontal("Flip Horizontal", null, "Flip Horizontal", null));
    }
    /**
     * Copied method to create menu
     * @return The view menu UI element.
     */
    public JMenu createMenu() {
        JMenu flipMenu = new JMenu("Adjustments");
        
        JMenuItem edgeDetect = new JMenuItem(actions.get(0));
        edgeDetect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        flipMenu.add(edgeDetect);

        JMenuItem resize = new JMenuItem(actions.get(1));
        resize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        flipMenu.add(resize);

        JMenuItem rotate = new JMenuItem(actions.get(2));
        rotate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        flipMenu.add(rotate);

        JMenuItem flipV = new JMenuItem(actions.get(3));
        flipV.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        flipMenu.add(flipV);

        JMenuItem flipH = new JMenuItem(actions.get(4));
        flipH.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        flipMenu.add(flipH);

        //for (Action action: actions) {
        //    flipMenu.add(new JMenuItem(action));
        //}

        return flipMenu;
    }

    public class EdgeDetectAction extends ImageAction {
        EdgeDetectAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("EdgeDetect error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new EdgeDetect());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class ResizeAction extends ImageAction {
        JSlider ws = new JSlider(0, 200, 100);
        JSlider hs = new JSlider(0, 200, 100);

        JLabel wl = new JLabel("Width: " +  ws.getValue() + "%");
        JLabel hl = new JLabel("Height: " +  ws.getValue() + "%");

        JPanel mane = new JPanel();
        ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Resize error", "Please open an image first");
                p.show();
                return;
            }

            int width = 100;
            int height = 100;
            
          
            mane.setLayout(new BoxLayout(mane, BoxLayout.Y_AXIS));
            mane.setPreferredSize(new Dimension(250, 150));

            SliderListener sliderListener = new SliderListener();
            // credit to Tony for this interface design
            ws.setSnapToTicks(true);
            ws.setMajorTickSpacing(50);
            ws.setMinorTickSpacing(10);
            ws.setPaintTicks(true);
            ws.setPaintLabels(true);
            ws.addChangeListener(sliderListener);

            hs.setSnapToTicks(true);
            hs.setMajorTickSpacing(50);
            hs.setMinorTickSpacing(10);
            hs.setPaintTicks(true);
            hs.setPaintLabels(true);
            hs.addChangeListener(sliderListener);

            mane.add(ws);
            mane.add(wl);
            mane.add(hs);
            mane.add(hl);
            
            int option = 0;
            
            try {
                option = JOptionPane.showOptionDialog(null, mane, "Scale", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                //System.out.println("numba" + w.getValue());
                if(option == JOptionPane.OK_CANCEL_OPTION) {
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    width = ws.getValue();
                    height = hs.getValue();
                    
                } 

                
            } catch(IllegalArgumentException er) {
                JOptionPane.showConfirmDialog(null, "Invalid width or height, cannot be negative or 0", "enter a new value", JOptionPane.INFORMATION_MESSAGE);

            }
                
                
           
            target.getImage().apply(new Resize(width, height));
            target.repaint();
            target.getParent().revalidate();
        }
        private class SliderListener implements ChangeListener{
            public void stateChanged(ChangeEvent e){
                wl.setText("Width: " +  ws.getValue() + "%");
                hl.setText("Height: " + hs.getValue() + "%");
            }
        }
    }
    
    

    public class RotateAction extends ImageAction {
        RotateAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * function to run from toolbar
         * @param angle the angle to rotate by
         */
        public void ToolBarRotate(int angle) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Rotate error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new Rotate(angle));
            target.repaint();
            target.getParent().revalidate();
        }
        public void actionPerformed(ActionEvent e) {
            
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Rotate error", "Please open an image first");
                p.show();
                return;
            }
            

            int angle = 0;
            //int[] angles = {0, 90, -90, 180, -180, 270, -270, 360, -360};
            String[] angles = {"0", "90", "-90", "180", "-180", "270", "-270", "360", "-360"};

            
            String option = (String)JOptionPane.showInputDialog(null, "Select an angle", "Rotate", JOptionPane.QUESTION_MESSAGE, null, angles, angles[0]);
            
            if(option == null) {
                return;
            }
                angle = Integer.parseInt(option);
            //}
            

            // Create and apply the filter
            target.getImage().apply(new Rotate(angle));
            target.repaint();
            target.getParent().revalidate();
            

            angle = 0;
        }
    }

    //TONY ADDED (Ben added code rotate the image using directly modified angle)
    public void setRotationAngle(int angle){
        //this.angle = angle;
        RotateAction r = new RotateAction("rotate", null, "toolbar rotate", 1);//arguments not neccessary but required
        r.ToolBarRotate(angle);
    }

    
    /**
     * Method to return a 'quantized' angle that is snapped to increments of 90 deg (is no longer used :( )
     * @param a angle to snap
     * @return quantized angle
     */
    private int round(int a) {
        for(int i = 1; i < 5; i++) {

            if(i*90 < (a * sign(a))) {
                continue;
            }

            if((((i*90)) - ((i*90)/(Math.pow(2, i))) > (a * sign(a)))) {
                a = (i-1)*90 * sign(a);
                return a;

            } else {
                a = ((i)*90) * sign(a);
                return a;
            }
            
        }
        return 1;//so the class compiles
    }

    private int sign(int a) {
        if(a > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Class to flip image vertically by changing x order
     */

    public class FlipVertical extends ImageAction{
        /**
         * Constructor
         * @param name
         * @param icon
         * @param desc
         * @param mnemonic
         */
        public FlipVertical(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        /**
         * Method to make things happen by running Flip class.
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Flip error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new Flip(0));
            target.repaint();
            target.getParent().revalidate();
        }

    }
    /**
     * Class to flip image horizontally by changing y order
     */
    public class FlipHorizontal extends ImageAction{
        /**
         * Constructor
         * @param name
         * @param icon
         * @param desc
         * @param mnemonic
         */
        public FlipHorizontal(String name, ImageIcon icon, String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }
        /**
         * Method to make things happen by running Flip class.
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Flip error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new Flip(1));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}