package tbtrack.chapter3;

import tools.FileIO;

public class Src_BA3B {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'b');
		
		String seq = file.read(), tmp = "";
		while((tmp = file.read()) != null) {
			seq += tmp.charAt(tmp.length() - 1);
		}
		
		file.write(seq);
		file.close();
	}
}
