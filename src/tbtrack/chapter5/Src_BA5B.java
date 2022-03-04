package tbtrack.chapter5;

import tools.FileIO;

public class Src_BA5B {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'b');
		String[] vspl = file.read().split(" ");
		int n = Integer.parseInt(vspl[0]), m = Integer.parseInt(vspl[1]);
		
		int[][] vdist = new int[n][m + 1];
		int[][] hdist = new int[n + 1][m];
		
		for(int i = 0; i < n; i++) {
			vspl = file.read().split(" ");
			for(int j = 0; j <= m; j++) {
				vdist[i][j] = Integer.parseInt(vspl[j]);
			}
		}
		file.read();
		for(int i = 0; i <= n; i++) {
			vspl = file.read().split(" ");
			for(int j = 0; j < m; j++) {
				hdist[i][j] = Integer.parseInt(vspl[j]);
			}
		}
		
		int[][] dist = new int[n + 1][m + 1];
		dist[0][0] = 0;
		for(int i = 1; i <= n; i++) dist[i][0] = dist[i - 1][0] + vdist[i - 1][0];
		for(int j = 1; j <= m; j++) dist[0][j] = dist[0][j - 1] + hdist[0][j - 1];
		for(int i = 1; i <= n; i++) {
			for(int j = 1; j <= m; j++) {
				dist[i][j] = dist[i - 1][j] + vdist[i - 1][j];
				if(dist[i][j] < dist[i][j - 1] + hdist[i][j - 1]) {
					dist[i][j] = dist[i][j - 1] + hdist[i][j - 1];
				}
			}
		}
		
		file.writeObj(dist[n][m]);
		file.close();
	}
}
