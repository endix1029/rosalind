package tbtrack.chapter1;

import tools.*;

public class Src_BA1M {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'm');
		int val = Integer.parseInt(file.read()), k = Integer.parseInt(file.read());
		file.write(Functions.seq_revhash(val, k));
		file.close();
	}
}
