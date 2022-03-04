package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1G {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'g');
		
		String ref = file.read(), qry = file.read();
		if(ref.length() != qry.length()) return;
		
		int ham = 0;
		for(int i = 0; i < ref.length(); i++) {
			if(ref.charAt(i) != qry.charAt(i)) ham++;
		}
		
		file.write(String.valueOf(ham));
		file.close();
	}
}
