package tbtrack.chapter7;

import tools.*;
import java.util.*;

class BioTree {
	public final int nleaf, maxv;
	private int[][] adj = null;
	
	public BioTree(int n, int v) {
		this.nleaf = n;
		this.maxv = v;
		this.adj = new int[maxv + 1][maxv + 1];
	}
	
	public void insert(int src, int dst, int wgt) {
		this.adj[src][dst] = wgt;
	}
	
	private int dfs(int loc, int dst, int depth, boolean[] visit) {
		if(loc == dst) return depth;
		if(depth > 0 && loc < nleaf) return -1;
		
		visit[loc] = true;
		for(int i = 0; i <= maxv; i++) {
			if(adj[loc][i] > 0 && !visit[i]) {
				int val = dfs(i, dst, depth + adj[loc][i], visit);
				if(val > 0) return val;
			}
		}
		
		return -1;
	}
	
	public int dist(int src, int dst) {
		boolean[] visit = new boolean[maxv + 1];
		return dfs(src, dst, 0, visit);
	}
}

public class Src_BA7A {
	public static int MAXV = -1;
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'a');
		int n = Integer.parseInt(file.read());
		
		String line;
		List<int[]> metalist = new ArrayList<int[]>();
		
		while((line = file.read()) != null) {
			String[] espl = line.split("->");
			int src = Integer.parseInt(espl[0]);
				
			String[] dspl = espl[1].split(":");
			int dst = Integer.parseInt(dspl[0]);
			int wgt = Integer.parseInt(dspl[1]);
			
			int[] meta = {src, dst, wgt};
			metalist.add(meta);
			
			if(src > MAXV) MAXV = src;
			if(dst > MAXV) MAXV = src;
		}
		
		BioTree bt = new BioTree(n, MAXV);
		for(int[] meta : metalist) {
			bt.insert(meta[0], meta[1], meta[2]);
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				file.type(bt.dist(i, j) + " ");
			}
			file.write("");
		}
		
		file.close();
	}
}
