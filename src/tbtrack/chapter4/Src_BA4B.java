package tbtrack.chapter4;

import tools.*;

public class Src_BA4B {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'b');
		String seq = file.read();
		String prtn = file.read();
		
		int wsize = prtn.length() * 3;
		for(int i = wsize; i <= seq.length(); i++) {
			String window = seq.substring(i - wsize, i);
			String p5 = Functions.tsln_seq(window, 5), p3 = Functions.tsln_seq(window, 3);
			if(p5.equals(prtn) || p3.equals(prtn)) {
				file.write(window);
			}
		}

		file.close();
	}
}
