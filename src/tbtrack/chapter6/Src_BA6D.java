package tbtrack.chapter6;

import java.util.*;
import tools.*;

public class Src_BA6D {
	// returns {x, xp, y, yp, locp, locq}
	private static int[] nonTrivialEdges(List<int[]> blueEdges, List<int[]> redEdges){
		int[] edgeSet = new int[6];
		for(int[] blue : blueEdges) {
			int locp = -1, locq = -1;
			int dstp = -1, dstq = -1;
			for(int i = 0; i < redEdges.size(); i++) {
				int[] red = redEdges.get(i);
				
				if(blue[0] == red[0]) {
					locp = i; dstp = red[1];
				}
				else if(blue[0] == red[1]) {
					locp = i; dstp = red[0];
				}
				
				if(blue[1] == red[0]) {
					locq = i; dstq = red[1];
				}
				else if(blue[1] == red[1]) {
					locq = i; dstq = red[0];
				}
			}
			
			if(locp == locq) continue;
			edgeSet[0] = dstp;
			edgeSet[1] = blue[0];
			edgeSet[2] = blue[1];
			edgeSet[3] = dstq;
			edgeSet[4] = locp;
			edgeSet[5] = locq;
			return edgeSet;
		}
		
		return null;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'd');
		String gstr;
		
		List<List<Integer>> genp = new ArrayList<List<Integer>>();
		List<List<Integer>> genq = new ArrayList<List<Integer>>();
		
		gstr = file.read();
		for(String cstr : gstr.split("\\)")) {
			List<Integer> chr = new ArrayList<Integer>();
			
			for(String ele : cstr.substring(1).split(" ")) {
				if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
				else chr.add(-Integer.parseInt(ele.substring(1)));
			}
			
			genp.add(chr);
		}
		gstr = file.read();
		for(String cstr : gstr.split("\\)")) {
			List<Integer> chr = new ArrayList<Integer>();
			
			for(String ele : cstr.substring(1).split(" ")) {
				if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
				else chr.add(-Integer.parseInt(ele.substring(1)));
			}
			
			genq.add(chr);
		}
		
		for(List<Integer> chr : genp) file.type(Src_BA6A.permString(chr));
		file.write("");
		
		List<int[]> redEdges = Src_BA6H.coloredEdges(genp);
		List<int[]> blueEdges = Src_BA6H.coloredEdges(genq);
		int[] edgeSet;
		while((edgeSet = nonTrivialEdges(blueEdges, redEdges)) != null) {
			if(edgeSet[4] > edgeSet[5]) {
				redEdges.remove(edgeSet[4]);
				redEdges.remove(edgeSet[5]);
			}
			else {
				redEdges.remove(edgeSet[5]);
				redEdges.remove(edgeSet[4]);
			}
			
			int[] newp = {edgeSet[0], edgeSet[3]};
			int[] newq = {edgeSet[1], edgeSet[2]};
			redEdges.add(newp);
			redEdges.add(newq);
			
			List<List<Integer>> breakedGenome = Src_BA6K.breakGenome(genp, edgeSet[0], edgeSet[1], edgeSet[2], edgeSet[3]);
			for(List<Integer> chr : breakedGenome) file.type(Src_BA6A.permString(chr));
			//for(List<Integer> chr : breakedGenome) System.out.print(Src_BA6A.permString(chr));
			file.write("");
			genp = breakedGenome;
		}
		
		file.close();
	}
}
