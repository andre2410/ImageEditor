package cosc202.andie;

import java.util.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.JobKOctetsSupported;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.LabelView;

//Not sure what this does and it wouldn't let me compile with it in so I commented it out
//import cosc202.andie.ImageTransformation.*;



/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. 
 * This includes a mean filter (a simple blur) in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FilterActions {
    
    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<Action>();
        //actions.add(new EmbossAndEdgeDetectAction("Emboss", null, "Use Emboss with a variety of modes", Integer.valueOf(KeyEvent.VK_0)));//needs a proper shortcut

        actions.add(new MeanFilterAction("Mean filter", null, "Apply a mean filter", Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new MedianFilterAction("Median filter", null, "Apply a median filter", Integer.valueOf(KeyEvent.VK_F))); 
        actions.add(new SharpenAction("Sharpen", null, "Sharpen image", Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new GaussianFilterAction("Gaussian filter", null, "Apply a gaussian filter", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new EmbossAndEdgeDetectAction("Emboss", null, "Use Emboss with a variety of modes", Integer.valueOf(KeyEvent.VK_7)));//needs a proper shortcut
    }

    /**
     * <p>
     * Create a menu contianing the list of Filter actions.
     * Note: Any new actions must be manuelly added to the JMenu here since the automatic feature was taken out for quickkeys
     * </p>
     * 
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Filter");

        JMenuItem meanFilter = new JMenuItem(actions.get(0));
        meanFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        fileMenu.add(meanFilter);

        JMenuItem medianFilter = new JMenuItem(actions.get(1));
        medianFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        fileMenu.add(medianFilter);

        JMenuItem sharpen = new JMenuItem(actions.get(2));
        sharpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        fileMenu.add(sharpen);

        JMenuItem gaussian = new JMenuItem(actions.get(3));
        gaussian.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        fileMenu.add(gaussian);

        JMenuItem emboss = new JMenuItem(actions.get(4));
        emboss.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.CTRL_MASK));
        fileMenu.add(emboss);

        //for(Action action: actions) {
        //    fileMenu.add(new JMenuItem(action));
        //}

        return fileMenu;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Filter error", "Please open an image first");
                p.show();
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MeanFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    public class MedianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new median-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link MedianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Filter error", "Please open an image first");
                p.show();
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MedianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    public class EmbossAndEdgeDetectAction extends ImageAction {
        public EmbossAndEdgeDetectAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name,icon,desc,mnemonic);
        }
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Filter error", "Please open an image first");
                p.show();
                return;
            }
            //todo
            JPanel panel = new JPanel();
            JPanel panel2 = new JPanel();
            GridLayout g2 = new GridLayout(3,4);
            GridLayout g = new GridLayout(2,1);
            //g.setVgap(20);
            panel.setLayout(g);
            panel2.setLayout(g2);
            String[] listS = {"Emboss", "Edge Detection"};
            JList<String> list = new JList<String>(listS);
            list.setSelectedIndex(0);
            JLabel label = new JLabel("Select Type");
            
            panel.add(label);
           
            panel.setSize(25, 25);
            panel.add(list);
            panel.add(panel2);
            
            //int i =0;
           
            final ArrayList<JToggleButton> bjII = new ArrayList<JToggleButton>(Arrays.asList(new JToggleButton(), new JToggleButton()));

            ButtonGroup bg = new ButtonGroup();
            //ButtonGroup bg2 = new ButtonGroup();
            String[] dirs = new String[]{"←", "↖", "↑", "↗", "→", "↘", "↓", "↙" };
            final ArrayList<JToggleButton> bj = new ArrayList<JToggleButton>(Arrays.asList(new JToggleButton(), new JToggleButton(), new JToggleButton(), new JToggleButton(), new JToggleButton(), new JToggleButton(), new JToggleButton(), new JToggleButton()));
            
            String[] dirsII = new String[]{"↔", "↕"};
            
            //System.out.println("dir text" + dirs[0]);
            //--------
            //System.out.println(bjII.size() + "is bjII size");
           for(int i = 0; i < bj.size(); i++) {
                if(i<2) bjII.get(i).setText(dirsII[i]);
                if(i<2) bjII.get(i).setSize(20, 20);

                if(i<2) {
                    bg.add(bjII.get(i));
                    bg.add(bj.get(i));
                } else {
                    bg.add(bj.get(i));
                }
                

                bj.get(i).setText("e" + dirs[i]);
                bj.get(i).setSize(20, 20);

            
            

            }
            
            // System.out.println(bjII.get(0).getText());
            // System.out.println(bjII.get(1).getText());
            


            

            ListSelectionListener lis = new ListSelectionListener() {
                int i = 0;

                public void updatePanels() {

                    for(int i =0; i < 2; i++) {
                        panel2.add(bj.get(0), BorderLayout.CENTER);
                    }
                    return;
                }
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // TODO Auto-generated method stub 
                    
                    panel2.removeAll();
                    
                    
                    if(list.getSelectedValue().equals("Emboss")) {
                        
                    
                        for(int i = 0; i < bj.size(); i++) {
                            panel2.add(bj.get(i), BorderLayout.CENTER);
                        }
                        
                    
                    } else {
                        for(int i = 0; i < 2; i++) {
                            panel2.add(bjII.get(i), BorderLayout.CENTER);
                        
                        }
                 
                        
                    }
            
                    panel2.revalidate();
                    panel2.repaint();
                }
                
                
            };

           
            for(int i = 0; i < bj.size(); i++) {
               
                bj.get(i).setText(dirs[i]);
                bj.get(i).setSize(20, 20);
                panel2.add(bj.get(i), BorderLayout.CENTER);

            }
            list.addListSelectionListener(lis);
            
          
            int direction = 0;
            String type = "";
            
            int option = JOptionPane.showOptionDialog(null, panel, "emboss", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,null);//(null, "Set type", "Emboss and Edge", );// JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            //System.out.println(direction + " is direction and " + type + " is type. yessir");
            if(option == JOptionPane.OK_OPTION) {
                type = list.getSelectedValue();
                
                for(int j = 0; j < bj.size(); j++) {
                    if(type.equals("Emboss")) {
                        if(bj.get(j).isSelected()) {
                            direction = j;//this can be reworked to make the ui look bettr
                            
                            break;
                        }
                    } else {
                        if(j < 2) {
                            if(bjII.get(j).isSelected()) {
                                direction = j;//this can be reworked to make the ui look bettr
                                
                                break;
                            }
                    } else {
                        System.out.println("broken");
                        break;
                    }
                    }
                }
            } else {
                return;
            }
            EmbossAndSobel em = new EmbossAndSobel(direction, type);
            target.getImage().apply(em);
            target.repaint();
            target.getParent().revalidate();

        }
    }

    

    public class GaussianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new gaussian-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        GaussianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the GaussianFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link GaussianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Filter error", "Please open an image first");
                p.show();
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new GaussianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }
    /**
     * Sharpen action class
     */
    public class SharpenAction extends ImageAction{
        /**
         * <p>
         * Create a new sharpen filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        SharpenAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        /**
         * 
         * <p>
         * This method is called whenever the SharpenAction is triggered.
         * Applys an appropriately sized {@link GaussianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Sharpen error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new Sharpen());
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
