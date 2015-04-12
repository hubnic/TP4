package gti310.tp4;
/**
* <h3><b>DCT - A Java implementation of the Discreet Cosine Transform</b></h3><br><br>
* <hr>
* The discreet cosine transform converts spatial information to "frequency" or
* spectral information, with the X and Y axes representing frequencies of the
* signal in different dimensions. This allows for "lossy" compression of image
* data by determining which information can be thrown away without compromising
* the image.<br><br>
* The DCT is used in many compression and transmission codecs, such as JPEG, MPEG
* and others. The pixels when transformed are arraged from the most signifigant pixel
* to the least signifigant pixel. The DCT functions themselves are lossless.
* Pixel loss occurs when the least signifigant pixels are quantitized to 0.
* <br><br>
* This is NOT a JPEG or JFIF compliant implementation however it could
* be with very little extra work. (i.e. A huffman encoding stage needs
* to be added.) I am making this source availible in the hopes that
* someone will add this functionality to the class, if you do, please
* email me! As always, if you have any problems feel free to contact
* me. (Or comments, or praise, etc..)
* <br><br>
* <b>Keep in mind</b> that when compressing color images with this, you will
* need to break the image up into it's R G B components and preform
* the calculations three times!!
* <br><br>
* <b>A general algorithim for DCT compression with this class:</b> <br><br>
* 1) Create a DCT Object.<br>
* 2) Set up your program to read pixel information in 8*8 blocks. See example.<br>
* 3) Run the forwardDCT() on all blocks.<br>
* 4) Run the quantitizeImage() on all blocks.<br>
* 5) If you want, send the information to the imageCompressor().<br><br>
* <b>A general algorithim for DCT decompression with this class:</b> <br><br>
* 1) Create a DCT Object. <br>
* 2) Set up the program to convert compressed data in 8*8 blocks. (if compressed)<br>
* 3) Run the data through dequantitizeImage(). <br>
* 4) Run the data through inverseDCT().<br>
* <br><br>
* A complete implementation of an image compressor which compares
* the quality of the two images is also availible. See the
* JEncode/Decode <a href="JEncode.java">source code.</a> The
* <a href="DCT.java">DCT.java source code</a> is also availible.
* The implementation is also handy for seeing how to break the image
* down, read in 8x8 blocks, and reconstruct it. The <a href="steve.jpg">
* sample graphic</a> is handy to have too. (Bad pic of me)
* The best way to get ahold of me is through my
* <a href="http://eagle.uccb.ns.ca/steve/home.html">homepage</a>. There's
* lots of goodies there too.
* @version 1.0.1 August 22nd 1996
* @author <a href="http://eagle.uccb.ns.ca/steve/home.html">Stephen Manley</a> - smanley@eagle.uccb.ns.ca
*/
public class DCT
{
    /**
     * DCT Block Size - default 8
     */
    public int N = 8;

    /**
     * Qualit� de 0 � 100
     */
    public int QUALITY = 25;
    
    
    /**
     * Y,Cb et Cr separees en 3 matrices
     */
    public int[][] yMatrix;
    public int[][] cbMatrix;
    public int[][] crMatrix;

    /**
     * Image width - must correspond to imageArray bounds - default 320
     */
    public int ROWS = 320;

    /**
     * Image height - must correspond to imageArray bounds - default 200
     */
    public int COLS = 240;

    /**
     * The ZigZag matrix.
     */
    public int zigZag[][] = new int[64][2];

    /**
     * Cosine matrix. N * N.
     */
    public double c[][] = new double[N][N];

    /**
     * Transformed cosine matrix, N*N.
     */
    public double cT[][] = new double[N][N];

    /**
     * Quantitization Matrix.
     */
    public int quantum[][] = new int[N][N];

    /**
     * DCT Result Matrix
     */
    public int resultDCT[][] = new int[ROWS][COLS];

