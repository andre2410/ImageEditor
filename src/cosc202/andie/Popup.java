package cosc202.andie;
import javax.swing.*;
/**
 * Class for popup messages. Mainly for handling errors.
 * By Andrew.
 * Taken from https://stackoverflow.com/questions/7080205/popup-message-boxes for JOptionPane.
 */
public class Popup{
    private String title;
    private String message;
    /**
     * Constructor for Popup
     * @param title
     * @param message
     */
    public Popup(String title, String message){
        this.title = title;
        this.message = message;
    }
    /**
     * Method for popup message to appear
     */
    public void show(){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
