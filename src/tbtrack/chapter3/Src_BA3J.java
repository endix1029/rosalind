package tbtrack.chapter3;

import tools.FileIO;
import tools.Random;
import java.util.*;

class ReadPair {
	protected String prefix = null, suffix = null;
	
	public ReadPair(String pre, String suf) {
		this.prefix = pre;
		this.suffix = suf;
	}
}

public class Src_BA3J {
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
	
	private static int rp_index(List<ReadPair> rps, ReadPair rp) {
		for(int i = 0; i < rps.size(); i++) {
			if(rps.get(i).prefix.compareTo(rp.prefix) == 0
			&& rps.get(i).suffix.compareTo(rp.suffix) == 0) return i;
		}
		
		return -1;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'j');
		
		List<int[]> pairList = new ArrayList<int[]>();
		List<ReadPair> rps = new ArrayList<ReadPair>();
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		
		int k = Integer.parseInt(vspl[0]), d = Integer.parseInt(vspl[1]);
		
		String line;
		while((line = file.read()) != null) {
			int[] pair = new int[2];
			int index;
			
			String[] spl = line.split("\\|");
			ReadPair head = new ReadPair(spl[0].substring(0, k - 1), spl[1].substring(0, k - 1));
			if((index = rp_index(rps, head)) >= 0) {
				pair[0] = index;
			}
			else {
				rps.add(head);
				pair[0] = MAXV++;
			}
			
			ReadPair tail = new ReadPair(spl[0].substring(1), spl[1].substring(1));
			if(((index = rp_index(rps, tail)) >= 0)) {
				pair[1] = index;
			}
			else {
				rps.add(tail);
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
		
		String preseq = rps.get(cycle.get(0)).prefix, sufseq = rps.get(cycle.get(0)).suffix;
		cycle.remove(0);
		for(int v : cycle) {
			preseq += rps.get(v).prefix.charAt(k - 2);
			sufseq += rps.get(v).suffix.charAt(k - 2);
		}
		
		file.write(preseq + sufseq.substring(sufseq.length() - k - d));
		file.close();
	}
}
