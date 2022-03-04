package tbtrack.chapter5;

import tools.*;

public class Src_BA5E {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'e');
		Blosum bs = new Blosum('B');
		
		int sigma = 5; // indel penalty
		String pepx = file.read(), pepy = file.read();
		int[][] score = new int[pepx.length() + 1][pepy.length() + 1];
		int[][] backtrack = new int[pepx.length() + 1][pepy.length() + 1]; // 1 : upward; 2 : leftward; 3 : diagonal;
		
		for(int i = 1; i <= pepx.length(); i++) {
			score[i][0] = score[i - 1][0] - sigma;
			backtrack[i][0] = 1;
		}
		
		for(int j = 1; j <= pepy.length(); j++) {
			score[0][j] = score[0][j - 1] - sigma;
			backtrack[0][j] = 2;
		}
		
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
				
				score[i][j] = mscore;
				backtrack[i][j] = mtrack;
			}
		}
		
		file.writeObj(score[pepx.length()][pepy.length()]);
		
		String trackx = "", tracky = "";
		int i = pepx.length(), j = pepy.length();
		while(i > 0 || j > 0) {
			if(backtrack[i][j] == 1) {
				trackx = pepx.charAt(i-- - 1) + trackx;
				tracky = '-' + tracky;
			}
			else if(backtrack[i][j] == 2) {
				trackx = '-' + trackx;
				tracky = pepy.charAt(j-- - 1) + tracky;
			}
			else {
				trackx = pepx.charAt(i-- - 1) + trackx;
				tracky = pepy.charAt(j-- - 1) + tracky;
			}
		}
		
		file.write(trackx);
		file.write(tracky);
		file.close();
	}
}
