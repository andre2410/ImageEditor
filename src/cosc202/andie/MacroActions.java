package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;

/**
 * <p>
 * Actions provided by the Macro menu
 * </p>
 * 
 * <p>
 * The Macro menu contains actions that record and store image operations 
 * in a macro file that can be applied to other images
 * </p>
 * 
 * @author Tony Cenaiko
 */
public class MacroActions {
    protected ArrayList<Action> actions;
    protected Border redBorder = BorderFactory.createLineBorder(Color.RED, 5);
    protected Border invisibleBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 0);

    /**
     * <p>
     * Create a set of Macro menu actions
     * </p>
     */
    public MacroActions(){
        actions = new ArrayList<Action>();
        actions.add(new MacroStart("Start Record", null, "Begins recording", null));
        actions.add(new MacroStop("End Record", null, "Stops recording", null));
        actions.add(new MacroSave("Save", null, "Saves macros", null));
        actions.add(new MacroApply("Apply", null, "Applies macros", null));
        actions.add(new MacroClear("Clear", null, "Clears macro operations", null));
        actions.add(new MacroInspect("Inspect", null, "Inspect macro file", null));
    }

    /**
     * <p>
     * Create a menu containing the list of Macro actions
     * </p>
     * 
     * @return The Macro menu UI element
     */
    public JMenu createMenu(){
        JMenu macroMenu = new JMenu("Macro");

        JMenuItem macroStart = new JMenuItem(actions.get(0));
        macroMenu.add(macroStart);
        JMenuItem macroStop = new JMenuItem(actions.get(1));
        macroMenu.add(macroStop);
        JMenuItem macroSave = new JMenuItem(actions.get(2));
        macroMenu.add(macroSave);
        JMenuItem macroApply = new JMenuItem(actions.get(3));
        macroMenu.add(macroApply);
        JMenuItem macroClear = new JMenuItem(actions.get(4));
        macroMenu.add(macroClear);
        JMenuItem macroInspect = new JMenuItem(actions.get(5));
        macroMenu.add(macroInspect);

        return macroMenu;
    }

    /**
     * <p>
     * Action to clear macro stack of image operations
     * </p>
     */
    public class MacroClear extends ImageAction{
        
        /**
         * <p>
         * Create a new Macro Clear action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroClear(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Clear action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Clear is triggered.
         * It allows the user to clear the macro stack.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            target.getImage().macroClear();
        }
    }

    /**
     * <p>
     * Action to inspect contents of macro and ops files
     * </p>
     */
    public class MacroInspect extends ImageAction{

        /**
         * <p>
         * Create a new Macro Inspect action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroInspect(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Inspect action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Inspect is triggered.
         * It allows the user to inspect contents of .macro and .ops files.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Apply Macro");

            javax.swing.filechooser.FileFilter filter = new FileNameExtensionFilter("Macros", "macro", "ops");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showOpenDialog(target);

            if(result == JFileChooser.APPROVE_OPTION){
                try{
                    String macroFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    inspect(macroFilepath);
                }
                catch(Exception ex){
                    System.exit(1);
                }
            }
        }

        /**
         * <p>
         * Creates JOptionPane that shows contents of .macro and .ops files
         * </p>
         * 
         * @param macroFilepath File path of .macro or .ops file
         * @throws Exception
         */
        public void inspect(String macroFilepath) throws Exception{
            FileInputStream fileIn = new FileInputStream(macroFilepath);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            try{
                @SuppressWarnings("unchecked")
                Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
                
                int number = 1;
                for(ImageOperation op: opsFromFile){
                    JLabel label = new JLabel(number + ". " + op.toString());
                    number++;
                    panel.add(label);
                    JLabel newLine = new JLabel("\n");
                    panel.add(newLine);
                }

                JOptionPane.showMessageDialog(null, panel, "Image operations", JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception e){}

            objIn.close();
            fileIn.close();
        }
    }

    /**
     * <p>
     * Action to apply contents of .macro or .ops file to image
     * </p>
     */
    public class MacroApply extends ImageAction{

        /**
         * <p>
         * Create a new Macro Apply action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroApply(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Apply action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Apply is triggered.
         * It allows the user to apply image operations of a 
         * .macro or .ops file to an image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Macro error", "Please open an image first");
                p.show();
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Apply Macro");

            javax.swing.filechooser.FileFilter filter = new FileNameExtensionFilter("Macros", "macro", "ops");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showOpenDialog(target);

            if(result == JFileChooser.APPROVE_OPTION){
                try{
                    String macroFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().macroApply(macroFilepath);
                    target.repaint();
                    target.getParent().revalidate();
                }
                catch(Exception ex){
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Action to save contents of macro stack to .macro file
     */
    public class MacroSave extends ImageAction{

        /**
         * <p>
         * Create a new Macro Save action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroSave(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Save action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Save is triggered.
         * It allows the user to save contents of macro stack
         * to a .macro file.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Macro");
            int result = fileChooser.showSaveDialog(target);

            if(result == JFileChooser.APPROVE_OPTION){
                try{
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().macroSave(imageFilepath);
                }
                catch(Exception ex){
                    System.exit(1);
                }
            }
        }
    }

    /**
     * <p>
     * Action to start recording image operations to macro stack
     * </p>
     */
    public class MacroStart extends ImageAction{
        
        /**
         * <p>
         * Create a new Macro Start action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroStart(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Start action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Start is triggered.
         * It allows the user to start recording image operations
         * by adding them to the macro stack.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if(!target.getImage().hasImage()){
                Popup p = new Popup("Macro error", "Please open an image first");
                p.show();
                return;
            }
            
            if(!target.getImage().getRecording()){
                target.getImage().setRecording(true);
                
                target.setBorder(redBorder);
            }
        }
    }

    /**
     * <p>
     * Action to stop recroding image operations to macro stack
     * and prompt user to save contents of stack to .macro file
     * </p>
     */
    public class MacroStop extends ImageAction{
        
        /**
         * <p>
         * Create a new Macro Stop action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MacroStop(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Stop action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever Stop is triggered.
         * It allows the user to stop adding image operations
         * to the macro stack and prompts user to save macro stack
         * to a .macro file.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if(target.getImage().getRecording()){
                target.getImage().setRecording(false);

                target.setBorder(invisibleBorder);

                Stack<ImageOperation> macroOps = target.getImage().getMacroOps();
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
                JLabel inquiry = new JLabel("Would you like to save this macro?");
                panel.add(inquiry);
                JLabel newLine = new JLabel("\n");
                panel.add(newLine);
                panel.add(newLine);

                int number = 1;
                for(ImageOperation op: macroOps){
                    JLabel label = new JLabel(number + ". " + op.toString());
                    number++;
                    panel.add(label);
                    panel.add(newLine);
                }

                int result = JOptionPane.showConfirmDialog(null, panel, "Save Confirmation: ", JOptionPane.YES_NO_OPTION);
                
                if(result == JOptionPane.YES_OPTION){
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Macro");
                    int fileChooserResult = fileChooser.showSaveDialog(target);

                    if(fileChooserResult == JFileChooser.APPROVE_OPTION){
                        try{
                            String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                            target.getImage().macroSave(imageFilepath);
                        }
                        catch(Exception ex){
                            System.exit(1);
                        }
                    }
                }
            }
        }
    }
}
