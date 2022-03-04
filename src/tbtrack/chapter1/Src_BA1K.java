package tbtrack.chapter1;

import tools.FileIO;
import tools.Functions;

public class Src_BA1K {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'k');
		
		String seq = file.read();
		int k = Integer.parseInt(file.read());
		int[] freqs = new int[1 << (k * 2)];
		
		for(int i = 0; i <= seq.length() - k; i++) {
			freqs[Functions.seq_hash(seq.substring(i, i + k))]++;
		}
		
		for(int freq : freqs) {
			file.type(freq + " ");
		}
		
		file.write("");
		file.close();
	}
}
