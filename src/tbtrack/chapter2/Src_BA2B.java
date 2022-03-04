package tbtrack.chapter2;

import tools.*;
import java.util.ArrayList;

public class Src_BA2B {
	private static int overallDist(String kmer, ArrayList<String> dna) {
		int dist = 0, k = kmer.length();
		
		for(String pattern : dna) {
			int pmin = 1 << 30;
			
			for(int i = 0; i <= pattern.length() - k; i++) {
				int pdist = Functions.hamming(pattern.substring(i, i + k), kmer);
				if(pdist < pmin) {
					pmin = pdist;
				}
			}
			
			dist += pmin;
		}
		
		return dist;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'b');
		
		int k = Integer.parseInt(file.read());
		ArrayList<String> dna = new ArrayList<String>();
		
		String line;
		while((line = file.read()) != null) {
			dna.add(line);
		}
		
		int minDist = 1 << 30;
		String median = "";
		
		for(int i = 0; i < (1 << (k * 2)); i++) {
			String kmer = Functions.seq_revhash(i, k);
			
			int dist = overallDist(kmer, dna);
			if(dist < minDist) {
				minDist = dist;
				median = kmer;
			}
		}
		
		file.write(median);
		file.close();
	}
}
