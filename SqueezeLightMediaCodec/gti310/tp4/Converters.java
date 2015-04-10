package gti310.tp4;
import java.lang.Error;

public class Converters {
	/*
	 * The number of dimensions in the color spaces.
	 */
	public static final int COLOR_SPACE_SIZE = 3;
	 /* The RGB color space.
	 */
	public static final int R = 0;
	public static final int G = 1;
	public static final int B = 2;
	
	/*
	 * The YUV color space.
	 */
	public static final int Y = 0;
	public static final int U = 1;
	public static final int V = 2;
	public boolean checkRange(int valeur){
		try{
			if(valeur >= 0 && valeur <= 255){
				return true;
			}
		}
		catch( Exception e ){
				 System.out.println("La valeur n'est pas entre 0 et 255");
				 return false;
		}
		return false;
		
	}
	
	public double [][][] convertRGBToYCbCr(int[][][] image){
		double[][][] newImage = new double[COLOR_SPACE_SIZE][image[0].length][image[0][0].length];
		for(int i = 0; i < image[0].length; i++) {
			for(int j = 0; j < image[0][0].length ; j++) {
				int Red = image[R][i][j];
				int Green = image[G][i][j];
				int Blue = image[B][i][j];
				newImage[Y][i][j] = validate(0.299 * Red + 0.587 * Green + 0.114 * Blue);
				newImage[U][i][j] = validate(-0.1687 * Red -0.3313 * Green + 0.5 * Blue + 128);
				newImage[V][i][j] = validate(0.5 * Red - 0.4187 * Green - 0.0813 * Blue + 128);
			}	
			
		}
		return newImage;
	}
	
	public double [][][] convertYCbCrToRGB(int[][][] image){
		double[][][] newImage = new double[COLOR_SPACE_SIZE][image[0].length][image[0][0].length];
		for(int i = 0; i < image[0].length; i++) {
			for(int j = 0; j < image[0][0].length ; j++) {
				int newY = image[Y][i][j];
				int newU = image[U][i][j];
				int newV = image[V][i][j];
				newImage[R][i][j] = validate((298.082*newY)/256 +(408.583*newV)/256 + 222.921);
				newImage[G][i][j] = validate((298.082*newY)/256 -(100.291 * newU)/256 - (208.120 * newV)/256 + 135.576);
				newImage[B][i][j] = validate((298.082*newY)/256 +(516.412 * newU)/256 - 276.836);
			}	
			
		}
		return newImage;
	}
	
	private double validate(double value){
		if(value < 0 || value > 255)
			return 0;
		else
			return value;
	}
	public double[][] getEspace(double[][][] image, int espace){
		double[][] newEspace = new double[image[0].length][image[0][0].length];
		for(int i = 0; i < image[0].length; i++) {
			for(int j = 0; j < image[0][0].length ; j++)
				newEspace[i][j] = image[espace][i][j];
			}	
		return newEspace;
	}	
		
		
}
	

