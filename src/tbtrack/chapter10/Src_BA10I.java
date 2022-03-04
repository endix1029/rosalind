package tbtrack.chapter10;

import tools.*;
import java.util.*;

public class Src_BA10I {
	private static int ITER;
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'i');
		ITER = Integer.parseInt(file.read());
		
		file.read();
		String epath = file.read();

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
		
		for(int i = 0; i < ITER; i++) {
			// run viterbi algorithm
			String hpath = Src_BA10C.viterbi(hmm, epath);
			// adjust parameters
			Src_BA10H.estimateParameter(hmm, epath, hpath);
		}
		
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
