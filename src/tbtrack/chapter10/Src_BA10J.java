package tbtrack.chapter10;

import java.util.ArrayList;
import java.util.List;

import tools.*;

public class Src_BA10J {
	public static double SINK;
	
	public static double[][] forwardViterbi(HiddenMarkov hmm, String opath) {
		double[][] probMatrix = new double[hmm.stateCount()][opath.length()];
		
		// initial state 
		for(int i = 0; i < hmm.stateCount(); i++) {
			probMatrix[i][0] = (1.0 / hmm.stateCount()) * hmm.getEmission(hmm.getState(i), opath.charAt(0));
		}
		
		// matrix buildup
		for(int i = 1; i < opath.length(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				double val = .0;
				for(int k = 0; k < hmm.stateCount(); k++) {
					val += probMatrix[k][i - 1] * hmm.getTransition(hmm.getState(k), hmm.getState(j))
							* hmm.getEmission(hmm.getState(j), opath.charAt(i));
				}
				probMatrix[j][i] = val;
			}
		}
		
		SINK = .0;
		for(int i = 0; i < hmm.stateCount(); i++) {
			SINK += probMatrix[i][opath.length() - 1] / hmm.stateCount();
		}
		
		return probMatrix;
	}
	
	public static double[][] backwardViterbi(HiddenMarkov hmm, String opath) {
		double[][] probMatrix = new double[hmm.stateCount()][opath.length()];
		
		// initialize
		for(int i = 0; i < hmm.stateCount(); i++) {
			probMatrix[i][opath.length() - 1] = 1.0 / hmm.stateCount();
		}
		
		// matrix buildup
		for(int i = opath.length() - 2; i >= 0; i--) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				double val = .0;
				for(int k = 0; k < hmm.stateCount(); k++) {
					val += probMatrix[k][i + 1] * hmm.getTransition(hmm.getState(j), hmm.getState(k))
							* hmm.getEmission(hmm.getState(k), opath.charAt(i + 1));
				}
				probMatrix[j][i] = val;
			}
		}
		
		return probMatrix;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'j');
		
		String opath = file.read();
		
		file.read();
		String sgstr = file.read();
		file.read();
		String ststr = file.read();
		
		HiddenMarkov hmm = new HiddenMarkov(sgstr, ststr);
		
		// Initial transition matrix
		file.read();
		List<String> mstrList = new ArrayList<String>();
		for(int i = 0; i <= hmm.stateCount(); i++) {
			mstrList.add(file.read());
		}
		hmm.setTransition(mstrList);
		
		// Initial emission matrix
		file.read();
		mstrList.clear();
		for(int i = 0; i <= hmm.stateCount(); i++) {
			mstrList.add(file.read());
		}
		hmm.setEmission(mstrList);
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			file.type(hmm.getState(i) + "\t");
		}
		file.write("");
		
		double[][] forward  = forwardViterbi(hmm, opath);
		double[][] backward = backwardViterbi(hmm, opath);
		
		for(int i = 0; i < opath.length(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				file.type(String.format("%.4f\t", forward[j][i] * backward[j][i] / SINK));
			}
			file.write("");
		}
		
		file.close();
	}
}
