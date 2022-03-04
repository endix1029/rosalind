package tbtrack.chapter3;

import tools.FileIO;
import java.util.*;

public class Src_BA3K {
	private static int MAXV = 0, K = 0;
	
	private static boolean nonbranching(int v, int[][] adj) {
		int icnt = 0, ocnt = 0;
		
		for(int i = 0; i <= MAXV; i++) {
			icnt += adj[i][v];
			ocnt += adj[v][i];
		}
		
		return (icnt == 1) & (ocnt == 1);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'k');
		
		List<int[]> pairList = new ArrayList<int[]>();
		List<String> kmer = new ArrayList<String>();
		List<String> contigs = new ArrayList<String>();
		
		String line;
		while((line = file.read()) != null) {
			int[] pair = new int[2];
			
			String head = line.substring(0, line.length() - 1);
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
		K = kmer.get(0).length();
		
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
				for(int w = 0; w <= MAXV; w++) {
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
						String seq = kmer.get(v);
						int iter = w;
						while(true) {
							seq += kmer.get(iter).charAt(K - 1);
							if(!nbranch[iter]) break;
							flags[iter] = true;
							iter = nindex[iter];
						}
						contigs.add(seq);
					}
				}
			}
		}
		
		for(int v = 0; v <= MAXV; v++) {
			if(!flags[v]) {
				String cycle = kmer.get(v);
				int iter = nindex[v];
				flags[v] = true;
				
				while(true) {
					cycle += kmer.get(iter).charAt(K - 1);
					flags[iter] = true;
					if(nindex[iter] == v) break;
					iter = nindex[iter];
				}
				
				contigs.add(cycle);
			}
		}
		
		Collections.sort(contigs);
		for(String contig : contigs) {
			file.type(contig + " ");
		}
		file.write("");
		
		file.close();
	}
}
