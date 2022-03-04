package tbtrack.chapter4;

import tools.*;

public class Src_BA4D {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'd');
		MassTable mt = new MassTable(true);
		
		int mass = Integer.parseInt(file.read());
		long[] count = new long[mass + 1];
		
		int[] mtable = new int[20];
		for(int i = 0; i < 20; i++) mtable[i] = mt.getSimpleIndex(i);
		
		int[] mtable_ = {57, 71, 87, 97, 99, 101, 103, 113, 114, 115, 128, 129, 131, 137, 147, 156, 163, 186};
		count[0] = 1;
		for(int m = 1; m <= mass; m++) {
			for(int am : mtable_) {
				if(m >= am) count[m] += count[m - am];
			}
		}
		
		file.writeObj(count[mass]);
		file.close();
	}
}
