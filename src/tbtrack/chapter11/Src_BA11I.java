package tbtrack.chapter11;

import tools.FileIO;
import tools.Peptide;

public class Src_BA11I {
	private static Peptide PEP = new Peptide();
	
	public static double dictionaryProb(int[] spec, int threshold, int max_score) {
		double[][] size = new double[spec.length][max_score + 1];
		
		size[0][0] = 1.0;
		for(int i = 1; i < spec.length; i++) {
			for(int t = 0; t <= max_score; t++) {
				if(t - spec[i] < 0) continue;
				if(t - spec[i] > max_score) continue;
				
				double sum = .0;
				for(char aa : Peptide.AA) {
					if(i - PEP.getMass(aa) < 0) continue;
					sum += 0.05 * size[i - PEP.getMass(aa)][t - spec[i]];
				}
				
				size[i][t] = sum;
			}
		}
		
		double sum = .0;
		for(int i = threshold; i <= max_score; i++) {
			sum += size[spec.length - 1][i];
		}
		return sum;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'i');
		
		String[] mspl = file.read().split(" ");	
		int[] spec = new int[mspl.length + 1];
		spec[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			spec[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		int threshold = Integer.parseInt(file.read());
		int max_score = Integer.parseInt(file.read());
		
		file.writeObj(dictionaryProb(spec, threshold, max_score));
		file.close();
	}
}
