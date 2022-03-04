package tbtrack.chapter5;

import tools.*;

public class Src_BA5G {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'g');
		String sx = file.read(), sy = file.read();
		
		int[][] dist = new int[sx.length() + 1][sy.length() + 1];
		for(int i = 1; i <= sx.length(); i++) dist[i][0] = i;
		for(int j = 1; j <= sy.length(); j++) dist[0][j] = j;
		
		for(int i = 1; i <= sx.length(); i++) {
			for(int j = 1; j <= sy.length(); j++) {
				int mdist = dist[i - 1][j] + 1;
				
				if(mdist > dist[i][j - 1] + 1) mdist = dist[i][j - 1] + 1;
				if(sx.charAt(i - 1) == sy.charAt(j - 1)) {
					if(mdist > dist[i - 1][j - 1]) mdist = dist[i - 1][j - 1];
				}
				else {
					if(mdist > dist[i - 1][j - 1] + 1) mdist = dist[i - 1][j - 1] + 1;
				}
				
				dist[i][j] = mdist;
			}
		}
		
		file.writeObj(dist[sx.length()][sy.length()]);
		file.close();
	}
}
