package tbtrack.chapter5;

import tools.*;

public class Src_BA5H {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'h');

		int penalty = 1;
		String sx = file.read(), sy = file.read();
		int[][] score = new int[sx.length() + 1][sy.length() + 1];
		int[][] backtrack = new int[sx.length() + 1][sy.length() + 1]; // 1 : upward; 2 : leftward; 3 : diagonal; 4 : renewal;
		
		for(int i = 1; i <= sx.length(); i++) {
			score[i][0] = 0;
			backtrack[i][0] = 4;
		}
		
		for(int j = 1; j <= sy.length(); j++) {
			score[0][j] = -j;
			backtrack[0][j] = 2;
		}
		
		int gmax = 0, gi = 0, gj = sy.length();
		for(int i = 1; i <= sx.length(); i++) {
			for(int j = 1; j <= sy.length(); j++) {
				int mscore = score[i - 1][j] - penalty, mtrack = 1;
				
				if(mscore < score[i][j - 1] - penalty) {
					mscore = score[i][j - 1] - penalty;
					mtrack = 2;
				}
				if(sx.charAt(i - 1) == sy.charAt(j - 1)) {
					if(mscore < score[i - 1][j - 1] + 1) {
						mscore = score[i - 1][j - 1] + 1;
						mtrack = 3;
					}
				}
				else {
					if(mscore < score[i - 1][j - 1] - penalty) {
						mscore = score[i - 1][j - 1] - penalty;
						mtrack = 3;
					}
				}
				
				score[i][j] = mscore;
				backtrack[i][j] = mtrack;
				if(j == sy.length()) {
					if(gmax < mscore) {
						gmax = mscore;
						gi = i;
					}
				}
			}
		}
		
		file.writeObj(gmax);

		String trackx = "", tracky = "";
		while((gi > 0 || gj > 0) && backtrack[gi][gj] != 4) {
			if(backtrack[gi][gj] == 1) {
				trackx = sx.charAt(gi-- - 1) + trackx;
				tracky = '-' + tracky;
			}
			else if(backtrack[gi][gj] == 2) {
				trackx = '-' + trackx;
				tracky = sy.charAt(gj-- - 1) + tracky;
			}
			else {
				trackx = sx.charAt(gi-- - 1) + trackx;
				tracky = sy.charAt(gj-- - 1) + tracky;
			}
		}
		
		file.write(trackx);
		file.write(tracky);
		file.close();
	}
}
