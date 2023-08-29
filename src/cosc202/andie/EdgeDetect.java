package cosc202.andie;

import java.awt.image.*;
//import java.util.*;
//import java.io.Serializable;

/**
 * Class written by Ben that does Edge Detection
 */
public class EdgeDetect implements ImageOperation, java.io.Serializable {
    public EdgeDetect() {
        
    }

    public BufferedImage apply (BufferedImage input) {

        float[] vals = new float[] {
            0.25f, 0.5f, 0.75f,
            0, 0.5f, 1,
            0.25f, 0.5f, 0.75f
        };
        // for(int i = 0; i < vals.length; i++) {
        //     vals[i] +=0.5;
        // }
        Kernel kernel = new Kernel(3,3,vals);
        
        ConvolveOp con = new ConvolveOp(kernel);
        
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        con.filter(input, output);

        return output; //Add code for filter here
    }

    public String toString(){
        return "Edge detect";
    }
}
