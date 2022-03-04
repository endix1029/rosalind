package tbtrack.chapter4;

import tools.*;
import java.util.*;

public class Src_BA4F {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'f');
		String prtn = file.read();
		List<Integer> spectrum = Functions.linearSpectrum(prtn);
		
		List<Integer> querySpectrum = new ArrayList<Integer>();
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		for(String v : vspl) {
			querySpectrum.add(Integer.parseInt(v));
		}
		
		int i = 0, j = 0, score = 0;
		while(i < spectrum.size() && j < querySpectrum.size()) {
			int ref = spectrum.get(i), qry = querySpectrum.get(j);
			if(ref == qry) {
				score++;
				i++;
				j++;
			}
			else if(ref > qry) j++;
			else i++;
		}
		
		file.writeObj(score);
		file.close();
	}
}
