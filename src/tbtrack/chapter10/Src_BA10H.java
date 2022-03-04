package tbtrack.chapter10;

import tools.*;

public class Src_BA10H {
	public static void estimateParameter(HiddenMarkov hmm, String epath, String hpath) {
		int[][] stateTransfer = new int[hmm.stateCount()][hmm.stateCount()];
		int[][] stateEmission = new int[hmm.stateCount()][hmm.sigmaCount()];
		
		// analyze hidden path
		for(int i = 0; i < hpath.length() - 1; i++) {
			stateTransfer[hmm.getStateEntry(hpath.charAt(i))][hmm.getStateEntry(hpath.charAt(i + 1))]++;
		}
		
		// analyze emitted path
		for(int i = 0; i < hpath.length(); i++) {
			stateEmission[hmm.getStateEntry(hpath.charAt(i))][hmm.getAlphaEntry(epath.charAt(i))]++;
		}
		
		// setup transition matrix
		for(int i = 0; i < hmm.stateCount(); i++) {
			int rsum = 0;
			for(int j = 0; j < hmm.stateCount(); j++) rsum += stateTransfer[i][j];
			
			for(int j = 0; j < hmm.stateCount(); j++) {
				double val;
				if(rsum == 0) val = (double) 1 / hmm.stateCount();
				else val = (double) stateTransfer[i][j] / rsum;
				hmm.setTransition(hmm.getState(i), hmm.getState(j), val);
			}
		}
		
		// setup emission matrix
		for(int i = 0; i < hmm.stateCount(); i++) {
			int rsum = 0;
			for(int j = 0; j < hmm.sigmaCount(); j++) rsum += stateEmission[i][j];
			
			for(int j = 0; j < hmm.sigmaCount(); j++) {
				double val;
				if(rsum == 0) val = (double) 1 / hmm.sigmaCount();
				else val = (double) stateEmission[i][j] / rsum;
				hmm.setEmission(hmm.getState(i), hmm.getAlpha(j), val);
			}
		}
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'h');
		String epath = file.read();
		
		file.read();
		String sgstr = file.read().replace(' ', '\t');
		
		file.read();
		String hpath = file.read();
		
		file.read();
		String ststr = file.read().replace(' ',  '\t');
		
		HiddenMarkov hmm = new HiddenMarkov(sgstr, ststr);
		estimateParameter(hmm, epath, hpath);
		
		// Transition header
		for(int i = 0; i < hmm.stateCount(); i++) {
			file.type("\t" + hmm.getState(i));
		}
		file.write("");
		for(int i = 0; i < hmm.stateCount(); i++) {
			file.typeObj(hmm.getState(i));
			for(int j = 0; j < hmm.stateCount(); j++) {
				file.type(String.format("\t%.3f", hmm.getTransition(hmm.getState(i), hmm.getState(j))));
			}
			file.write("");
		}
		
		file.write("--------");
		
		// Emission header
		for(int i = 0; i < hmm.sigmaCount(); i++) {
			file.type("\t" + hmm.getAlpha(i));
		}
		file.write("");
		for(int i = 0; i < hmm.stateCount(); i++) {
			file.typeObj(hmm.getState(i));
			for(int j = 0; j < hmm.sigmaCount(); j++) {
				file.type(String.format("\t%.3f", hmm.getEmission(hmm.getState(i), hmm.getAlpha(j))));
			}
			file.write("");
		}
		
		file.close();
	}
}
