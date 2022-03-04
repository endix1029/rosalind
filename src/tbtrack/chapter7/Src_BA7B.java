package tbtrack.chapter7;

import tools.*;

public class Src_BA7B {
	public static int limb(int[][] dist, int x) {
		int p = x == 0 ? 1 : 0;
		int limb = Integer.MAX_VALUE;
		for(int q = 0; q < dist.length; q++) {
			if(p == q) continue;
			if(x == q) continue;
			
			int l = (dist[p][x] + dist[q][x] - dist[p][q]) / 2;
			if(l < limb) limb = l;
		}
		
		return limb;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'b');
		int n = Integer.parseInt(file.read()), x = Integer.parseInt(file.read());
		
		int[][] dist = new int[n][n];
		for(int i = 0; i < n; i++) {
			String dstr = file.read();
			int j = 0;
			for(String d : dstr.split(" ")) dist[i][j++] = Integer.parseInt(d);
		}
		
		file.writeObj(limb(dist, x));
		file.close();
	}
}
