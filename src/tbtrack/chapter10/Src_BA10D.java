package tbtrack.chapter10;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.HiddenMarkov;

public class Src_BA10D {
	public static double outcomeLikelihood(HiddenMarkov hmm, String opath) {
		double[][] outcomeMatrix = new double[hmm.stateCount()][opath.length()];
		for(int i = 0; i < hmm.stateCount(); i++) {
			outcomeMatrix[i][0] = (1.0 / hmm.stateCount()) * hmm.getEmission(hmm.getState(i), opath.charAt(0));
		}
		
		for(int i = 1; i < opath.length(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				for(int k = 0; k < hmm.stateCount(); k++) {
					outcomeMatrix[j][i] += outcomeMatrix[k][i - 1] * hmm.getTransition(hmm.getState(k), hmm.getState(j))
							* hmm.getEmission(hmm.getState(j), opath.charAt(i));
				}
			}
		}
		
		double outcome = .0;
		for(int i = 0; i < hmm.stateCount(); i++) outcome += outcomeMatrix[i][opath.length() - 1];
		return outcome;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'd');
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
		
		file.writeObj(outcomeLikelihood(hmm, opath));
		file.close();
	}
}
