package tbtrack.chapter7;

import tools.*;
import java.util.*;

class GraphEdge {
	public int src, dst, wgt;
	
	public GraphEdge(int s, int d) {
		this.src = s;
		this.dst = d;
		this.wgt = -1;
	}
	
	public GraphEdge(int s, int d, int w) {
		this.src = s;
		this.dst = d;
		this.wgt = w;
	}
}

public class Src_BA7C {
	private static int V, INTERNAL;
	
	private static List<GraphEdge> getPath(List<GraphEdge> edgeList, int src, int dst, int curr, boolean[] visit){
		if(curr == dst) return new ArrayList<GraphEdge>();
		if(curr != src && curr < V) return null;
		
		visit[curr] = true;
		List<GraphEdge> path = new ArrayList<GraphEdge>();
		for(GraphEdge edge : edgeList) {
			if(edge.src == curr && !visit[edge.dst]) {
				path.add(edge);
				
				List<GraphEdge> subPath = getPath(edgeList, src, dst, edge.dst, visit);
				if(subPath != null) {
					path.addAll(subPath);
					return path;
				}
				
				path.remove(edge);
			}
		}
		
		return null;
	}
	
	private static void addBranch(List<GraphEdge> edgeList, int dst, int dist, int limb) {
		List<GraphEdge> dstPath = getPath(edgeList, 0, dst, 0, new boolean[INTERNAL]);
		
		int loc, dcopy = dist;
		for(loc = 0; dcopy >= 0 && loc < dstPath.size(); dcopy -= dstPath.get(loc++).wgt);
		
		if(dcopy == 0) {
			edgeList.add(new GraphEdge(dstPath.get(loc).src, V, limb));
			edgeList.add(new GraphEdge(V, dstPath.get(loc).src, limb));
			return;
		}
//		if(dcopy > 0) dcopy -= dstPath.get(loc - 1).wgt;
		
		// dstPath[loc - 1].src -/wgt + dcopy/-> placement -/-dcopy/-> dstPath[loc - 1].dst
		int plsrc = dstPath.get(loc - 1).src, pldst = dstPath.get(loc - 1).dst;
		for(int i = 0; i < edgeList.size(); i++) {
			if(edgeList.get(i).src == plsrc && edgeList.get(i).dst == pldst) {
				edgeList.remove(i);
				break;
			}
		}
		for(int i = 0; i < edgeList.size(); i++) {
			if(edgeList.get(i).dst == plsrc && edgeList.get(i).src == pldst) {
				edgeList.remove(i);
				break;
			}
		}
		
		edgeList.add(new GraphEdge(plsrc, INTERNAL, dstPath.get(loc - 1).wgt + dcopy));
		edgeList.add(new GraphEdge(INTERNAL, plsrc, dstPath.get(loc - 1).wgt + dcopy));
		edgeList.add(new GraphEdge(INTERNAL, pldst, -dcopy));
		edgeList.add(new GraphEdge(pldst, INTERNAL, -dcopy));
		edgeList.add(new GraphEdge(INTERNAL, V, limb));
		edgeList.add(new GraphEdge(V, INTERNAL, limb));
		INTERNAL++;
		
		return;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO();
		int n = Integer.parseInt(file.read());
		
		int[][] additive = new int[n][n];
		for(int i = 0; i < n; i++) {
			String line = file.read();
			int j = 0;
			for(String w : line.split(" ")) additive[i][j++] = Integer.parseInt(w);
		}
		
		List<GraphEdge> edgeList = new ArrayList<GraphEdge>();
		
		// Initial condition
		edgeList.add(new GraphEdge(0, 1, additive[0][1]));
		edgeList.add(new GraphEdge(1, 0, additive[1][0]));
		INTERNAL = n;
		
		// Extension
		for(V = 2; V < n; V++) {
			// Limb length calculation
			int[][] subAdditive = new int[V + 1][V + 1];
			for(int i = 0; i <= V; i++) {
				for(int j = 0; j <= V; j++) {
					subAdditive[i][j] = additive[i][j];
				}
			}
			int vLimb = Src_BA7B.limb(subAdditive, V);
			
			// Source, destination decision
			int src = 0, dst;
			for(dst = 1; dst < V; dst++) {
				if(additive[src][dst] == (additive[src][V] - vLimb) + (additive[V][dst] - vLimb)) break;
			}
			
			// Branch addition
			addBranch(edgeList, dst, additive[src][V] - vLimb, vLimb);
		}
		
		for(GraphEdge edge : edgeList) {
			file.write(String.format("%d->%d:%d", edge.src, edge.dst, edge.wgt));
		}
		file.close();
	}
}
