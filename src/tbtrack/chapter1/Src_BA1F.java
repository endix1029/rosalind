package tbtrack.chapter1;

import tools.FileIO;
import java.util.ArrayList;

public class Src_BA1F {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'f');
		
		String genome = file.read();
		int skew = 0, minskew = 0;
		ArrayList<Integer> indices = new ArrayList<Integer>();
		indices.add(0);
		
		for(int i = 0; i < genome.length(); i++) {
			switch(genome.charAt(i)) {
			case 'A':
			case 'T':
				break;
			case 'G':
				skew++;
				break;
			case 'C':
				skew--;
				break;
			}
			
			if(minskew > skew) {
				minskew = skew;
				indices = new ArrayList<Integer>();
				indices.add(i + 1);
			}
			else if(minskew == skew) {
				indices.add(i + 1);
			}
		}
		
		for(int index : indices) {
			file.type(index + " ");
		}
		
		file.write("");
		file.close();
	}
}
