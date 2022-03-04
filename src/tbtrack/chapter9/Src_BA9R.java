package tbtrack.chapter9;

import java.util.*;
import tools.*;

public class Src_BA9R {
	private static List<String> EDGES;
	
	private static void processIsland(List<String> island) {
		int minlen = 0;
		for(; minlen <= island.get(0).length(); minlen++) {
			String prefix = island.get(0).substring(0, minlen);
			
			boolean flag = true;
			for(String isle : island) {
				if(!isle.startsWith(prefix)){
					flag = false;
					break;
				}
			}
			if(!flag) break;
		}
		minlen--;
		
		EDGES.add(island.get(0).substring(0, minlen));
		List<String> frags = new ArrayList<String>();
		for(String isle : island) {
			if(isle.length() > minlen) frags.add(isle.substring(minlen));
		}
		
		if(frags.isEmpty()) return;
		islandMaker(frags);
	}
	
	private static void islandMaker(List<String> frags) {
		List<String> island = new ArrayList<String>();
		char prev = frags.get(0).charAt(0);
		
		for(String frag : frags) {
			if(frag.charAt(0) != prev) {
				processIsland(island);
				island = new ArrayList<String>();
				prev = frag.charAt(0);
			}
			
			island.add(frag);
		}
		
		processIsland(island);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'r');
		String text = file.read();
		
		List<String> sufarray = new ArrayList<String>();
		for(String nstr : file.read().split(", ")) {
			sufarray.add(text.substring(Integer.parseInt(nstr)));
		}
		
		int lcps[] = new int[sufarray.size() + 1], i = 0;
		for(String nstr : file.read().split(", ")) {
			lcps[i++] = Integer.parseInt(nstr);
		}
		lcps[i] = -1;
		
		EDGES = new ArrayList<String>();
		for(i = 1; i < lcps.length; i++) {
			String suffix = sufarray.get(i - 1);
			
			int plen = lcps[i] > lcps[i - 1] ? lcps[i] : lcps[i - 1];
			EDGES.add(suffix.substring(plen));
			
			sufarray.remove(i - 1);
			sufarray.add(i - 1, suffix.substring(0, plen));
		}
		
		for(i = sufarray.size() - 1; i >= 0; i--) {
			if(sufarray.get(i).length() == 0) sufarray.remove(i);
		}
		
		islandMaker(sufarray);
		
		Collections.sort(EDGES);
		for(String suffix : EDGES) file.write(suffix);
		file.close();
	}
}
