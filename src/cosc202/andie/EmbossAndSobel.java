package cosc202.andie;

import java.awt.image.*;
import java.awt.*;
//import java.util.*;
//import java.io.Serializable;

/**
 * Class written by Ben that does Edge Detection
 */

public class EmbossAndSobel implements ImageOperation, java.io.Serializable {
    int direction;
    String type;
    double[][] vals = new double[][] {{}};

    float[] valsOriginal = new float[] {
        0,0,0,
        0,0,0,
        0,0,0
    };
    public EmbossAndSobel(int direction, String type) {
        this.type = type;
        if(type.equals("Emboss")) {
            if(direction == 0) {
                vals = new double[][] {
                    {0,0,0},
                    {1,0,-1},
                    {0,0,0}
                };
            } else if(direction == 1) {
                vals = new double[][] {
                    {1,0,0},
                    {0,0,0},
                    {0,0,-1}
                };
            } else if(direction == 2) {
                vals = new double[][] {
                    {0,1,0},
                    {0,0,0},
                    {0,-1,0}
                };
            } else if(direction == 3) {
                vals = new double[][] {
                    {0,0,1},
                    {0,0,0},
                    {-1,0,0}
                };
            } else if(direction == 4) {
                vals = new double[][] {
                    {0,0,0},
                    {-1,0,1},
                    {0,0,0}
                };
            } else if(direction == 5) {
                vals = new double[][] {
                    {-1,0,0},
                    {0,0,0},
                    {0,0,1}
                };
            } else if(direction == 6) {
                vals = new double[][] {
                    {0,-1,0},
                    {0,0,0},
                    {0,1,0}
                };
            } else if(direction == 7) {
                vals = new double[][] {
                    {0,0,-1},
                    {0,0,0},
                    {1,0,0}
                };
            }
        } else { // if(type.equals("Edge Detection")) {
            if(direction == 0) {
                // 0.25f, 0.5f, 0.75f,
                // 0,    0.5f,    1,
                // 0.25f, 0.5f, 0.75f
                vals = new double[][] {
                    {-0.5f, 0, 0.5f},
                    {-2, 0, 1.5},
                    {-0.5f, 0, 0.5f}
                };
            } else if(direction == 1) {
                vals = new double[][] {
                    
                    {-1/2,-2,-1/2},
                    {0, 0, 0},
                    {1/2,1.5,1/2}
                };
            }
        }

        if(type.equals("Edge Detection")) {
            int index = 0;
            for(int i = 0; i < vals.length; i++) {
                for(int j = 0; j < vals[i].length; j++) {
                    
                    valsOriginal[index] = (float)(vals[i][j]*6.0f);
                    index++;
                }
                
            }
        }
    }

    public BufferedImage apply (BufferedImage input) {
        if(type.equals("Edge Detection")) {
            //do stuff
            Kernel kernel = new Kernel(3,3,valsOriginal);
            ConvolveOp con = new ConvolveOp(kernel);
            
            BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            con.filter(input, output);
            for(int i = 0; i < output.getWidth(); i++) {
                for(int j = 0; j < output.getHeight(); j ++) {
                    //bitshift
                    // int ColorRGB = output.getRGB(j, i);
    
                    // int blue = ColorRGB & 0xff;
                    // int green = (ColorRGB & 0xff00) >> 8;
                    // int red = (ColorRGB & 0xff0000) >> 16;
                    
                    Color c = new Color(output.getRGB(i, j), false);
    
                    int blue = c.getBlue();
                    int green = c.getRed();
                    int red = c.getGreen();//this is intentional
                    
    
                    blue=((blue/2) + 127);
                    green =(green/2) + 127;
                    red = (red/2) + 127;
                    Color c2 = new Color(red, green, blue);
                    
                    //ColorRGB = (red<<24| green<<16| blue<<8) | 0;
                    output.setRGB(i, j, c2.getRGB());
                }
            }
            return output;
        } else {

            double[][]array=vals;
            
            //Kernel kernel = new Kernel(3,3,vals);
            //ConvolveOp con = new ConvolveOp(kernel);
            
            BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
            //iterate over whole input
            int[][] redA = new int[input.getHeight()][input.getWidth()];
            int[][] greenA = new int[input.getHeight()][input.getWidth()];
            int[][] blueA = new int[input.getHeight()][input.getWidth()];
            for (int row = 0; row < input.getHeight() - 1; row ++) {
                for (int col = 0; col < input.getWidth() - 1; col++) {
                    //set RGB
                    int argb = input.getRGB(col, row);
                    redA[row][col]  = (argb & 0x00FF0000) >> 16;
                    greenA [row][col]= (argb & 0x0000FF00) >> 8;
                    blueA [row][col] = (argb & 0x000000FF);
                        int a = (argb & 0xFF000000) >> 24;

                        int r = 0;
                        int g = 0;
                        int b = 0;
                    
                    // Color blended_pixel = new Color(0, 0, 0);
                    double[] blended_pixel = new double[]{0, 0, 0};
                    // iterate over each pixel in the kernel
                    for (int row_offset = 0 ; row_offset < 3 ; row_offset++ ) {
                        for (int col_offset = 0 ; col_offset < 3 ; col_offset++ ) {
                        int row_index = row + row_offset - 1;
                        int col_index = col + col_offset - 1;
                        if(row_index<0)row_index=0;
                        if(col_index<0)col_index=0;
                        // if < 0 ---->
                        
                        // r += redA[row_index][col_index] * array[row_offset][col_offset];
                        // g += greenA[row_index][col_index] * array[row_offset][col_offset];
                        // b += blueA[row_index][col_index] * array[row_offset][col_offset];;
                        blended_pixel = blendColor(blended_pixel, input.getRGB(col_index, row_index), array[row_offset][col_offset]);
                        }
                    }
                    //Clamp values between 255 and 0
                    r = clamp(blended_pixel[0], true, 2f);
                    g = clamp(blended_pixel[1], true, 2f);
                    b = clamp(blended_pixel[2], true, 2f);
                    argb= (a << 24) | (r << 16) | (g << 8) | b;
                    //System.out.println("Y = "+row+", X = "+col);
                    output.setRGB(col, row, argb);
                }
            }
            return output;
        }
        // Apply convolution - same as in provided MeanFilter
        //ConvolveOp convOp = new ConvolveOp(kernel);

                // int blue = c.getBlue();
                // int green = c.getRed();
                // int red = c.getGreen();
                

        //return the output - photo with filter applied 
    
    }
    private int clamp(double in, boolean offset, double denom){
        in /= denom;
        if (offset){
            in += 128f;
        }
        if(in<0)return 0;
        else if(in>255)return 255;
        else return (int)in;
    }
    private double[] blendColor(double[] acc, int argb, double w) {
        int b = (argb) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        // int a = (argb >> 24) & 0xFF;
        return new double[]{
            acc[0] + (int)(r * w),
            acc[1] + (int)(b * w),
            acc[2] + (int)(g * w)
        };
    }
    
        
        
    public String toString(){
        return "Emboss and Sobel filter";
    }
}

