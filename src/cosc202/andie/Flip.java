package cosc202.andie;
import java.awt.image.*;

/**
 * Class for flip operation
 * By Andrew
 */
public class Flip implements ImageOperation, java.io.Serializable{
    private int checker;
    /**
     * Constructor
     * @param checker for value
     */
    public Flip(int checker){
        this.checker = checker;
    }
    /**
     * Where flipping magic happens. Method swaps pixels using double for-loop.
     * Mainly taken from this video: 
     * https://www.youtube.com/watch?v=HJXl2hmapdo&ab_channel=ZoranDavidovi%C4%87
     * 
     * Somehow, by not creating another BufferedImage variable, the image flips only
     * halfway. Either way its working now.
     * I used a checker so I dont have to write this method twice.
     */
    public BufferedImage apply(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if(checker == 0){
            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    image.setRGB(j, (height - 1) - i, input.getRGB(j,i));
                }
            }
        }if(checker != 0){
            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    image.setRGB((width -1) - j, i, input.getRGB(j,i));
                }
            }
            
        }
        return image;
    }

    public String toString(){
        if(checker == 0){
            return "Vertical flip";
        }else{
            return "Horizontal flip";
        }
    }
}