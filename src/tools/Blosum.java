package tools;

import java.io.*;

public class Blosum {
	private int[][] db;

	public Blosum(char query) throws IOException {
		this.db = new int[20][20];
		switch(query) {
		case 'B' : this.parse("BLOSUM62"); break;
		case 'F' : this.parse("PAM250"); break;
		default : return;
		}
	}
	
	private static final int[] PHASH = {0,-1,1,2,3,4,5,6,7,-1,8,9,10,11,-1,12,13,14,15,16,-1,17,18,-1,19,-1};
	// protein to number
	private int pton(char p) {
		return PHASH[(int) p - 'A'];
	}
	
	private void parse(String tableName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("lib/table/" + tableName + ".txt"));

		String line = br.readLine();
		int iter = 0;
		while((line = br.readLine()) != null) {
			String[] vals = line.split("\t");
			for(int loc = 1; loc < vals.length; loc++) {
				this.db[iter][loc - 1] = Integer.parseInt(vals[loc]);
			}
			iter++;
		}
		
		br.close();
	}
	
	public int get(char px, char py) {
		return this.db[pton(px)][pton(py)];
	}
}
