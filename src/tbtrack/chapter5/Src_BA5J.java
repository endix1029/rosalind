package tbtrack.chapter5;

import tools.*;

public class Src_BA5J {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'j');
		Blosum bs = new Blosum('B');
		
		int gap_open = 11, gap_ext = 1;
		String pepx = file.read(), pepy = file.read();
		int[][] score = new int[pepx.length() + 1][pepy.length() + 1];
		int[][] backtrack = new int[pepx.length() + 1][pepy.length() + 1]; // n : upward gaplen; -n : leftward gaplen; 0 : diagonal 
		
		for(int i = 1; i <= pepx.length(); i++) {
			score[i][0] = -(gap_open + gap_ext * (i - 1));
			backtrack[i][0] = i;
		}
		
		for(int j = 1; j <= pepy.length(); j++) {
			score[0][j] = -(gap_open + gap_ext * (j - 1));
			backtrack[0][j] = -j;
		}
		
		for(int i = 1; i <= pepx.length(); i++) {
			for(int j = 1; j <= pepy.length(); j++) {
				int mscore = score[i - 1][j - 1] + bs.get(pepx.charAt(i - 1), pepy.charAt(j - 1));
				int mtrack = 0;
				
				for(int k = 0; k < i; k++) {
					if(score[k][j] - (gap_open + gap_ext * (i - k - 1)) > mscore) {
						mscore = score[k][j] - (gap_open + gap_ext * (i - k - 1));
						mtrack = i - k;
					}
				}
				
				for(int k = 0; k < j; k++) {
					if(score[i][k] - (gap_open + gap_ext * (j - k - 1)) > mscore) {
						mscore = score[i][k] - (gap_open + gap_ext * (j - k - 1));
						mtrack = k - j;
					}
				}
				
				score[i][j] = mscore;
				backtrack[i][j] = mtrack;
			}
		}
		
		file.writeObj(score[pepx.length()][pepy.length()]);
		
		String trackx = "", tracky = "";
		int i = pepx.length(), j = pepy.length();
		while(i > 0 || j > 0) {
			if(backtrack[i][j] > 0) {
				trackx = pepx.substring(i - backtrack[i][j], i) + trackx;
				for(int k = 0; k < backtrack[i][j]; k++) tracky = '-' + tracky;
				i -= backtrack[i][j];
			}
			else if(backtrack[i][j] < 0) {
				for(int k = 0; k < -backtrack[i][j]; k++) trackx = '-' + trackx;
				tracky = pepy.substring(j + backtrack[i][j], j) + tracky;
				j += backtrack[i][j];
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
