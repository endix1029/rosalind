package structure;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
	public Object cargo = null;
	public int index = -1;
	public boolean visited = false;
	
	public List<GraphNode> adj = null;
	public List<Integer> wgt = null;
	
	public GraphNode(Object cargo) {
		this.cargo = cargo;
		this.adj = new ArrayList<GraphNode>();
		this.wgt = new ArrayList<Integer>();
	}
	
	public void connect(GraphNode node) {
		this.adj.add(node);
	}
	
	public void connect(GraphNode node, int w) {
		this.adj.add(node);
		this.wgt.add(w);
	}
	
	public void load(Object cargo) {
		this.cargo = cargo;
	}
}