    /**
     * Constructs a new DCT object. Initializes the cosine transform matrix
     * these are used when computing the DCT and it's inverse. This also
     * initializes the run length counters and the ZigZag sequence. Note that
     * the image quality can be worse than 25 however the image will be
     * extemely pixelated, usually to a block size of N.
     *
     * @param QUALITY The quality of the image (0 best - 25 worst)
     *
     */
    public DCT(int QUALITY)
    {
    	
        initZigZag();
        initMatrix(QUALITY);
    }


    /**
     * This method sets up the quantization matrix using the Quality parameter
     * and then sets up the Cosine Transform Matrix and the Transposed CT.
     * These are used by the forward and inverse DCT. The RLE encoding
     * variables are set up to track the number of consecutive zero values
     * that have output or will be input.
     * @param quality The quality scaling factor
     */
    private void initMatrix(int quality)
    {
        int i;
        int j;

        for (i = 0; i < 8; i++)
        {
            for (j = 0; j < 8; j++)
            {
                quantum[i][j] = (1 + ((1 + i + j) * quality));
            }
        }

        for (j = 0; j < 8; j++)
        {
            double nn = (double)(8);
            c[0][j]  = 1.0 / Math.sqrt(nn);
            cT[j][0] = c[0][j];
        }

        for (i = 1; i < 8; i++)
        {
            for (j = 0; j < 8; j++)
            {
                double jj = (double)j;
                double ii = (double)i;
                c[i][j]  = Math.sqrt(2.0/8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
                cT[j][i] = c[i][j];
            }
        }
    }

    /**
     * This method preforms a matrix multiplication of the input pixel data matrix
     * by the transposed cosine matrix and store the result in a temporary
     * N * N matrix. This N * N matrix is then multiplied by the cosine matrix
     * and the result is stored in the output matrix.
     *
     * @param input The Input Pixel Matrix
     * @returns output The DCT Result Matrix
     */
    public int[][] forwardDCT(char input[][])
    {
        int output[][] = new int[N][N];
        double temp[][] = new double[N][N];
        double temp1;
        int i;
        int j;
        int k;

        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                temp[i][j] = 0.0;
                for (k = 0; k < N; k++)
                {
                    temp[i][j] += (((int)(input[i][k]) - 128) * cT[k][j]);
                }
            }
        }

        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                temp1 = 0.0;

                for (k = 0; k < N; k++)
                {
                    temp1 += (c[i][k] * temp[k][j]);
                }

