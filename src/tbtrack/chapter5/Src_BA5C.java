package tbtrack.chapter5;

import tools.*;

public class Src_BA5C {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'c');
		String seqX = file.read(), seqY = file.read();
		
		int[][] lcs = new int[seqX.length() + 1][seqY.length() + 1];
		int[][] backtrack = new int[seqX.length() + 1][seqY.length() + 1]; // 1 : upward; 2 : leftward; 3 : diagonal;
		
		for(int i = 0; i <= seqX.length(); i++) lcs[i][0] = 0;
		for(int j = 0; j <= seqY.length(); j++) lcs[0][j] = 0;
		for(int i = 1; i <= seqX.length(); i++) backtrack[i][0] = 1;
		for(int j = 1; j <= seqY.length(); j++) backtrack[0][j] = 2;
		
		for(int i = 1; i <= seqX.length(); i++){
			for(int j = 1; j <= seqY.length(); j++){
				if(seqX.charAt(i - 1) == seqY.charAt(j - 1)) {
					lcs[i][j] = lcs[i - 1][j - 1] + 1;
					backtrack[i][j] = 3;
				}
				else if(lcs[i - 1][j] > lcs[i][j - 1]) {
					lcs[i][j] = lcs[i - 1][j];
					backtrack[i][j] = 1;
				}
				else {
					lcs[i][j] = lcs[i][j - 1];
					backtrack[i][j] = 2;
				}
			}
		}
		
		int i = seqX.length(), j = seqY.length();
		String lcstr = "";
		while(i > 0 && j > 0) {
			if(backtrack[i][j] == 3) {
				lcstr = seqX.charAt(i - 1) + lcstr;
				i--;
				j--;
			}
			else if(backtrack[i][j] == 2) j--;
			else i--;
		}
		
		file.write(lcstr);
		file.close();
	}
}
