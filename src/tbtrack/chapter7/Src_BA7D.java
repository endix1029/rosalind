package tbtrack.chapter7;

import tools.*;
import java.util.*;

class DoubleEdge extends GraphEdge{
	public double wgt;
	public DoubleEdge(int s, int d, double w) {
		super(s, d);
		this.wgt = w;
	}
}

public class Src_BA7D {
	private static int INTERVAL;
	private static List<Integer> INDEX = new ArrayList<Integer>();
	private static List<Integer> LEAF  = new ArrayList<Integer>();
	private static List<Double>  AGE   = new ArrayList<Double> ();
	
	private static double[][] cluster(double[][] adj, int p, int q, List<DoubleEdge> edgeList) {
		double[][] clusterAdj = new double[adj.length - 1][adj.length - 1];
		
		double age = adj[p][q] / 2;
		edgeList.add(new DoubleEdge(INDEX.get(p), INTERVAL, age - AGE.get(INDEX.get(p))));
		edgeList.add(new DoubleEdge(INTERVAL, INDEX.get(p), age - AGE.get(INDEX.get(p))));
		edgeList.add(new DoubleEdge(INDEX.get(q), INTERVAL, age - AGE.get(INDEX.get(q))));
		edgeList.add(new DoubleEdge(INTERVAL, INDEX.get(q), age - AGE.get(INDEX.get(q))));
		
		int ic = 0, jc = 0;
		for(int i = 0; i < adj.length; i++) {
			if(i == p || i == q) continue;
			
			jc = 0;
			for(int j = 0; j < adj.length; j++) {
				if(j == p || j == q) continue;
				clusterAdj[ic][jc] = adj[i][j];
				jc++;
			}
			
			ic++;
		}
		
		ic = 0;
		for(int i = 0; i < adj.length; i++) {
			if(i == p || i == q) continue;
			
			clusterAdj[ic][adj.length - 2] = 
					(adj[i][p] * LEAF.get(INDEX.get(p)) + adj[i][q] * LEAF.get(INDEX.get(q))) 
					/ (LEAF.get(INDEX.get(p)) + LEAF.get(INDEX.get(q)));
			clusterAdj[adj.length - 2][ic] = clusterAdj[ic][adj.length - 2];
			
			ic++;
		}
		
		LEAF.add(LEAF.get(INDEX.get(p)) + LEAF.get(INDEX.get(q)));
		AGE.add(age);
		
		if(p > q) {
			INDEX.remove(p);
			INDEX.remove(q);
		}
		else {
			INDEX.remove(q);
			INDEX.remove(p);
		}
		
		INDEX.add(INTERVAL++);				
		return clusterAdj;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'd');
		int n = Integer.parseInt(file.read());
		
		double[][] adj = new double[n][n];
		for(int i = 0; i < n; i++) {
			String line = file.read();
			int j = 0;
			for(String w : line.split("\t")) adj[i][j++] = Integer.parseInt(w);
			INDEX.add(i);
			LEAF.add(1);
			AGE.add(.0);
		}
		
		List<DoubleEdge> edgeList = new ArrayList<DoubleEdge>();
		INTERVAL = n;
		
		while(n-- > 1) {
			double minDist = Integer.MAX_VALUE;
			int minp = -1, minq = -1;
			for(int i = 0; i <= n; i++) {
				for(int j = i + 1; j <= n; j++) {
					if(adj[i][j] < minDist) {
						minDist = adj[i][j];
						minp = i;
						minq = j;
					}
				}
			}
			
			adj = cluster(adj, minp, minq, edgeList);
		}
		
		for(DoubleEdge edge : edgeList) {
			file.write(String.format("%d->%d:%.3f", edge.src, edge.dst, edge.wgt));
		}
		file.close();
	}
}
