package tbtrack.chapter2;

import tools.*;
import java.util.ArrayList;
import java.util.Collections;

public class Src_BA2A {
	// matches given k-mer to all patterns in DNA, returns its appearance in all patterns
	private static boolean match(String kmer, int d, ArrayList<String> dna) {
		int k = kmer.length();
		
		for(String pattern : dna) {
			boolean flag = false;
			for(int i = 0; i <= pattern.length() - k; i++) {
				if(Functions.hamming(kmer, pattern.substring(i, i + k)) <= d) {
					flag = true;
					break;
				}
			}
			
			if(flag) continue;
			return false;
		}
		
		return true;
	}
	
	private static ArrayList<String> motif_enum(int k, int d, ArrayList<String> dna){
		ArrayList<String> motifs = new ArrayList<String>();
		ArrayList<String> neighs;
		
		String pattern = dna.get(0);
		for(int i = 0; i <= pattern.length() - k; i++) {
			neighs = Functions.neighbor(pattern.substring(i, i + k), d);
			for(String neigh : neighs) {
				if(motifs.contains(neigh)) continue;
				if(match(neigh, d, dna)) motifs.add(neigh);
			}
		}
		
		return motifs;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'a');
		
		String vstr = file.read();
		String[] vsplit = vstr.split(" ");
		int k = Integer.parseInt(vsplit[0]), d = Integer.parseInt(vsplit[1]);
		
		ArrayList<String> dna = new ArrayList<String>();
		String line;
		while((line = file.read()) != null) {
			dna.add(line);
		}
		
		ArrayList<String> motifs = motif_enum(k, d, dna);
		Collections.sort(motifs);
		for(String motif : motifs) {
			file.type(motif + " ");
		}
		
		file.close();
	}
}
