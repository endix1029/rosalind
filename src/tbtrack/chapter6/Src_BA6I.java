package tbtrack.chapter6;

import tools.*;
import java.util.*;

public class Src_BA6I {
	private static int notVisited(boolean[] visited) {
		for(int i = 0; i < visited.length; i++) {
			if(!visited[i]) return i;
		}
		return -1;
	}
	
	public static List<List<Integer>> graphToGenome(List<int[]> coloredEdges){
		List<List<Integer>> genome = new ArrayList<List<Integer>>();
		
		boolean[] visited = new boolean[coloredEdges.size()];
		
		int next;
		while((next = notVisited(visited)) >= 0) {
			List<Integer> chr = new ArrayList<Integer>();
			
			int[] currentEdge = coloredEdges.get(next);
			do {
				visited[next] = true;
				
				int node, nextStart;
				if(currentEdge[1] % 2 == 1) {
					node = currentEdge[1] / 2 + 1;
					nextStart = node * 2;
				}
				else {
					node = -(currentEdge[1] / 2);
					nextStart = node * -2 - 1;
				}
				
				chr.add(node);
				
				for(next = 0; next < coloredEdges.size(); next++) {
					if(coloredEdges.get(next)[0] == nextStart) break;
				}
				if(next == coloredEdges.size()) {
					for(next = 0; next < coloredEdges.size(); next++) {
						if(coloredEdges.get(next)[1] == nextStart) {
							coloredEdges.get(next)[1] = coloredEdges.get(next)[0];
							coloredEdges.get(next)[0] = nextStart;
							break;
						}
					}
				}
				
				if(next < coloredEdges.size()) currentEdge = coloredEdges.get(next);
				else next = -1;
			} while(next > 0 && !visited[next]);
			
			chr.add(0, chr.remove(chr.size() - 1));
			genome.add(chr);
		}
		
		return genome;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'i');
		String edgeStr = file.read();
		
		List<int[]> coloredEdges = new ArrayList<int[]>();
		for(String edge : edgeStr.substring(1, edgeStr.length() - 1).split("\\), \\(")) {
			int edgex = Integer.parseInt(edge.split(", ")[0]);
			int edgey = Integer.parseInt(edge.split(", ")[1]);
			int[] coloredEdge = {edgex, edgey};
			coloredEdges.add(coloredEdge);
		}
		
		List<List<Integer>> genome = graphToGenome(coloredEdges);
		for(List<Integer> chr : genome) file.type(Src_BA6A.permString(chr));
		file.close();
	}
}
