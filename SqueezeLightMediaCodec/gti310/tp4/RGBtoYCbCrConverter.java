package gti310.tp4;
import java.lang.Error;

public class RGBtoYCbCrConverter {
	public int[][][] convert(int[][][] imageRGB){
		
		int[][][] imageYCbCr = null;
		
		
		
		
		
		
		return imageYCbCr;
		
	}
	
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
		
	}
}
