package tbtrack.chapter4;

import tools.*;
import java.util.*;

public class Src_BA4L {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'l');
		String[] peptides = file.read().split(" ");
		
		List<Integer> query = new ArrayList<Integer>();
		for(String val : file.read().split(" ")) query.add(Integer.parseInt(val));
		
		int N = Integer.parseInt(file.read());
		
		List<Integer> scores = new ArrayList<Integer>();
		for(String peptide : peptides) scores.add(Functions.linearScore(peptide, query));
		Collections.sort(scores);
		
		int cutoff = scores.get(scores.size() - N);
		List<String> trim = new ArrayList<String>();
		for(String peptide : peptides) {
			if(Functions.linearScore(peptide, query) >= cutoff) trim.add(peptide);
		}
		
		for(String peptide : trim) file.type(peptide + " ");
		file.write("");
		file.close();
	}
}
