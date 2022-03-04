package tbtrack.chapter2;

import tools.*;

public class Src_BA2E extends Src_BA2D {
	public static double[][] buildProfile_laplace(String[] motifs, int n){
		double[][] profile = new double[4][motifs[0].length()];
		for(int i = 0; i < motifs[0].length(); i++) {
			int nA = 0, nC = 0, nG = 0, nT = 0;
			for(int j = 0; j < n; j++) {
				switch(motifs[j].charAt(i)) {
				case 'A':
					nA++; break;
				case 'C':
					nC++; break;
				case 'G':
					nG++; break;
				case 'T':
					nT++; break;
				}
			}
			
			profile[0][i] = (double) (1 + nA) / (4 + n);
			profile[1][i] = (double) (1 + nC) / (4 + n);
			profile[2][i] = (double) (1 + nG) / (4 + n);
			profile[3][i] = (double) (1 + nT) / (4 + n);
		}
		
		return profile;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'e');
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		int k = Integer.parseInt(vspl[0]), t = Integer.parseInt(vspl[1]);
		
		String[] dna = new String[t];
		for(int i = 0; i < t; i++) {
			dna[i] = file.read();
		}
		
		String[] bestMotifs = new String[t];
		for(int i = 0; i < t; i++) {
			bestMotifs[i] = dna[i].substring(0, k);
		}
		int bestScore = score(bestMotifs);
		
		for(int i = 0; i <= dna[0].length() - k; i++) {
			String[] motifs = new String[t];
			motifs[0] = dna[0].substring(i, i + k);
			
			for(int j = 1; j < t; j++) {
				double profile[][] = buildProfile_laplace(motifs, j);
				String newMotif = profit(dna[j], k, profile);
				motifs[j] = newMotif;
			}
			
			int newScore = score(motifs);
			if(newScore < bestScore) {
				bestScore = newScore;
				for(int j = 0; j < t; j++) {
					bestMotifs[j] = motifs[j];
				}
			}
		}
		
		for(String motif : bestMotifs) {
			file.write(motif);
		}
		
		file.close();
	}		
}
