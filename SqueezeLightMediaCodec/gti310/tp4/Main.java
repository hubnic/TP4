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
		int [][][] image;
		PPMReaderWriter reader = new PPMReaderWriter();
		image = reader.readPPMFile("C:\\Users\\Jean-Theo\\workspace\\TP4\\media-TP4\\lena.ppm");
		RGBtoYCbCrConverter converter = new RGBtoYCbCrConverter();
		double [][][] YCbCrImage = converter.convert(image);
		
		DCT dct = new DCT(0);
		
		
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
