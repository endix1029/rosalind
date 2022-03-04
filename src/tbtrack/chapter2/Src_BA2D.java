package tbtrack.chapter2;

import tools.*;

public class Src_BA2D {
	protected static String consensus(String[] motifs) {
		String css = "";
		
		for(int i = 0; i < motifs[0].length(); i++) {
			int[] bc = new int[4];
			for(int j = 0; j < motifs.length; j++) {
				switch(motifs[j].charAt(i)) {
				case 'A':
					bc[0]++;
					break;
				case 'C':
					bc[1]++;
					break;
				case 'G':
					bc[2]++;
					break;
				case 'T':
					bc[3]++;
					break;
				}
			}
				
			int maxC = bc[0];
			char maxB = 'A';
			if(bc[1] > maxC) {
				maxC = bc[1];
				maxB = 'C';
			}
			if(bc[2] > maxC) {
				maxC = bc[2];
				maxB = 'G';
			}
			if(bc[3] > maxC) {
				maxC = bc[3];
				maxB = 'T';
			}
				
			css += maxB;
		}
		
		return css;
	}
	
	protected static int score(String[] motifs) {
		String css = consensus(motifs);
		int score = 0;
		
		for(int i = 0; i < motifs[0].length(); i++) {
			for(int j = 0; j < motifs.length; j++) {
				if(motifs[j].charAt(i) != css.charAt(i)) score++;
			}
		}
		
		return score;
	}
	
	protected static double[][] buildProfile(String[] motifs, int n){
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
			
			profile[0][i] = (double) nA / n;
			profile[1][i] = (double) nC / n;
			profile[2][i] = (double) nG / n;
			profile[3][i] = (double) nT / n;
		}
		
		return profile;
	}
	
	protected static String profit(String seq, int k, double[][] profile) {
		double maxFreq = .0;
		String maxSeq = seq.substring(0, k);
		
		for(int i = 0; i <= seq.length() - k; i++) {
			String kmer = seq.substring(i, i + k);
			double freq = 1.0;
			
			for(int j = 0; j < k; j++) {
				switch(kmer.charAt(j)) {
				case 'A':
					freq *= profile[0][j]; break;
				case 'C':
					freq *= profile[1][j]; break;
				case 'G':
					freq *= profile[2][j]; break;
				case 'T':
					freq *= profile[3][j]; break;
				}
			}
			
			if(freq > maxFreq) {
				maxFreq = freq;
				maxSeq = kmer;
			}
		}
		
		return maxSeq;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'd');
		
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
				double profile[][] = buildProfile(motifs, j);
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
