package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1C {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'c');
		
		String pattern = file.read();
		
		for(int i = pattern.length(); i > 0; i--) {
			switch(pattern.charAt(i - 1)) {
			case 'A':
				file.type("T");
				break;
			case 'T':
				file.type("A");
				break;
			case 'G':
				file.type("C");
				break;
			case 'C':
				file.type("G");
				break;
			default:
				break;
			}
		}
		
		file.write("");
		file.close();
	}
}
