package tbtrack.chapter2;

import tools.*;
import java.util.concurrent.ThreadLocalRandom;

public class Src_BA2G extends Src_BA2D {
	public static double[][] buildProfile_exception(String[] motifs, int except){
		double[][] profile = new double[4][motifs[0].length()];
		int n = motifs.length - 1;
		
		for(int i = 0; i < motifs[0].length(); i++) {
			int nA = 0, nC = 0, nG = 0, nT = 0;
			for(int j = 0; j <= n; j++) {
				if(j == except) continue;
				
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
		FileIO file = new FileIO(2, 'g');
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		int k = Integer.parseInt(vspl[0]), t = Integer.parseInt(vspl[1]), N = Integer.parseInt(vspl[2]);
		
		String[] dna = new String[t];
		for(int i = 0; i < t; i++) {
			dna[i] = file.read();
		}
		
		String[] bestMotifs = new String[t];
		int bestScore = 1 << 30;
		
		for(int run = 0; run < 20; run++) {
			String[] rnMotifs = new String[t];
			for(int i = 0; i < t; i++) {
				int rn = ThreadLocalRandom.current().nextInt(0, dna[i].length() - k + 1);
				rnMotifs[i] = dna[i].substring(rn, rn + k);
			}
			
			for(int i = 0; i < N; i++) {
				int except = ThreadLocalRandom.current().nextInt(0, t);
				double[][] profile = buildProfile_exception(rnMotifs, except);
				
				String[] motifs = new String[t];
				for(int j = 0; j < t; j++) {
					motifs[j] = rnMotifs[j];
				}
				
				motifs[except] = profit(dna[except], k, profile);
				
				if(score(motifs) < score(rnMotifs)) {
					rnMotifs[except] = motifs[except];
				}
			}
			
			if(score(rnMotifs) < bestScore) {
				bestScore = score(rnMotifs);
				
				for(int j = 0; j < t; j++) {
					bestMotifs[j] = rnMotifs[j];
				}
			}
		}
		
		for(String motif : bestMotifs) {
			file.write(motif);
		}
		
		file.close();
	}
}
