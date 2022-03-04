package tbtrack.chapter7;

import tools.*;

public class Src_BA7E {
	private static int INTERVAL;
	
	private static DoubleEdge[] nj(double[][] adj, int[] index, int n) {
		if(n == 2) {
			DoubleEdge ex = new DoubleEdge(index[0], index[1], adj[0][1]);
			DoubleEdge ey = new DoubleEdge(index[1], index[0], adj[1][0]);
			DoubleEdge[] tree = {ex, ey};
			return tree;
		}
		
		double[][] njMatrix = new double[n][n];
		double[] totalDist = new double[n];
		
		// total distance matrix calculation
		for(int i = 0; i < n; i++) {
			double dsum = 0;
			for(int j = 0; j < n; j++) dsum += adj[i][j];
			totalDist[i] = dsum;
		}
		
		// neighbor-joining value calculation
		double minNj = Double.MAX_VALUE;
		int p = -1, q = -1;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(i == j) {
					njMatrix[i][j] = 0;
					continue;
				}
				
				njMatrix[i][j] = (n - 2) * adj[i][j] - totalDist[i] - totalDist[j];
				if(njMatrix[i][j] < minNj) {
					minNj = njMatrix[i][j];
					p = i; q = j;
				}
			}
		}
		
		double delta = (totalDist[p] - totalDist[q]) / (n - 2);
		double limbP = (adj[p][q] + delta) / 2;
		double limbQ = (adj[p][q] - delta) / 2;
		int newNode = INTERVAL;
		
		// construct temporarily extended matrix
		double[][] tmpMatrix = new double[n + 1][n + 1];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				tmpMatrix[i][j] = adj[i][j];
			}
		}
		for(int i = 0; i < n; i++) tmpMatrix[i][n] = (adj[i][p] + adj[i][q] - adj[p][q]) / 2;
		for(int j = 0; j < n; j++) tmpMatrix[n][j] = tmpMatrix[j][n];
		
		// construct reduced matrix
		double[][] reducedMatrix = new double[n - 1][n - 1];
		int ir = 0, jr = 0;
		for(int i = 0; i <= n; i++) {
			if(i == p || i == q) continue;
			jr = 0;
			for(int j = 0; j <= n; j++) {
				if(j == p || j == q) continue;
				reducedMatrix[ir][jr++] = tmpMatrix[i][j];
			}
			ir++;
		}
		
		// compute reduced index list
		int[] reducedIndex = new int[n - 1];
		ir = 0;
		for(int i = 0; i < n; i++) {
			if(i == p || i == q) continue;
			reducedIndex[ir++] = index[i];
		}
		reducedIndex[n - 2] = INTERVAL++;
		
		DoubleEdge[] subTree = nj(reducedMatrix, reducedIndex, n - 1);
		int len = subTree.length;
		DoubleEdge[] tree = new DoubleEdge[len + 4];
		for(int i = 0; i < len; i++) tree[i] = subTree[i];
		
		tree[len]     = new DoubleEdge(index[p], newNode, limbP);
		tree[len + 1] = new DoubleEdge(newNode, index[p], limbP);
		tree[len + 2] = new DoubleEdge(index[q], newNode, limbQ);
		tree[len + 3] = new DoubleEdge(newNode, index[q], limbQ);
		
		return tree;
	}
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'e');
		int n = Integer.parseInt(file.read());
		
		double[][] adj = new double[n][n];
		for(int i = 0; i < n; i++) {
			String line = file.read();
			int j = 0;
			for(String w : line.split("\t")) adj[i][j++] = Integer.parseInt(w);
		}
		
		INTERVAL = n;
		int[] index = new int[n];
		for(int i = 0; i < n; i++) index[i] = i;
		
		DoubleEdge[] njTree = nj(adj, index, n);
		for(DoubleEdge edge : njTree) {
			file.write(String.format("%d->%d:%.3f", edge.src, edge.dst, edge.wgt));
		}
		
		file.close();
	}
}
