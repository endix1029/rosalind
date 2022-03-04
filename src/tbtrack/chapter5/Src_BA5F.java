package tbtrack.chapter5;

import tools.*;

public class Src_BA5F {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'f');
		Blosum bs = new Blosum('F');
		
		int sigma = 5; // indel penalty
		String pepx = file.read(), pepy = file.read();
		int[][] score = new int[pepx.length() + 1][pepy.length() + 1];
		int[][] backtrack = new int[pepx.length() + 1][pepy.length() + 1]; // 1 : upward; 2 : leftward; 3 : diagonal; 4 : renewal;
		
		for(int i = 1; i <= pepx.length(); i++) {
			score[i][0] = 0;
			backtrack[i][0] = 4;
		}
		
		for(int j = 1; j <= pepy.length(); j++) {
			score[0][j] = 0;
			backtrack[0][j] = 4;
		}
		
		int gmax = 0, gi = 0, gj = 0;
		for(int i = 1; i <= pepx.length(); i++) {
			for(int j = 1; j <= pepy.length(); j++) {
				int mscore = score[i - 1][j] - sigma, mtrack = 1;
				
				if(mscore < score[i][j - 1] - sigma) {
					mscore = score[i][j - 1] - sigma;
					mtrack = 2;
				}
				if(mscore < score[i - 1][j - 1] + bs.get(pepx.charAt(i - 1), pepy.charAt(j - 1))) {
					mscore = score[i - 1][j - 1] + bs.get(pepx.charAt(i - 1), pepy.charAt(j - 1));
					mtrack = 3;
				}
				if(mscore < 0) {
					mscore = 0;
					mtrack = 4;
				}
				
				score[i][j] = mscore;
				backtrack[i][j] = mtrack;
				if(gmax < mscore) {
					gmax = mscore;
					gi = i;
					gj = j;
				}
			}
		}
		
		file.writeObj(gmax);

		String trackx = "", tracky = "";
		while((gi > 0 || gj > 0) && backtrack[gi][gj] != 4) {
			if(backtrack[gi][gj] == 1) {
				trackx = pepx.charAt(gi-- - 1) + trackx;
				tracky = '-' + tracky;
			}
			else if(backtrack[gi][gj] == 2) {
				trackx = '-' + trackx;
				tracky = pepy.charAt(gj-- - 1) + tracky;
			}
			else {
				trackx = pepx.charAt(gi-- - 1) + trackx;
				tracky = pepy.charAt(gj-- - 1) + tracky;
			}
		}
		
		file.write(trackx);
		file.write(tracky);
		file.close();
	}
}
