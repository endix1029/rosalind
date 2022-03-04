package tbtrack.chapter3;

import tools.FileIO;
import tools.Random;
import java.util.*;

public class Src_BA3G {
	private static int MAXV = 0;
	
	// existence of unused incoming, outcoming edge for given vertex
	private static boolean nodeIO(int v, boolean[][] adj) {
		boolean iflag = false, oflag = false;
		
		for(int i = 0; i <= MAXV; i++) {
			iflag |= adj[v][i];
			oflag |= adj[i][v];
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
		FileIO file = new FileIO(3, 'g');
		
		ArrayList<int[]> parseList = new ArrayList<int[]>();
		
		String line;
		while((line = file.read()) != null) {
			String[] aspl = line.split(" -> ");
			String[] cspl = aspl[1].split(",");
			
			int[] vars = new int[cspl.length + 1];
			int iter = 0;
			vars[iter++] = Integer.parseInt(aspl[0]);
			for(String dst : cspl) {
				vars[iter++] = Integer.parseInt(dst);
			}
			
			for(int v : vars) {
				if(v > MAXV) MAXV = v;
			}
			parseList.add(vars);
		}
		
		int edgecount = 0;
		boolean[][] adj = new boolean[MAXV + 1][MAXV + 1];
		for(int[] parse : parseList) {
			int src = parse[0];
			for(int i = 1; i < parse.length; i++) {
				adj[src][parse[i]] = true;
				edgecount++;
			}
		}
		
		int uStart = -1, uEnd = -1;
		for(int i = 0; i <= MAXV; i++) {
			int icnt = 0, ocnt = 0;
			
			for(int j = 0; j <= MAXV; j++) {
				if(adj[i][j]) ocnt++;
				if(adj[j][i]) icnt++;
			}
			
			if(icnt < ocnt) uStart = i;
			if(icnt > ocnt) uEnd = i;
		}
		
		List<Integer> cycle = new ArrayList<Integer>();
		if(uEnd < 0) cycle.add(Random.randInt(0, MAXV + 1));
		else{
			cycle.add(uEnd);
			cycle.add(uStart);
		}
		
		while(edgecount > 0) {
			int loc;
			for(loc = 0; !nodeIO(cycle.get(loc), adj); loc++);
			List<Integer> newCycle = recycle(cycle, loc);
			
			int start = newCycle.get(0);
			int curr = newCycle.get(newCycle.size() - 1);
			int next;
			while(true) {
				List<Integer> rperm = Random.randPerm(MAXV + 1);
				for(next = 0; !adj[curr][rperm.get(next)]; next++);
				next = rperm.get(next);
				
				adj[curr][next] = false;
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
		
		file.typeObj(cycle.get(0));
		cycle.remove(0);
		for(int v : cycle) {
			file.type("->" + v);
		}
		file.write("");
		
		file.close();
	}
}
