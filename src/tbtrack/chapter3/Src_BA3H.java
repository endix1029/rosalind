package tbtrack.chapter3;

import tools.FileIO;
import tools.Random;
import java.util.*;

public class Src_BA3H {
	private static int MAXV = 0;
	
	// existence of unused incoming, outcoming edge for given vertex
	private static boolean nodeIO(int v, int[][] adj) {
		boolean iflag = false, oflag = false;
		
		for(int i = 0; i <= MAXV; i++) {
			iflag |= (adj[v][i] > 0);
			oflag |= (adj[i][v] > 0);
		}
		
		return iflag & oflag;
	}
	
	// reconstruct given cycle with new starting location
	private static List<Integer> recycle(List<Integer> cycle, int start){
		List<Integer> newCycle = new ArrayList<Integer>();
		
		if(cycle.size() <= 2) {
			newCycle.addAll(cycle);
			return newCycle;
		}
		
		for(int i = start; i < cycle.size() - 1; i++) {
			newCycle.add(cycle.get(i));
		}
		for(int i = 0; i <= start; i++) {
			newCycle.add(cycle.get(i));
		}
		
		return newCycle;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO();
		
		List<int[]> pairList = new ArrayList<int[]>();
		List<String> kmer = new ArrayList<String>();
		int k = Integer.parseInt(file.read());
		
		String line;
		while((line = file.read()).length() > 0) {
			int[] pair = new int[2];
			
			String head = line.substring(0, k - 1);
			if(kmer.contains(head)) {
				pair[0] = kmer.indexOf(head);
			}
			else {
				kmer.add(head);
				pair[0] = MAXV++;
			}
			
			String tail = line.substring(1);
			if(kmer.contains(tail)) {
				pair[1] = kmer.indexOf(tail);
			}
			else {
				kmer.add(tail);
				pair[1] = MAXV++;
			}
			
			pairList.add(pair);
		}
		
		MAXV--;
		int[][] adj = new int[MAXV + 1][MAXV + 1];
		int edgecount = 0;
		for(int[] pair : pairList) {
			adj[pair[0]][pair[1]]++;
			edgecount++;
		}
		
		int uStart = -1, uEnd = -1;
		for(int i = 0; i <= MAXV; i++) {
			int icnt = 0, ocnt = 0;
			
			for(int j = 0; j <= MAXV; j++) {
				ocnt += adj[i][j];
				icnt += adj[j][i];
			}
			
			if(icnt < ocnt) uStart = i;
			if(icnt > ocnt) uEnd = i;
		}
		
		if(uEnd > 0) {
			adj[uEnd][uStart] = 1;
			edgecount++;
		}
		
		List<Integer> cycle = new ArrayList<Integer>();
		cycle.add(Random.randInt(0, MAXV + 1));
		
		while(edgecount > 0) {
			int loc;
			for(loc = 0; !nodeIO(cycle.get(loc), adj); loc++);
			List<Integer> newCycle = recycle(cycle, loc);
			
			int start = newCycle.get(0);
			int curr = newCycle.get(newCycle.size() - 1);
			int next;
			while(true) {
				List<Integer> rperm = Random.randPerm(MAXV + 1);
				for(next = 0; adj[curr][rperm.get(next)] == 0; next++);
				next = rperm.get(next);
				
				adj[curr][next]--;
				newCycle.add(next);
				edgecount--;
				
				if(next == start) break;
				curr = next;
			}
			
			cycle.clear();
			cycle.addAll(newCycle);
		}
		
		if(uEnd > 0) {
			int loc;
			for(loc = 0; cycle.get(loc) != uEnd || cycle.get(loc + 1) != uStart; loc++);
			List<Integer> edit = recycle(cycle, loc);
			cycle.clear();
			cycle.addAll(edit);
			cycle.remove(0);
		}
		
		file.type(kmer.get(cycle.get(0)));
		cycle.remove(0);
		for(int v : cycle) {
			file.typeObj(kmer.get(v).charAt(k - 2));
		}
		file.write("");
		
		file.close();
	}
}
