package gti310.tp4;
import java.lang.Error;

public class RGBtoYCbCrConverter {
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
	
	public double [][][] convert(int[][][] image){
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
	
	private double validate(double value){
		if(value < 0 || value > 255)
			return 0;
		else
			return value;
	}
}
