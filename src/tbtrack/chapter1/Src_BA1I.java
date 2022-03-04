package tbtrack.chapter1;

import tools.FileIO;
import tools.Functions;
import java.util.ArrayList;

public class Src_BA1I {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'i');
		
		String text = file.read(), vset = file.read();
		String[] vsplit = vset.split(" ");
		
		int k = Integer.parseInt(vsplit[0]), d = Integer.parseInt(vsplit[1]);
		int maxfreq = 0;
		
		int[] freqs = new int[1 << (k * 2)];
		for(int i = 0; i <= text.length() - k; i++) {
			String kmer = text.substring(i, i + k);
			ArrayList<String> neighs = Functions.neighbor(kmer, d);
		//	System.out.print("kmer : " + kmer + "\tNeighbors : ");
			for(String neighbor : neighs) {
		//		System.out.print(neighbor + " ");
				int hval = Functions.seq_hash(neighbor);
				freqs[hval]++;
				
				if(freqs[hval] > maxfreq) maxfreq = freqs[hval];
			}
		//	System.out.println("");
		}
		
		for(int i = 0; i < freqs.length; i++) {
			if(freqs[i] == maxfreq) {
				file.type(Functions.seq_revhash(i, k) + " ");
			}
		}
		
		file.write("");
		file.close();
	}
}
