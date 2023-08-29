package cosc202.andie;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 *
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel directly
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations will need to be added.
 * </p>
 *
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 *
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {
   
    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new BrightnessAndContrastAction("Brightness and Contrast", null, "Adjusts brightness and contrast", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new PosterizeAction("Posterize", null, "Posterizes image", null));
        actions.add(new InvertAction("Invert", null, "Invert image colours", Integer.valueOf(KeyEvent.VK_T)));
        actions.add(new NotGreyAction("NotGrey", null, "Make it colourful", null));
        actions.add(new SimplifyAction("Comic-Ify", null, "Give a comic book effect to an image", null));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     *
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Colour");

        JMenuItem greyscale = new JMenuItem(actions.get(0));
        greyscale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        fileMenu.add(greyscale);

        JMenuItem brightnessAndContrast = new JMenuItem(actions.get(1));
        brightnessAndContrast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        fileMenu.add(brightnessAndContrast);

        JMenuItem posterize = new JMenuItem(actions.get(2));
        fileMenu.add(posterize);

        JMenuItem invert = new JMenuItem(actions.get(3));
        invert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        fileMenu.add(invert);

        JMenuItem notGrey = new JMenuItem(actions.get(4));
        fileMenu.add(notGrey);

        JMenuItem simplify = new JMenuItem(actions.get(5));
        fileMenu.add(simplify);
        //for(Action action: actions) {
        //    fileMenu.add(new JMenuItem(action));
        //}

        return fileMenu;
    }

    /**
     * <p>
     * Action to posterize image by limiting number of available colours
     * </p>
     * 
     * @see Posterize
     */
    public class PosterizeAction extends ImageAction{
        JLabel posterizeLabel;
        JSlider pSlider;
        int pLevel;
        int coloursAvailable;
        
        /**
         * <p>
         * Create a new posterize action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        PosterizeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the posterize action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Posterize is triggered.
         * It allows the user to posterize image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Posterize error", "Please open an image first");
                p.show();
                return;
            }
            int option = JOptionPane.showOptionDialog(null, createSliderPanel(), null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
           
            if(option == JOptionPane.CANCEL_OPTION){
                return;
            }
            else if(option == JOptionPane.OK_OPTION){
                pLevel = pSlider.getValue();
            }
            
            target.getImage().apply(new Posterize(pLevel));
            target.repaint();
            target.getParent().revalidate();
        }
        /**
         * <p>
         * Creates JPanel with slider that allows user to adjust posterization level
         * </p>
         * 
         * @return JPanel with slider that adjusts posterization level
         */
        private JPanel createSliderPanel(){
            pLevel = 0;
            coloursAvailable = (int)(Math.pow(256 / Math.pow(2, 8 - pLevel) + 1, 3));
            posterizeLabel = new JLabel("Colours available: " + coloursAvailable);
            pSlider = new JSlider(0, 0, 4, 0);
            
            JPanel sliderPanel = new JPanel();

            sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
            sliderPanel.setPreferredSize(new Dimension(250, 150));

            pSlider.setMajorTickSpacing(1);
            pSlider.setPaintTicks(true);
            pSlider.setPaintLabels(true);
            pSlider.setSnapToTicks(true);

            SliderListener sliderListener = new SliderListener();
            pSlider.addChangeListener(sliderListener);

            sliderPanel.add(pSlider);
            sliderPanel.add(posterizeLabel);

            return sliderPanel;
        }

        /**
         * Adjust "colours available" label based on slider position
         * allowing user to see exact value
         */
        private class SliderListener implements ChangeListener{
            public void stateChanged(ChangeEvent e){
                coloursAvailable = (int)(Math.pow(256 / Math.pow(2, 8 - pSlider.getValue()) + 1, 3));
                posterizeLabel.setText("Colours available: " + coloursAvailable);
            }
        }
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     *
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){      
            //Acquired Tony's idea, mine now haha 
            if(!target.getImage().hasImage()){
                Popup p = new Popup("GreyScale error", "Please open an image first");
                p.show();
                return;
            }
            
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
            
        }

    }

    /**
     * Action to adjust brightness and contrast of image
     */
    public class BrightnessAndContrastAction extends ImageAction{
        JLabel brightnessLabel;
        JLabel contrastLabel;
        JSlider bSlider;
        JSlider cSlider;
        int brightness;
        int contrast;
       
        /**
         * <p>
         * Create a new 'adjust brightness and contrast' action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        BrightnessAndContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the adjust-brightness-and-contrast action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the AdjustBrightnessAndContrast is triggered.
         * It allows the user to adjust brightness and contrast.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage()){
                Popup p = new Popup("BrightnessAndContrast error", "Please open an image first");
                p.show();
                return;
            }
            int option = JOptionPane.showOptionDialog(null, createSliderPanel(), null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
           
            if(option == JOptionPane.CANCEL_OPTION){
                return;
            }
            else if(option == JOptionPane.OK_OPTION){
                brightness = bSlider.getValue();
                contrast = cSlider.getValue();
            }

            target.getImage().apply(new AdjustBrightnessAndContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }

        /**
         * Creates panel with two sliders that allow user to adjust brightness and contrast values
         * @return JPanel with sliders that adjust brightness and contrast
         */
        private JPanel createSliderPanel(){
            brightness = 0;
            contrast = 0;
            brightnessLabel = new JLabel("Adjust brightness: " + 0 + "%");
            contrastLabel = new JLabel("Adjust contrast: " + 0 + "%");
            bSlider = new JSlider(-100, 100, 0);
            cSlider = new JSlider(-100, 100, 0);
            
            JPanel sliderPanel = new JPanel();
           
            sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
            sliderPanel.setPreferredSize(new Dimension(250, 150));

            SliderListener sliderListener = new SliderListener();
           
            bSlider.setMajorTickSpacing(50);
            bSlider.setMinorTickSpacing(10);
            bSlider.setPaintTicks(true);
            bSlider.setPaintLabels(true);
            bSlider.addChangeListener(sliderListener);

            cSlider.setMajorTickSpacing(50);
            cSlider.setMinorTickSpacing(10);
            cSlider.setPaintTicks(true);
            cSlider.setPaintLabels(true);
            cSlider.addChangeListener(sliderListener);

            sliderPanel.add(bSlider);
            sliderPanel.add(brightnessLabel);
            sliderPanel.add(cSlider);
            sliderPanel.add(contrastLabel);

            return sliderPanel;
        }

        /**
         * Adjusts brightness and contrast labels based on slider position
         * allowing user to see exact values
         */
        private class SliderListener implements ChangeListener{
            public void stateChanged(ChangeEvent e){
                brightnessLabel.setText("Adjust brightness: " +  bSlider.getValue() + "%");
                contrastLabel.setText("Adjust contrast: " + cSlider.getValue() + "%");
            }
        }
    }

    public class InvertAction extends ImageAction{


        InvertAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Invert error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new InvertFilter());
            target.repaint();
            target.getParent().revalidate();

        }

    }
    public class NotGreyAction extends ImageAction{


        NotGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Image error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new NotGrey());
            target.repaint();
            target.getParent().revalidate();

        }

    }
    public class SimplifyAction extends ImageAction{


        SimplifyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Image error", "Please open an image first");
                p.show();
                return;
            }
            target.getImage().apply(new Simplify());
            target.repaint();
            target.getParent().revalidate();

        }

    }
}
