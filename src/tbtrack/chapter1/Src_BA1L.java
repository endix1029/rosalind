package tbtrack.chapter1;

import tools.*;

public class Src_BA1L {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'l');
		String seq = file.read();
		file.writeObj(Functions.seq_longhash(seq));
		file.close();
	}
}
