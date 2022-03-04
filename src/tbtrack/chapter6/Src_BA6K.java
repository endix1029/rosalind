package tbtrack.chapter6;

import java.util.*;
import tools.*;

public class Src_BA6K {
	public static List<List<Integer>> breakGenome(List<List<Integer>> genome, int x, int xp, int y, int yp){
		List<int[]> coloredEdges = Src_BA6H.coloredEdges(genome);
		List<int[]> breakedEdges = Src_BA6J.breakGraph(coloredEdges, x, xp, y, yp);
		return Src_BA6I.graphToGenome(breakedEdges);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'k');
		
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
		
		String breakStr = file.read();
		int x  = Integer.parseInt(breakStr.split(", ")[0]);
		int xp = Integer.parseInt(breakStr.split(", ")[1]);
		int y  = Integer.parseInt(breakStr.split(", ")[2]);
		int yp = Integer.parseInt(breakStr.split(", ")[3]);
		
		List<List<Integer>> breakedGenome = breakGenome(genome, x, xp, y, yp);
		for(List<Integer> chr : breakedGenome) file.type(Src_BA6A.permString(chr));
		file.close();
	}
}
