package cosc202.andie;
import java.util.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import java.awt.AlphaComposite;

/**
 * Class for cropaction buttons
 */
public class CropActions {
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Crop menu actions.
     * </p>
     */
    public CropActions() {
        actions = new ArrayList<Action>();
        actions.add(new CropAction("Crop", null, "Crop", Integer.valueOf(KeyEvent.VK_C)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Crop actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu cropMenu = new JMenu("Crop");

        JMenuItem crop = new JMenuItem(actions.get(0));
        crop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        cropMenu.add(crop);

        return cropMenu;
    }

    /**
     * <p>
     * Crop function class
     */
    public class CropAction extends ImageAction {
        private Rectangle r;
        private Cropper cropper;
        /**
         * <p>
         * Create a new Test action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        CropAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the test action is triggered.
         * </p>
         * 
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Crop error", "Please open an image first");
                p.show();
                return;
            }
            //Establish info
            int x = target.getImage().getCurrentImage().getWidth();
            int y = target.getImage().getCurrentImage().getHeight();
            BufferedImage image = target.getImage().getCurrentImage();

            //Select area
            cropper = new Cropper(x, y, image);
            target.add(cropper);
            
        }
        /**
         * Inner class for crop function
         * inspired by https://www.daniweb.com/programming/software-development/threads/342036/how-to-select-images-by-using-a-rectangular-area#post1453252
         */
        private class Cropper extends JPanel implements MouseMotionListener, MouseListener{
            private Rectangle selected;
            private Point point;
            private BufferedImage image;
            private int fx, fy;

            /**
             * Constructor
             */
            public Cropper(int fx, int fy, BufferedImage image){
                this.addMouseListener(this);
                this.addMouseMotionListener(this);
                setOpaque(false);
                this.fx = fx;
                this.fy = fy;
                setSize(fx,fy);
            }

            /**
            * Paint component for rectangle
            * Getting the rectangle transparent inspired from http://www.java2s.com/Code/Java/2D-Graphics-GUI/Drawtenrectangleswithdifferentlevelsoftransparency.htm
            */
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                    if(selected != null){
                        Graphics2D g2d = (Graphics2D)(g);
                        //g2d.draw(selected);
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        g2d.fillRect(selected.x, selected.y, selected.width, selected.height);
                        
                }
            }

            /**
            * Mouse pressed event
            */
            public void mousePressed(MouseEvent e){
                point = e.getPoint();
                selected = new Rectangle(point);
            }

            /**
            * Mouse dragged event 
            * Had a bug where rectangle could extent itself even if boundary is reached, thanks Tony for fix.
            */
            public void mouseDragged(MouseEvent e){
                //Finalise x 
                int x = e.getX();
                if(x < 0) x = 0;
                if(x > fx) x = fx - 1;
                int finalX = (int)Math.min(point.x, x);

                //Finalise y
                int y = e.getY();
                if(y< 0) y = 0;
                if(y > fy) y = fy -1;
                int finalY = (int)Math.min(point.y, y);

                //Finalise width and height
                int width = (int)Math.abs(x - point.x);
                int height = (int)Math.abs(y - point.y);

                //Set rectangle
                selected.setBounds(finalX, finalY, width, height);
                repaint();
            }

            /**
            * Mouse released event
            * Throws option pane as well
            */
            public void mouseReleased(MouseEvent e){
                int result = JOptionPane.showConfirmDialog(null, "Select Yes to crop to selected area \nSelect No to cancel action", "Crop", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch (result) {
                   case JOptionPane.YES_OPTION:
                   r = selected;
                   target.getImage().apply(new Crop(image, r));
                   target.repaint();
                   target.getParent().revalidate();
                   break;

                   case JOptionPane.NO_OPTION:
                   target.repaint();
                   target.getParent().revalidate();
                   break;
                   
                   case JOptionPane.CLOSED_OPTION:
                   target.repaint();
                   target.getParent().revalidate();
                   break;
                }

            }
                
            /**
            * Unused methods required for interface
            */
            public void mouseMoved(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        }
    }
}
