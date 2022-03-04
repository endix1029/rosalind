package tbtrack.chapter6;

import java.util.*;
import tools.*;

public class Src_BA6J {
	public static List<int[]> breakGraph(List<int[]> coloredEdges, int x, int xp, int y, int yp){
		List<int[]> newEdges = new ArrayList<>();
		
		for(int[] edge : coloredEdges) {
			if(edge[0] == x && edge[1] == xp) continue;
			else if(edge[0] == xp && edge[1] == x) continue;
			else if(edge[0] == y && edge[1] == yp) continue;
			else if(edge[0] == yp && edge[1] == y) continue;
			else newEdges.add(edge);
		}
		
		int[] newp = new int[2], newq = new int[2];
		newp[0] = xp; newp[1] = y;
		newEdges.add(newp);
		
		boolean out = true;
		for(int[] edge : newEdges) {
			int dst = x % 2 == 0 ? x - 1 : x + 1;
			if(edge[0] == dst) {out = false; break;}
			if(edge[1] == dst) {out = true;  break;}
		}
		
		if(out) {
			newq[0] = x; newq[1] = yp;
		}
		else {
			newq[0] = yp; newq[1] = x;
		}
		
		newEdges.add(newq);
		return newEdges;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'j');
		String edgeStr = file.read();
		
		List<int[]> coloredEdges = new ArrayList<int[]>();
		for(String edge : edgeStr.substring(1, edgeStr.length() - 1).split("\\), \\(")) {
			int edgex = Integer.parseInt(edge.split(", ")[0]);
			int edgey = Integer.parseInt(edge.split(", ")[1]);
			int[] coloredEdge = {edgex, edgey};
			coloredEdges.add(coloredEdge);
		}
		
		String breakStr = file.read();
		int x  = Integer.parseInt(breakStr.split(", ")[0]);
		int xp = Integer.parseInt(breakStr.split(", ")[1]);
		int y  = Integer.parseInt(breakStr.split(", ")[2]);
		int yp = Integer.parseInt(breakStr.split(", ")[3]);
		
		List<int[]> breakedEdges = breakGraph(coloredEdges, x, xp, y, yp);
		String pr = "";
		for(int[] edge : breakedEdges) pr += ", " + String.format("(%d, %d)", edge[0], edge[1]);
		file.type(pr.substring(2));
		file.write("");
		
//		List<List<Integer>> genome = Src_BA6I.graphToGenome(breakedEdges);
//		for(List<Integer> chr : genome) file.type(Src_BA6A.permString(chr));
		
		file.close();
	}
}
