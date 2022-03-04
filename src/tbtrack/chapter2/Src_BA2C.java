package tbtrack.chapter2;

import tools.*;

public class Src_BA2C {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(2, 'c');
		
		String text = file.read();
		int k = Integer.parseInt(file.read());
		
		double[][] profile = new double[4][k];
		for(int i = 0; i < 4; i++) {
			String vstr = file.read();
			String[] vspl = vstr.split(" ");
			
			for(int j = 0; j < k; j++) {
				profile[i][j] = Double.parseDouble(vspl[j]);
			}
		}
		
		double maxFreq = .0;
		String maxSeq = "";
		for(int i = 0; i <= text.length() - k; i++) {
			String kmer = text.substring(i, i + k);
			double freq = 1.0;
			
			for(int j = 0; j < k; j++) {
				switch(kmer.charAt(j)) {
				case 'A':
					freq *= profile[0][j];
					break;
				case 'C':
					freq *= profile[1][j];
					break;
				case 'G':
					freq *= profile[2][j];
					break;
				case 'T':
					freq *= profile[3][j];
					break;
				}
			}
			
			if(maxFreq < freq) {
				maxFreq = freq;
				maxSeq = kmer;
			}
		}
		
		file.write(maxSeq);
		file.close();
	}
}
