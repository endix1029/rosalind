package tbtrack.chapter5;

import tools.*;
import java.util.*;

public class Src_BA5N {
	private static int MAXV;
	
	private static boolean inputExist(boolean[][] adj, int v) {
		for(int i = 0; i <= MAXV; i++) {
			if(adj[i][v]) return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'n');
		
		List<Integer> srcList = new ArrayList<Integer>();
		List<Integer> dstList = new ArrayList<Integer>();
		
		String line;
		while((line = file.read()) != null) {
			int src = Integer.parseInt(line.split(" -> ")[0]);
			
			String dstStr = line.split(" -> ")[1];
			List<Integer> dsts = new ArrayList<Integer>();
			for(String dst : dstStr.split(",")) dsts.add(Integer.parseInt(dst));
			
			for(int dst : dsts) {
				srcList.add(src);
				if(src > MAXV) MAXV = src;
				dstList.add(dst);
				if(dst > MAXV) MAXV = dst;
			}
		}
		
		boolean[][] adj = new boolean[MAXV + 1][MAXV + 1];
		boolean[] valid = new boolean[MAXV + 1];
		for(int i = 0; i < srcList.size(); i++) {
			adj[srcList.get(i)][dstList.get(i)] = true;
			valid[srcList.get(i)] = true;
			valid[dstList.get(i)] = true;
		}
		
		List<Integer> candidates = new ArrayList<Integer>();
		for(int v = 0; v <= MAXV; v++) {
			if(valid[v] & (!inputExist(adj, v))) candidates.add(v);
		}
		
		List<Integer> sorted = new ArrayList<Integer>();
		while(!candidates.isEmpty()) {
			int pop = candidates.remove(0);
			sorted.add(pop);
			
			for(int v = 0; v <= MAXV; v++) {
				if(adj[pop][v]) {
					adj[pop][v] = false;
					if(!inputExist(adj, v)) candidates.add(v);
				}
			}
		}
		
		file.typeObj(sorted.get(0));
		for(int i = 1; i < sorted.size(); i++) {
			file.type(", " + sorted.get(i));
		}
		
		file.write("");
		file.close();
	}
}