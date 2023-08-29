package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

//import cosc202.andie.ImageTransformation.Resize;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original image 
 * and the result of applying the current set of operations to it. 
 * The operations themselves are stored on a {@link Stack}, with a second {@link Stack} 
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** Operations that have been saved */
    private Object savedOps;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;

    /** Operations that have been recorded for a .macro file */
    private Stack<ImageOperation> macroOps = new Stack<ImageOperation>();
    /** True if currently recording image operations to macroOps */
    private boolean recording;

    /**
     * <p>
     * Returns macroOps stack
     * </p>
     * @return macroOps stack
     */
    public Stack<ImageOperation> getMacroOps(){
        return macroOps;
    }

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;

        //tracks unsaved changes to image - Tony
        savedOps = ops.clone();
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage. 
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so the 
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knoweldge of some details about the internals of the BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to 
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code> added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        if(unsavedChanges()){
            int option = JOptionPane.showConfirmDialog(null, "Would you like to save changes to " + imageFilename, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if(option == JOptionPane.YES_OPTION){
                saveAs(imageFilename);
            }
            else if(option == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        
        ops.clear();
        macroOps.clear();

        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        original = ImageIO.read(imageFile);
        current = deepCopy(original);
        
        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
        }
        this.refresh();
        
        savedOps = ops.clone();
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();

        ImageIO.write(original, extension, new File(imageFilename));

        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
        
        //updates whether changes to image have been saved
        savedOps = ops.clone();
    }

    /**
     * <p>
     * Save an image to a speficied file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();

        if(extension.equals("png") || extension.equals("jpg")){
            this.imageFilename = imageFilename;
        }else{
            this.imageFilename = imageFilename + ".jpg";
        }

        this.opsFilename = this.imageFilename + ".ops";
        save();
    }

    /**
     * Exports image to selected file
     * Almost a carbon copy of save-as but with original replaced with current.
     * <p>
     * Saves the edited image to the file provided as a parameter.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void export(String imageFilename) throws Exception {
        this.imageFilename = imageFilename;
        // Write image file based on file extension
        String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
        File output = new File(imageFilename);
        if(extension.equals("png") || extension.equals("jpg")){
            ImageIO.write(current, extension, output);
        }else{
            extension = "jpg";
            output = new File(imageFilename + "."+ extension);
            ImageIO.write(current, extension, output);
        }
        //Updates whether changes to image has been saved
        savedOps = ops.clone();
    }

    /**
     * <p>
     * Applies image operations in .macro or .ops file to image
     * </p>
     * 
     * @param macroFilepath path of .micro file that will be applied to image
     * @throws Exception
     */
    public void macroApply(String macroFilepath) throws Exception{
        
        FileInputStream fileIn = new FileInputStream(macroFilepath);
        
        try{
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
        
            try{
                
                @SuppressWarnings("unchecked")
                Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
                for(ImageOperation op: opsFromFile){
                    this.apply(op);
                }
            }
            catch(Exception e){}

            objIn.close();
            fileIn.close();
            this.refresh();
        }
        catch(Exception e){
            Popup uhOh = new Popup("Corrupt macro file", "The macro file you selected is corrupted");
            uhOh.show();
        }
    }

    /**
     * <p>
     * Saves image operations in macroOps to a .macro file
     * </p>
     * 
     * @param macroFilename Name user chooses for .macro file
     * @throws Exception
     */
    public void macroSave(String macroFilename) throws Exception{
        macroFilename = macroFilename + ".macro";

        FileOutputStream fileOut = new FileOutputStream(macroFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.macroOps);
        objOut.close();
        fileOut.close();
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param input The operation to apply.
     */
    public void apply(ImageOperation input){
        current = input.apply(current);
        ops.add(input);

        if(recording) macroOps.add(input);
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * Added new statement to check if ops stack is empty.
     * </p>
     * Unlike the other effects, undo has to be checked by ops stack as imported image without 
     * any actions can bypassed the getImage() false condition - Andrew
     */
    public void undo() {
        if(ops.empty()){
            Popup p = new Popup("Undo Error", "No actions done beforehand");
            p.show();
        }else{
            redoOps.push(ops.pop());
            refresh();

            if(recording) macroOps.pop();
        }
    }

     /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * Added new statement to check if redoOps stack is empty.
     * </p>
     * Unlike the other effects, redo has to be checked by ops stack as imported image without 
     * any actions can bypassed the getImage() false condition - Andrew
     */
    public void redo()  {
        if(redoOps.empty()){
            Popup p = new Popup("Redo error", "All previous actions have been done");
            p.show();
        }else{
            apply(redoOps.pop());
        }
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in sequence.
     * This is useful when undoing changes to the image, or in any other case where {@link current}
     * cannot be easily incrementally updated. 
     * </p>
     */
    private void refresh()  {
        current = deepCopy(original);
        for (ImageOperation op: ops) {
            current = op.apply(current);
        }
    }

    /**
     * <p>
     * Checks if ops stack is synced with savedOps stack and returns
     * true if so. Keeps track of whether there are unsaved changes.
     * </p>
     * 
     * @return Whether ops stack is synced with savedOps stack
     */
    public boolean unsavedChanges(){
        return !ops.equals(savedOps);
    }

    /**
     * <p>
     * Checks if macro feature is recording image operations
     * and adding them to macroOps
     * </p>
     * 
     * @return Whether macro feature is logging image operations
     */
    public boolean getRecording(){
        return recording;
    }

    /**
     * <p>
     * Switches recording status of macro feature
     * </p>
     * 
     * @param b Sets macro recording status
     */
    public void setRecording(boolean b){
        recording = b;
    }

    /**
     * Clears macroOps stack of image operations
     */
    public void macroClear(){
        macroOps.clear();
    }
}