package tbtrack.chapter5;

import tools.*;

public class Src_BA5M {
	private static int[][][] score, track;
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'm');
		String seqx = file.read(), seqy = file.read(), seqz = file.read();
		
		score = new int[seqx.length() + 1][seqy.length() + 1][seqz.length() + 1];
		track = new int[seqx.length() + 1][seqy.length() + 1][seqz.length() + 1];
		
		for(int i = 1; i <= seqx.length(); i++) track[i][0][0] = 4;
		for(int j = 1; j <= seqy.length(); j++) track[0][j][0] = 2;
		for(int k = 1; k <= seqz.length(); k++) track[0][0][k] = 1;
		for(int i = 1; i <= seqx.length(); i++)
			for(int j = 1; j <= seqy.length(); j++) track[i][j][0] = 6;
		for(int i = 1; i <= seqx.length(); i++)
			for(int k = 1; k <= seqz.length(); k++) track[i][0][k] = 5;
		for(int j = 1; j <= seqy.length(); j++)
			for(int k = 1; k <= seqz.length(); k++) track[0][j][k] = 3;
		
		for(int i = 1; i <= seqx.length(); i++) {
			for(int j = 1; j <= seqy.length(); j++) {
				for(int k = 1; k <= seqz.length(); k++) {
					// case 1
					int mScore = score[i][j][k - 1], mTrack = 1;
					// case 2
					if(score[i][j - 1][k] > mScore) {
						mScore = score[i][j - 1][k];
						mTrack = 2;
					}
					// case 3
					if(score[i][j - 1][k - 1] > mScore) {
						mScore = score[i][j - 1][k - 1];
						mTrack = 3;
					}
					// case 4
					if(score[i - 1][j][k] > mScore) {
						mScore = score[i - 1][j][k];
						mTrack = 4;
					}
					// case 5
					if(score[i - 1][j][k - 1] > mScore) {
						mScore = score[i - 1][j][k - 1];
						mTrack = 5;
					}
					// case 6
					if(score[i - 1][j - 1][k] > mScore) {
						mScore = score[i - 1][j - 1][k];
						mTrack = 6;
					}
					// case 7
					boolean iden = (seqx.charAt(i - 1) == seqy.charAt(j - 1)) & (seqx.charAt(i - 1) == seqz.charAt(k - 1));
					if(score[i - 1][j - 1][k - 1] + (iden ? 1 : 0) > mScore) {
						mScore = score[i - 1][j - 1][k - 1] + (iden ? 1 : 0);
						mTrack = 7;
					}
					
					// assignment
					score[i][j][k] = mScore;
					track[i][j][k] = mTrack;
				}
			}
		}
		
		file.writeObj(score[seqx.length()][seqy.length()][seqz.length()]);
		
		String trackx = "", tracky = "", trackz = "";
		int iterx = seqx.length(), itery = seqy.length(), iterz = seqz.length();
		while(iterx > 0 | itery > 0 | iterz > 0) {
			switch(track[iterx][itery][iterz]) {
			case 1:
				trackx = '-' + trackx;
				tracky = '-' + tracky;
				trackz = seqz.charAt(iterz-- - 1) + trackz;
				break;
			case 2:
				trackx = '-' + trackx;
				tracky = seqy.charAt(itery-- - 1) + tracky;
				trackz = '-' + trackz;
				break;
			case 3:
				trackx = '-' + trackx;
				tracky = seqy.charAt(itery-- - 1) + tracky;
				trackz = seqz.charAt(iterz-- - 1) + trackz;
				break;
			case 4:
				trackx = seqx.charAt(iterx-- - 1) + trackx;
				tracky = '-' + tracky;
				trackz = '-' + trackz;
				break;
			case 5:
				trackx = seqx.charAt(iterx-- - 1) + trackx;
				tracky = '-' + tracky;
				trackz = seqz.charAt(iterz-- - 1) + trackz;
				break;
			case 6:
				trackx = seqx.charAt(iterx-- - 1) + trackx;
				tracky = seqy.charAt(itery-- - 1) + tracky;
				trackz = '-' + trackz;
				break;
			case 7:
				trackx = seqx.charAt(iterx-- - 1) + trackx;
				tracky = seqy.charAt(itery-- - 1) + tracky;
				trackz = seqz.charAt(iterz-- - 1) + trackz;
				break;
			}
		}
		
		file.write(trackx);
		file.write(tracky);
		file.write(trackz);
		file.close();
	}
}
