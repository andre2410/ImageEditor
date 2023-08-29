package cosc202.andie;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.imageio.*;

/*
    A new comment
    A comment from Rosie to check push and pull
*/

//Just testing some stuff -Tony
//And here's some more comments just so I can get the hang of this pushing/pulling business

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class Andie {

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * WHAT IS GOING ON
     * <p>
     * This method sets up an interface consisting of an active image (an {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save, edit, etc. 
     * These operations are implemented {@link ImageOperation}s and triggerd via 
     * {@code ImageAction}s grouped by their general purpose into menus.

     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * 
     * @throws Exception if something goes wrong.
     */
    private static void createAndShowGUI() throws Exception {
        // Set up the main GUI frame
        JFrame frame = new JFrame("ANDIE");
        try{
            Image image = ImageIO.read(new File("bin/icon.png"));
            frame.setIconImage(image);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            System.err.println("ANDIE loaded");
        }catch (Exception e){
            System.err.println("ANDIE failed to load");
        }

        // The main content area is an ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        menuBar.add(fileActions.createMenu());

        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        menuBar.add(editActions.createMenu());

        // CROP THING TO BE ADDED
        CropActions cropActions = new CropActions();
        menuBar.add(cropActions.createMenu());

        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        menuBar.add(viewActions.createMenu());

        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions();
        menuBar.add(filterActions.createMenu());

        //Rotation actions for rotation actions yes
        RotationActions rotationActions = new RotationActions();
        menuBar.add(rotationActions.createMenu());

        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        menuBar.add(colourActions.createMenu());

        //Macro actions
        MacroActions macroActions = new MacroActions();
        menuBar.add(macroActions.createMenu());
        
        frame.setJMenuBar(menuBar);

        // Adds the toolbar to the left side of the frame
        ToolBar tBar = new ToolBar();
        JToolBar toolBar = tBar.createToolBar();
        frame.add(toolBar, BorderLayout.WEST);

        // Adds the drawing toolbar to the top of the frame
        DrawToolBar drawBar = new DrawToolBar();
        JToolBar drawToolBar = drawBar.createToolBar();
        frame.add(drawToolBar, BorderLayout.NORTH);

        //If user clicks on the exit button with unsaved changes 
        //Informs user about unsaved changes and askes if they still want to exit 
        //code from http://www.java2s.com/Tutorials/Java/Swing_How_to/JOptionPane/Show_confirmation_dialog_for_closing_JFrame.htm
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
              if(imagePanel.getImage().unsavedChanges()){
                int result = JOptionPane.showConfirmDialog(frame,
                    "You have unsaved changes. Do you want to Exit ?", "Exit Confirmation : ",
                    JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                else if (result == JOptionPane.NO_OPTION)
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
              }else{
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              }
            }
          });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Allows FileActions to repack from to size of image that's been opened
        fileActions.setFrame(frame);
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception ex) {
                    System.out.println(ex);
                    System.exit(1);
                }
            }
        });
    }
}
