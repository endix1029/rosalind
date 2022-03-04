package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1D {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'd');
		
		String pattern = file.read(), genome = file.read();
		
		for(int i = 0; i < genome.length() - pattern.length(); i++) {
			if(genome.substring(i, i + pattern.length()).equals(pattern)) {
				file.type(i + " ");
			}
		}
		
		file.write("");
		file.close();
	}
}
