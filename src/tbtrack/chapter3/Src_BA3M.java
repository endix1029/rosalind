package tbtrack.chapter3;

import tools.FileIO;
import java.util.*;

public class Src_BA3M {
	private static int MAXV = 0;
	
	private static boolean nonbranching(int v, int[][] adj) {
		int icnt = 0, ocnt = 0;
		
		for(int i = 0; i <= MAXV; i++) {
			icnt += adj[i][v];
			ocnt += adj[v][i];
		}
		
		return (icnt == 1) & (ocnt == 1);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'm');
		
		List<int[]> pairList = new ArrayList<int[]>();
		List<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		
		String line;
		while((line = file.read()) != null) {
			String[] vspl = line.split(" -> ");
			String[] dspl = vspl[1].split(",");
			int src = Integer.parseInt(vspl[0]);
			
			if(src > MAXV) MAXV = src;
			
			for(String dstr : dspl) {
				int[] pair = new int[2];
				pair[0] = src;
				pair[1] = Integer.parseInt(dstr);
				pairList.add(pair);
				
				if(pair[1] > MAXV) MAXV = pair[1];
			}
		}
		
		int[][] adj = new int[MAXV + 1][MAXV + 1];
		for(int[] pair : pairList) {
			adj[pair[0]][pair[1]]++;
		}
		boolean[] flags = new boolean[MAXV + 1];
		
		boolean[] nbranch = new boolean[MAXV + 1];
		int[] nindex = new int[MAXV + 1];
		for(int v = 0; v <= MAXV; v++) nbranch[v] = nonbranching(v, adj);
		for(int v = 0; v <= MAXV; v++) {
			if(nbranch[v]) {
				for(int w = 1; w <= MAXV; w++) {
					if(adj[v][w] > 0) {
						nindex[v] = w;
						break;
					}
				}
			}
			else {
				nindex[v] = -1;
			}
		}
		
		for(int v = 0; v <= MAXV; v++) {
			if(nbranch[v]) continue;
			flags[v] = true;
			
			for(int w = 0; w <= MAXV; w++) {
				if(adj[v][w] > 0) {
					for(int i = 0; i < adj[v][w]; i++) {
						ArrayList<Integer> path = new ArrayList<Integer>();
						path.add(v);
						
						int iter = w;
						while(true) {
							path.add(iter);
							if(!nbranch[iter]) break;
							flags[iter] = true;
							iter = nindex[iter];
						}
						
						paths.add(path);
					}
				}
			}
		}
		
		for(int v = 0; v <= MAXV; v++) {
			if(!flags[v]) {
				ArrayList<Integer> cycle = new ArrayList<Integer>();
				cycle.add(v);
				int iter = nindex[v];
				flags[v] = true;
				
				while(true) {
					cycle.add(iter);
					flags[iter] = true;
					if(nindex[iter] == v) break;
					iter = nindex[iter];
				}
				
				cycle.add(v);
				
				paths.add(cycle);
			}
		}
		
		for(ArrayList<Integer> path : paths) {
			file.typeObj(path.get(0));
			for(int i = 1; i < path.size(); i++) {
				file.type(" -> " + path.get(i));
			}
			file.write("");
		}
		
		file.close();
	}
}
