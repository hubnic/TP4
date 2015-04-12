package gti310.tp4;

public class Quantification {
	
	double qualite = 0;
	double facteurQualite = 0;
	
	private double[][] tableY = new double[][] {
			
			{16,40,40,40,40,40,51,61},
			{40,40,40,40,40,58,60,55},
			{40,40,40,40,40,57,69,56},
			{40,40,40,40,51,87,80,62},
			{40,40,40,56,68,109,103,77},
			{40,40,55,64,81,104,113,92},
			{49,64,78,87,103,121,120,101},
			{72,92,95,98,112,100,103,95}
	};
		
	private double[][] tableCbCr = new double[][] {
				
				{17,40,40,95,95,95,95,95},
				{40,40,40,95,95,95,95,95},
				{40,40,40,95,95,95,95,95},
				{40,40,95,95,95,95,95,95},
				{95,95,95,95,95,95,95,95},
				{95,95,95,95,95,95,95,95},
				{95,95,95,95,95,95,95,95},
				{95,95,95,95,95,95,95,95}
		};
	
	public double [][] appliquerQuantificationY(double[][] entree, int qualite){
		double[][] sortie = new double[entree.length][entree[0].length];
		facteurDeQualite(qualite);
		if(facteurQualite == 1){
	          for (int i=0;i<entree.length;i++) {
	            for (int j=0;j<entree[0].length;j++) {
	              sortie[i][j] = entree[i][j];
	            }
	          }
	    	return sortie;
		}
		else{
			for (int i=0;i<entree.length;i++) {
	            for (int j=0;j<entree[0].length;j++) {
	              sortie[i][j] = (entree[i][j])/(facteurQualite*tableY[i][j]);
	            }
	          }
		}
		return sortie;
	}
	
	public double [][] appliquerQuantificationCbCr(double[][] entree, int qualite){
		double[][] sortie = new double[entree.length][entree[0].length];
		facteurDeQualite(qualite);
		
		if(facteurQualite == 1){
	          for (int i=0;i<entree.length;i++) {
	            for (int j=0;j<entree[0].length;j++) {
	              sortie[i][j] = entree[i][j];
	            }
	          }
	    	return sortie;
		}
		else{
			for (int i=0;i<entree.length;i++) {
	            for (int j=0;j<entree[0].length;j++) {
	              sortie[i][j] = (entree[i][j])/(facteurQualite*tableCbCr[i][j]);
	            }
	          }
		}
		return sortie;
	}
	
	private void facteurDeQualite(double qualite){
		if(qualite == 100){
			facteurQualite = 1;
		}
		else if(qualite >= 1 && qualite <= 50){
			facteurQualite = 50/qualite;
		}
		else if(qualite >= 51 && qualite <= 99){
			facteurQualite = (200-2*qualite)/100;
		}
	}




}
