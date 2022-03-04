package tbtrack.chapter10;

import tools.*;
import java.util.*;

public class Src_BA10B {
	public static double outputPathProb(HiddenMarkov hmm, String opath, String hpath) {
		double prob = 1.0;
		for(int i = 0; i < opath.length(); i++) {
			prob *= hmm.getEmission(hpath.charAt(i), opath.charAt(i));
		}
		return prob;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'b');
		String opath = file.read();
		
		file.read();
		String sgstr = file.read();
		
		file.read();
		String hpath = file.read();
		
		file.read();
		String ststr = file.read();
		
		HiddenMarkov hmm = new HiddenMarkov(sgstr, ststr);
		
		file.read();
		List<String> mstrList = new ArrayList<String>();
		for(int i = 0; i <= hmm.stateCount(); i++) mstrList.add(file.read());
		
		hmm.setEmission(mstrList);
		
		file.writeObj(outputPathProb(hmm, opath, hpath));
		file.close();
	}	
}
