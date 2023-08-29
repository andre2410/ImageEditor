package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

/**
 * <p>
 * Class for creating the toolbar used in ANDIE
 * </p>
 * 
 * <p>
 * This class creates the toolbar that is added to the ANDIE JFrame
 * Buttons that call common operations have been added to the toolbar
 * </p>
 * 
 * <a href="https://www.flaticon.com/free-icons/graphic-tools" title="graphic tools icons">Graphic tools icons created by Cuputo - Flaticon</a>
 * https://www.flaticon.com/premium-icon/zoom-out_3682511
 * 
 * <a href="https://www.flaticon.com/free-icons/graphic-tools" title="graphic tools icons">Graphic tools icons created by Cuputo - Flaticon</a>
 * https://www.flaticon.com/premium-icon/zoom-in_3682510
 * 
 * <a href="https://www.flaticon.com/free-icons/redo" title="redo icons">Redo icons created by Cuputo - Flaticon</a>
 * https://www.flaticon.com/premium-icon/redo_3682472
 * 
 * <a href="https://www.flaticon.com/free-icons/undo" title="undo icons">Undo icons created by Cuputo - Flaticon</a>
 * https://www.flaticon.com/premium-icon/undo_3682494
 * 
 * <a href="https://www.flaticon.com/free-icons/ui" title="ui icons">Ui icons created by nawicon - Flaticon</a>
 * https://www.flaticon.com/premium-icon/rotate_4211866
 * 
 * <a href="https://www.flaticon.com/free-icons/ui" title="ui icons">Ui icons created by nawicon - Flaticon</a>
 * https://www.flaticon.com/premium-icon/rotate_4211869
 * 
 * <a href="https://www.flaticon.com/free-icons/save" title="save icons">Save icons created by Ardiansyah - Flaticon</a>
 * https://www.flaticon.com/premium-icon/save_2319076
 * 
 * <a href="https://www.flaticon.com/free-icons/flip" title="flip icons">Flip icons created by nawicon - Flaticon</a>
 * https://www.flaticon.com/premium-icon/flip_4211543?related_id=4211543&origin=pack
 * 
 * <a href="https://www.flaticon.com/free-icons/vertical" title="vertical icons">Vertical icons created by nawicon - Flaticon</a>
 * https://www.flaticon.com/premium-icon/flip_4211545?related_id=4211545&origin=pack
 * 
 * @author Tony Cenaiko
 */
public class ToolBar{
    JToolBar toolBar = new JToolBar(1);
    
    FileActions fileActions = new FileActions();
    ColourActions colourActions = new ColourActions();
    ViewActions viewActions = new ViewActions();
    EditActions editActions = new EditActions();
    RotationActions rotationActions = new RotationActions();

    private JButton saveButton = new JButton();
    private JButton zoomInButton = new JButton();
    private JButton zoomOutButton = new JButton();
    private JButton undoButton = new JButton();
    private JButton redoButton = new JButton();
    private JButton rotateRightButton = new JButton();
    private JButton rotateLeftButton = new JButton();
    private JButton flipHorizontalButton = new JButton();
    private JButton flipVerticalButton = new JButton();
    
    public ToolBar(){}

    /**
     * <p>
     * Creates JToolBar and adds relevant JButtons
     * Connects JButtons to action listener that calls relevant operations
     * </p>
     * 
     * @return JToolBar that's added to ANDIE's JFrame
     * @throws Exception
     */
    public JToolBar createToolBar() throws Exception{
        ActionListenerClass actionListener = new ActionListenerClass();
        
        addButton(saveButton, "src/cosc202/andie/ToolBarIcons/save.png", "save", actionListener);
        addButton(zoomInButton, "src/cosc202/andie/ToolBarIcons/zoom-in.png", "zoom in", actionListener);
        addButton(zoomOutButton, "src/cosc202/andie/ToolBarIcons/zoom-out.png", "zoom out", actionListener);
        addButton(undoButton, "src/cosc202/andie/ToolBarIcons/undo.png", "undo", actionListener);
        addButton(redoButton, "src/cosc202/andie/ToolBarIcons/redo.png", "redo", actionListener);
        addButton(rotateRightButton, "src/cosc202/andie/ToolBarIcons/rotateRight.png", "rotate right", actionListener);
        addButton(rotateLeftButton, "src/cosc202/andie/ToolBarIcons/rotateLeft.png", "rotate left", actionListener);
        addButton(flipHorizontalButton, "src/cosc202/andie/ToolBarIcons/flipHorizontal.png", "flip horizontal", actionListener);
        addButton(flipVerticalButton, "src/cosc202/andie/ToolBarIcons/flipVertical.png", "flip vertical", actionListener);
        
        return toolBar;
    }

    /**
     * <p>
     * Adds features to JButtons and adds them to toolbar
     * </p>
     * 
     * <p>
     * Takes in JButton and adds icon (or name of button if file path of icon throws exception)
     * Adds ActionListener to button, connecting it to image operations
     * Adds button to toolbar
     * </p>
     * 
     * @param button JButton that method is initialized
     * @param filePath File path to icon that is added to the button
     * @param buttonName Name of the button in case file path fails
     * @param actionListener ActionListener that connects button to various image operations
     * @throws Exception
     */
    protected void addButton(JButton button, String filePath, String buttonName, ActionListenerClass actionListener) throws Exception{
        try{
            Image icon = ImageIO.read(new File(filePath));
            Image image = icon.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(image);
            
            button.setIcon(imageIcon);
        }
        catch(Exception e){
            button.setText(buttonName);
        }
        
        button.addActionListener(actionListener);
        toolBar.add(button);
    }

    /**
     * <p>
     * Connects JButtons to various operations
     * </p>
     */
    private class ActionListenerClass implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == saveButton){
                fileActions.actions.get(1).actionPerformed(e);
            }
            else if(e.getSource() == zoomInButton){
                viewActions.actions.get(0).actionPerformed(e);
            }
            else if(e.getSource() == zoomOutButton){
                viewActions.actions.get(1).actionPerformed(e);
            }
            else if(e.getSource() == undoButton){
                editActions.actions.get(0).actionPerformed(e);
            }
            else if(e.getSource() == redoButton){
                editActions.actions.get(1).actionPerformed(e);
            }
            else if(e.getSource() == rotateRightButton){
                rotationActions.setRotationAngle(90);
            }
            else if(e.getSource() == rotateLeftButton){
                rotationActions.setRotationAngle(-90);
            }
            else if(e.getSource() == flipHorizontalButton){
                rotationActions.actions.get(4).actionPerformed(e);
            }
            else if(e.getSource() == flipVerticalButton){
                rotationActions.actions.get(3).actionPerformed(e);
            }
        }
    }
}