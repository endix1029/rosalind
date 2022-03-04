package tbtrack.chapter10;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.HiddenMarkov;

public class Src_BA10K {
	public static void baumWelch(HiddenMarkov hmm, String opath) {		
		double[][] forward  = Src_BA10J.forwardViterbi(hmm, opath);  // alpha[i][t]
		double[][] backward = Src_BA10J.backwardViterbi(hmm, opath); // beta[i][t]
		
		double[] ssum = new double[opath.length()]; // sum of alpha[j][t] * beta[j][t]
		for(int t = 0; t < opath.length(); t++) {
			double sum = .0;
			for(int j = 0; j < hmm.stateCount(); j++) {
				sum += forward[j][t] * backward[j][t];
			}
			ssum[t] = sum;
		}
		
		double[] dsum = new double[opath.length() - 1]; // sum of alpha[i][t] * a[i][j] * beta[j][t+1] * b[j][t+1]
		for(int t = 0; t < opath.length() - 1; t++) {
			double sum = .0;
			for(int i = 0; i < hmm.stateCount(); i++) {
				for(int j = 0; j < hmm.stateCount(); j++) {
					sum += 	forward[i][t] * hmm.getTransition(hmm.getState(i), hmm.getState(j)) *
							backward[j][t + 1] * hmm.getEmission(hmm.getState(j), opath.charAt(t + 1));
				}
			}
			dsum[t] = sum;
		}
		
		double[][]   respSingle = new double[hmm.stateCount()][opath.length()];
		double[][][] respDouble = new double[hmm.stateCount()][hmm.stateCount()][opath.length() - 1];
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			for(int t = 0; t < opath.length(); t++) {
				respSingle[i][t] = forward[i][t] * backward[i][t] / ssum[t];
			}
		}
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				for(int t = 0; t < opath.length() - 1; t++) {
					respDouble[i][j][t] = 	forward[i][t] * hmm.getTransition(hmm.getState(i), hmm.getState(j)) *
											backward[j][t + 1] * hmm.getEmission(hmm.getState(j), opath.charAt(t + 1)) /
											dsum[t];
				}
			}
		}
		
		double[][] estTransition = new double[hmm.stateCount()][hmm.stateCount()];
		double[][] estEmission   = new double[hmm.stateCount()][hmm.sigmaCount()];
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			for(int j = 0; j < hmm.stateCount(); j++) {
				double sum = .0;
				for(int k = 0; k < opath.length() - 1; k++) {
					sum += respDouble[i][j][k];
				}
				estTransition[i][j] = sum;
			}
		}
		
		for(int i = 0; i < hmm.stateCount(); i++) {
			for(int j = 0; j < hmm.sigmaCount(); j++) {
				double sum = .0;
				for(int k = 0; k < opath.length(); k++) {
					if(opath.charAt(k) == hmm.getAlpha(j)) {
						sum += respSingle[i][k];
					}
				}
				estEmission[i][j] = sum;
			}
		}
		
		// setup transition matrix
		for(int i = 0; i < hmm.stateCount(); i++) {
			double rsum = .0;
			for(int j = 0; j < hmm.stateCount(); j++) rsum += estTransition[i][j];
			
			for(int j = 0; j < hmm.stateCount(); j++) {
				double val;
				if(rsum == 0) val = (double) 1 / hmm.stateCount();
				else val = (double) estTransition[i][j] / rsum;
				hmm.setTransition(hmm.getState(i), hmm.getState(j), val);
			}
		}
		
		// setup emission matrix
		for(int i = 0; i < hmm.stateCount(); i++) {
			double rsum = .0;
			for(int j = 0; j < hmm.sigmaCount(); j++) rsum += estEmission[i][j];
			
			for(int j = 0; j < hmm.sigmaCount(); j++) {
				double val;
				if(rsum == 0) val = (double) 1 / hmm.sigmaCount();
				else val = (double) estEmission[i][j] / rsum;
				hmm.setEmission(hmm.getState(i), hmm.getAlpha(j), val);
			}
		}
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'k');
		int iter = Integer.parseInt(file.read());
		
		file.read();
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
		
		for(int i = 0; i < iter; i++) {
			baumWelch(hmm, opath);
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
