package tbtrack.chapter4;

import tools.*;

public class Src_BA4A {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'a');
		
		String line = file.read();
		String prtn = "";
		
		for(int i = 0; i < line.length(); i+=3) {
			char aa = Functions.tsln_codon(line.substring(i, i + 3));
			
			if(aa == 'X') break;
			prtn += aa;
		}
		
		file.write(prtn);
		file.close();
	}
}
