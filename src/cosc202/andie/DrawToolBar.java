package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

/**
 * <p>
 * Class for creating the draw toolbar used in ANDIE
 * </p>
 * 
 * <p>
 * This class creates the toolbar that is added to the ANDIE JFrame
 * the dropdown lets users select a shape and the button lets the user select a colour
 * </p>
 * 
 * 
 * @author Rosie Bicknell
 */
public class DrawToolBar{
    JToolBar toolBar = new JToolBar(1);

    //drawing
    private JButton colourSelectButton = new JButton();

    //dropdown
    private String[] shapeChoices;
    protected static JComboBox shapeSelector;
    protected static JRadioButton fill;
    protected static Color chosenColour;
    ActionListenerClass actionListener;

    public DrawToolBar(){}

    /**
     * <p>
     * Creates JToolBar and adds drop down shape selector and colour selector button.  
     * The button has an action listener added to it to respond to the user
     * </p>
     * 
     * @return JToolBar that's added to ANDIE's JFrame
     * @throws Exception
     */
    public JToolBar createToolBar() throws Exception{
         actionListener = new ActionListenerClass();

        //shape choices
        shapeChoices = new String[]{"No Drawing", "Rectangle", "Oval", "Line"};
        shapeSelector = new JComboBox(shapeChoices);
        shapeSelector.setSelectedItem(0);
        toolBar.add(shapeSelector);

        //colour selector button
        colourSelectButton = new JButton("Select Colour");
        colourSelectButton.addActionListener(actionListener);
        toolBar.add(colourSelectButton);

        //jradiobutton
        fill = new JRadioButton("Fill");
        toolBar.add(fill);
        
        return toolBar;
    }
    /**
     * ActionListener class which listens to colour selector button
     * is activated when the user wants to choose a colour and provides a JColorChooser 
     */
    private class ActionListenerClass implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //bring up colour selector
            Color defaultColour = Color.BLACK;
            chosenColour = JColorChooser.showDialog(toolBar,"Select a color",defaultColour);
        }
    }

}