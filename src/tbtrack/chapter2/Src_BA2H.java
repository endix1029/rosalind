package tbtrack.chapter2;

import tools.*;
import java.util.ArrayList;

public class Src_BA2H {
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
		FileIO file = new FileIO(2, 'h');
		
		String kmer = file.read();
		ArrayList<String> dna = new ArrayList<String>();
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		for(String str : vspl) {
			dna.add(str);
		}
		
		file.writeObj(overallDist(kmer, dna));
		file.close();
	}
}
