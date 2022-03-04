package tbtrack.chapter10;

import tools.*;
import java.util.*;

public class Src_BA10C {
	public static String viterbi(HiddenMarkov hmm, String opath) {
		double[][] viterbiMatrix	= new double[hmm.stateCount()][opath.length()];
		int[][] stateTracker		= new int   [hmm.stateCount()][opath.length()];
		
		// initial state 
		for(int i = 0; i < hmm.stateCount(); i++) {
			viterbiMatrix[i][0] = (1.0 / hmm.stateCount()) * hmm.getEmission(hmm.getState(i), opath.charAt(0));
			stateTracker [i][0] = -1;
		}
		
		// matrix buildup with tracking
		double maxValue;
		int maxEntry;
		
		for(int i = 1; i < opath.length(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				maxValue = .0;
				maxEntry =  0;
				
				for(int k = 0; k < hmm.stateCount(); k++) {
					double val = viterbiMatrix[k][i - 1] * hmm.getTransition(hmm.getState(k), hmm.getState(j))
							* hmm.getEmission(hmm.getState(j), opath.charAt(i));
					if(val > maxValue) {
						maxValue = val;
						maxEntry = k;
					}
				}
				
				viterbiMatrix[j][i] = maxValue;
				stateTracker [j][i] = maxEntry;
			}
		}
		
		// terminal state
		maxValue = .0;
		maxEntry =  0;
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			if(viterbiMatrix[i][opath.length() - 1] < maxValue) {
				maxValue = viterbiMatrix[i][opath.length() - 1];
				maxEntry = i;
			}
		}
		
		String optPath = "" + hmm.getState(maxEntry);
		
		// backtrack
		for(int i = opath.length() - 1; i > 0; i--) {
			maxEntry = stateTracker[maxEntry][i];
			optPath = hmm.getState(maxEntry) + optPath;
		}
		
		return optPath;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'c');
		String opath = file.read();
		
		file.read();
		String sgstr = file.read();
		
		file.read();
		String ststr = file.read();
		
		HiddenMarkov hmm = new HiddenMarkov(sgstr, ststr);	
		List<String> mstrList;
		
		// parse transition matrix
		file.read();
		mstrList = new ArrayList<String>();
		for(int i = 0; i <= hmm.stateCount(); i++) mstrList.add(file.read());
		hmm.setTransition(mstrList);
		
		// parse emission matrix
		file.read();
		mstrList = new ArrayList<String>();
		for(int i = 0; i <= hmm.stateCount(); i++) mstrList.add(file.read());
		hmm.setEmission(mstrList);
		
		file.write(viterbi(hmm, opath));
		file.close();
	}
}
