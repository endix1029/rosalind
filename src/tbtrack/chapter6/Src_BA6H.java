package tbtrack.chapter6;

import tools.*;
import java.util.*;

public class Src_BA6H {
	public static List<int[]> coloredEdges(List<List<Integer>> genome){
		List<int[]> edges = new ArrayList<int[]>();
		
		for(List<Integer> chr : genome) {
			List<Integer> cyc = Src_BA6F.chromosomeToCycle(chr);
			for(int i = 1; i < chr.size(); i++) {
				int[] edge = {cyc.get(i * 2 - 1), cyc.get(i * 2)};
				edges.add(edge);
			}
			int[] edge = {cyc.get(cyc.size() - 1), cyc.get(0)};
			edges.add(edge);
		}
		
		return edges;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'h');
		
		String gstr = file.read();
		List<List<Integer>> genome = new ArrayList<List<Integer>>();
		
		for(String cstr : gstr.split("\\)")) {
			List<Integer> chr = new ArrayList<Integer>();
			
			for(String ele : cstr.substring(1).split(" ")) {
				if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
				else chr.add(-Integer.parseInt(ele.substring(1)));
			}
			
			genome.add(chr);
		}
		
		List<int[]> edges = coloredEdges(genome);
		String pr = "";
		for(int[] edge : edges) pr += ", " + String.format("(%d, %d)", edge[0], edge[1]);
		file.type(pr.substring(2));
		file.close();
	}
}
