package gti310.tp4;

public class newDCT {
	
	
	final int N = 8;
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
