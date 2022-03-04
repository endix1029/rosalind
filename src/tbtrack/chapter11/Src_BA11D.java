package tbtrack.chapter11;

import tools.*;
import java.util.*;

public class Src_BA11D {
	public static String vectorPeptide(boolean[] pvec) {
		Peptide pep = new Peptide();
		List<Integer> locs = new ArrayList<Integer>();
		
		for(int i = 1; i <= pvec.length; i++) {
			if(pvec[i - 1]) locs.add(i);
		}
		
		String peptide = "";
		int prev = 0;
		for(int loc : locs) {
			peptide += pep.getAcid(loc - prev);
			prev = loc;
		}
		
		return peptide;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'd');
		String[] vspl = file.read().split(" ");
		
		boolean[] pvec = new boolean[vspl.length];
		for(int i = 0; i < vspl.length; i++) {
			pvec[i] = vspl[i].equals("1");
		}
		
		file.write(vectorPeptide(pvec));
		file.close();
	}
}
