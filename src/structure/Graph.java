package structure;

import java.util.List;
import java.util.ArrayList;

public class Graph {
	public List<GraphNode> nodeIterator = null;
	public GraphNode root = null;
	public boolean[][] adjmap = null;
	
	// create graph with single root
	public Graph(Object rcargo) {
		this.nodeIterator = new ArrayList<GraphNode>();
		this.root = new GraphNode(rcargo);
		
		nodeIterator.add(root);
	}
	
	// create graph with N empty nodes
	public Graph(int n) {
		this.nodeIterator = new ArrayList<GraphNode>();
		this.adjmap = new boolean[n][n];
		for(int i = 0; i < n; i++) {
			nodeIterator.add(new GraphNode(null));
			nodeIterator.get(i).index = i;
		}
		this.root = nodeIterator.get(0);
	}
	
	public void createEdge(int v, int w) {
		nodeIterator.get(v).connect(nodeIterator.get(w));
		adjmap[v][w] = true;
	}
	
	public void createEdge(int src, int dst, int wgt) {
		nodeIterator.get(src).connect(nodeIterator.get(dst), wgt);
		adjmap[src][dst] = true;
	}
}
