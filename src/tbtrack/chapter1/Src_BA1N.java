package tbtrack.chapter1;

import tools.*;
import java.util.ArrayList;

public class Src_BA1N {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'n');
		
		String seq = file.read();
		int d = Integer.parseInt(file.read());
		
		ArrayList<String> neighs = Functions.neighbor(seq, d);
		for(String neigh : neighs) {
			file.write(neigh);
		}
		
		file.close();
	}
}