                output[i][j] = (int)Math.round(temp1);
            }
        }

        return output;
    }

    /**
     * This method reads in DCT codes  dequanitizes them
     * and places them in the correct location. The codes are stored in the
     * zigzag format so they need to be redirected to a N * N block through
     * simple table lookup. After dequantitization the data needs to be
     * run through an inverse DCT.
     *
     * @param inputData 8x8 Array of quantitized image data
     * @param zigzag Boolean switch to enable/disable zigzag path.
     * @returns outputData A N * N array of de-quantitized data
     *
     */
    public int[][] dequantitizeImage(int[][] inputData, boolean zigzag)
    {
        int i = 0;
        int j = 0;
        int a = 0;
        int b = 0;
        int row;
        int col;
        int outputData[][] = new int[N][N];

        double result;

        if (zigzag)
        {
            for (i=0; i<(N*N); i++)
            {
                row = zigZag[i][0];
                col = zigZag[i][1];

                result = inputData[row][col] * quantum[row][col];
                outputData[row][col] = (int)(Math.round(result));
            }
        }

        else
        {
            for (i=0; i<8; i++)
            {
                for (j=0; j<8; j++)
                {
                    result = inputData[i][j] * quantum[i][j];
                    outputData[i][j] = (int)(Math.round(result));
                }
            }
        }

        return outputData;
    }

    /**
     * This method orders the DCT result matrix into a zigzag pattern and then
     * quantitizes the data. The quantitized value is rounded to the nearest integer.
     * Pixels which round or divide to zero are the loss associated with
     * quantitizing the image. These pixels do not display in the AWT. (null)
     * Long runs of zeros and the small ints produced through this technique
     * are responsible for the small image sizes. For block sizes < or > 8,
     * disable the zigzag optimization. If zigzag is disabled on encode it
     * must be disabled on decode as well.
     *
     * @param inputData 8x8 array of DCT image data.
     * @param zigzag Boolean switch to enable/disable zigzag path.
     * @returns outputData The quantitized output data
     */
    public int[][] quantitizeImage(int inputData[][], boolean zigzag)
    {
        int outputData[][] = new int[N][N];
        int i = 0;
        int j = 0;
        int a = 0;
        int b = 0;
        int row;
        int col;

        double result;

        if (zigzag)
        {
            for (i = 0; i < (N*N); i++)
            {
                row = zigZag[i][0];
                col = zigZag[i][1];
                result = (inputData[row][col] / quantum[row][col]);
                outputData[row][col] = (int)(Math.round(result));
            }

        }

        else
        {
            for (i=0; i<N; i++)
            {
                for (j=0; j<N; j++)
                {
                    result = inputData[i][j] / quantum[i][j];
                    outputData[i][j] = (int)(Math.round(result));
                }
            }
        }

        return outputData;
    }

    /**
     * <br><br>
     * This does not huffman code,
     * it uses a minimal run-length encoding scheme. Huffman, Adaptive Huffman
     * or arithmetic encoding will give much better preformance. The information
     * accepted is quantitized DCT data. The output array should be
     * scanned to determine where the end is.
     *
     * @param image Quantitized image data.
     * @returns The string representation of the image. (Compressed)
     */
    public int[] compressImage(int[] QDCT, boolean log)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int temp = 0;
        int curPos = 0;
        int runCounter = 0;
        int imageLength = ROWS*COLS;

        int pixel[] = new int[ROWS*COLS];

        while((i<imageLength))
        {
            temp = QDCT[i];

            while((i < imageLength) && (temp == QDCT[i]))
            {
                runCounter++;
                i++;
            }

            if (runCounter > 4)
            {
                pixel[j] = 255;
                j++;
                pixel[j] = temp;
                j++;
                pixel[j] = runCounter;
                j++;
            }
            else
            {
                for (k=0; k<runCounter; k++)
                {
                    pixel[j] = temp;
                    j++;
                }
            }

            if (log)
            {
                System.out.print("." + "\r");
            }

            runCounter = 0;
            //i++;
        }

        return pixel;
    }

    /**
     * This method determines the runs in the input data, decodes it
     * and then returnss the corrected matrix. It is used to decode the data
     * from the compressImage method. Huffman encoding, Adaptive Huffman or
     * Arithmetic will give much better compression.
     *
     * @param DCT Compressed DCT int array (Expands to whole image).
     * @returns The decompressed one dimensional array.
     */
    public int[] decompressImage(int[] DCT, boolean log)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int temp = 0;
        int imageLength = ROWS*COLS;
        int pixel[] = new int[ROWS*COLS];

        while (i < imageLength)
        {
            temp = DCT[i];

            if (k < imageLength)
            {
                if (temp == 255)
                {
                    i++;
                    int value = DCT[i];
                    i++;
                    int length = DCT[i];

                    for(j=0; j<length; j++)
                    {
                        pixel[k] = value;
                        k++;
                    }
                }

                else
                {
                    pixel[k] = temp;
                    k++;
                }
            }
            if (log)
            {
                System.out.print(".." + "\r");
            }

            i++;
        }

        for (int a = 0; a < 80; a++)
        {
            System.out.print(pixel[a] + " ");
        }
        System.out.println();
        for (int a = 0; a < 80; a++)
        {
            System.out.print(DCT[a] + " ");
        }

        return pixel;
    }

    /**
     * This method is preformed using the reverse of the operations preformed in
     * the DCT. This restores a N * N input block to the corresponding output
     * block with values scaled to 0 to 255 and then stored in the input block
     * of pixels.
     *
     * @param input N * N input block
     * @returns output The pixel array output
     */
    public int[][] inverseDCT(int input[][])
    {
        int output[][] = new int[N][N];
        double temp[][] = new double[N][N];
        double temp1;
        int i;
        int j;
        int k;

        for (i=0; i<N; i++)
        {
            for (j=0; j<N; j++)
            {
                temp[i][j] = 0.0;

                for (k=0; k<N; k++)
                {
                    temp[i][j] += input[i][k] * c[k][j];
                }
            }
        }

        for (i=0; i<N; i++)
        {
            for (j=0; j<N; j++)
            {
                temp1 = 0.0;

                for (k=0; k<N; k++)
                {
                    temp1 += cT[i][k] * temp[k][j];
                }

                temp1 += 128.0;

                if (temp1 < 0)
                {
                    output[i][j] = 0;
                }
                else if (temp1 > 255)
                {
                    output[i][j] = 255;
                }
                else
                {
                     output[i][j] = (int)Math.round(temp1);
                }
            }
        }

        return output;
    }

    /**
     * This method uses bit shifting to convert an array of two bytes
     * to a integer (16 bits max). Byte 0 is the most signifigant byte
     * and Byte 1 is the least signifigant byte.
     * @param bitSet Two bytes to convert
     * @returns The constructed integer
     */
    private int bytetoInt(byte bitSet[])
    {
        int returnInt = 0;

        byte MSB = bitSet[0];
        byte LSB = bitSet[1];

        returnInt = ((MSB+128) << 8) | (LSB+128);

        return returnInt;
    }

    /**
     * This method uses bit shifting to convert an integer to an array
     * of two bytes, byte 0, the most signifigant and byte 1, the least
     * signifigant.
     * @param count The integer to convert. (16 bit max)
     * @returns The array of two bytes.
     */
    private byte[] inttoByte(int count)
    {
        int LSB = 0;
        int MSB = 0;
        byte bitSet[] = new byte[2];

        if (!(count > 65535))
        {
  	        LSB = ((count & 0x000000ff));
            MSB = ((count & 0x0000ff00) >> 8);
        }
        else
        {
            System.out.println("Integer > than 16 bit. Exiting..");
            System.exit(count);
        }

        bitSet[0] = (byte)(MSB-128);
        bitSet[1] = (byte)(LSB-128);

        return bitSet;
    }
    
    public int[][] decoupage(int[][] entree ){ //CHUI RENDU A PUR LE DECOUPAGE
    	int hauteurTable = entree.length/8;
    	int largeurTable = entree[0].length/8;
    	int[][] chunkTable = new int[hauteurTable][largeurTable];
    	
    	for(int i=0; i<hauteurTable;i++){
    		for(int j=0; j<largeurTable;j++){
    			chunkTable[i][j] = 
    		}
    	}
    		
    return chunkTable;
    }
    // source: http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java, adaptee a notre code
    public double[][] appliquerDCT(double[][] entree ){
    	double[][] sortie = new double[entree.length][entree[0].length];
    	double[] c = new double[8];
    	
    	for (int i=1;i<N;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    	
    	
        for (int u=0;u<N;u++) {
            for (int v=0;v<N;v++) {
              double sum = 0.0;
              for (int i=0;i<entree.length/8;i++) {
                  for (int j=0;j<entree[0].length/8;j++) {
                  sum+=Math.cos((((2*i+1)/(2.0*N))*u*Math.PI)/16)*Math.cos((((2*j+1)/(2.0*N))*v*Math.PI)/16)*entree[i][j];
                }
              }
              sum*=((c[u]*c[v])/4.0);
              sortie[u][v]=sum;
            }
          }
    	
    	return sortie;
    }
    // source: http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java, adaptee a notre code
    public double[][] appliquerIDCT(double[][] entree) {
    	double[][] sortie = new double[entree.length][entree[0].length];
    	double[] c = new double[8];
        
        for (int i=1;i<N;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
        for (int i=0;i<entree.length/8;i++) {
          for (int j=0;j<entree[0].length/8;j++) {
            double sum = 0.0;
            for (int u=0;u<N;u++) {
              for (int v=0;v<N;v++) {
                sum+=(c[u]*c[v])/4.0*Math.cos((((2*i+1)/(2.0*N))*u*Math.PI)/16)*Math.cos((((2*j+1)/(2.0*N))*v*Math.PI)/16)*entree[u][v];
              }
            }
            sortie[i][j]=Math.round(sum);
          }
        }
        return sortie;
    }
   }
