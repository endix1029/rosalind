package tbtrack.chapter2;

import tools.*;
import java.util.concurrent.ThreadLocalRandom;

public class Src_BA2F extends Src_BA2D {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'f');
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		int k = Integer.parseInt(vspl[0]), t = Integer.parseInt(vspl[1]);
		
		String[] dna = new String[t];
		for(int i = 0; i < t; i++) {
			dna[i] = file.read();
		}
		
		String[] bestMotifs = new String[t];
		int bestScore = 1 << 30;
		
		for(int i = 0; i < 1000; i++) {
			String[] rnMotifs = new String[t];
			for(int j = 0; j < t; j++) {
				int rn = ThreadLocalRandom.current().nextInt(0, dna[j].length() - k + 1);
				rnMotifs[j] = dna[j].substring(rn, rn + k);
			}
			
			while(true) {
				double[][] profile = Src_BA2E.buildProfile_laplace(rnMotifs, t);
				String[] motifs = new String[t];
				
				for(int j = 0; j < t; j++) {
					motifs[j] = profit(dna[j], k, profile);
				}
				
				if(score(motifs) < score(rnMotifs)) {
					for(int j = 0; j < t; j++) {
						rnMotifs[j] = motifs[j];
					}
				}
				else break;
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
