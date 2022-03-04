package tbtrack.chapter10;

import tools.*;
import java.util.*;

public class Src_BA10A {
	public static double hiddenPathProb(HiddenMarkov hmm, String hpath) {
		double prob = 1.0 / hmm.stateCount();
		for(int i = 1; i < hpath.length(); i++) {
			prob *= hmm.getTransition(hpath.charAt(i - 1),hpath.charAt(i));
		}
		return prob;		
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'a');
		String hpath = file.read();
		
		file.read();
		HiddenMarkov hmm = new HiddenMarkov(file.read());
		
		file.read();
		List<String> mstrList = new ArrayList<String>();
		for(int i = 0; i <= hmm.stateCount(); i++) mstrList.add(file.read());
		
		hmm.setTransition(mstrList);
		
		file.writeObj(hiddenPathProb(hmm, hpath));
		file.close();
	}
}
