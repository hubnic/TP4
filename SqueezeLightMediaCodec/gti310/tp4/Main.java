package gti310.tp4;

/**
 * The Main class is where the different functions are called to either encode
 * a PPM file to the Squeeze-Light format or to decode a Squeeze-Ligth image
 * into PPM format. It is the implementation of the simplified JPEG block 
 * diagrams.
 * 
 * @author François Caron
 */
public class Main {

	/*
	 * The entire application assumes that the blocks are 8x8 squares.
	 */
	public static final int BLOCK_SIZE = 8;
	
	/*
	 * The number of dimensions in the color spaces.
	 */
	public static final int COLOR_SPACE_SIZE = 3;
	
	/*
	 * The RGB color space.
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
	
	/**
	 * The application's entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Squeeze Light Media Codec !");
		int [][][] imageIn;
		PPMReaderWriter reader = new PPMReaderWriter();
		//image = reader.readPPMFile("C:\\Users\\Jean-Theo\\workspace\\TP4\\media-TP4\\lena.ppm");
		imageIn = reader.readPPMFile(args[1]);
		Converters converter = new Converters();
		newDCT dct = new newDCT();
		Quantification quantification = new Quantification();
		
		//la qualite est passee en parametre
		int qualite = Integer.parseInt(args[0]);
		
		//on converti l<image de RGB a YCbCr
		double[][][] YCbCrImage = converter.convertRGBToYCbCr(imageIn);
		
		//on separe les espace Y, Cb, Cr, choix personnel pour la manipulation du fichier
		double[][] YEspace = converter.getEspace(YCbCrImage, 0);
		double[][] CbEspace = converter.getEspace(YCbCrImage, 1);
		double[][] CrEspace = converter.getEspace(YCbCrImage, 2);
		
		//on effectue le dct sur les 3 espaces
		double[][] YEspaceDCT = dct.appliquerDCT(YEspace);
		double[][] CbEspaceDCT = dct.appliquerDCT(CbEspace);
		double[][] CrEspaceDCT = dct.appliquerDCT(CrEspace);
		
		//on effectue la quantification sur les 3 espaces
		double[][] YEspaceQt = quantification.appliquerQuantificationY(YEspaceDCT, qualite);
		double[][] CbEspaceQt = quantification.appliquerQuantificationCbCr(CbEspaceDCT, qualite);
		double[][] CrEspaceQt = quantification.appliquerQuantificationCbCr(CrEspaceDCT, qualite);
		
		/*int offset = 0;
		for(int i = 0; i < YCbCrImage[0].length; i++) {
			for(int j = 0; j < YCbCrImage[0][0].length ; j++) {
				System.out.println(YCbCrImage[Main.R][i][j]);
				System.out.println(YCbCrImage[Main.G][i][j]);
				System.out.println(YCbCrImage[Main.B][i][j]);
				offset += 3;
			}
			System.out.println("\n");
		}*/
	}
}
