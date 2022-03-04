package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import tools.FileIO;

public class Src_BA9N {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'n');
		String text = file.read() + "$";
		
		List<String>  suffixes = new ArrayList<String>();
		List<Integer> sufarray = new ArrayList<Integer>();
		
		for(int i = 0; i < text.length(); i++) {
			String suffix = text.substring(i);
			
			int loc;
			for(loc = 0; loc < i; loc++) {
				if(suffixes.get(loc).compareTo(suffix) > 0) break;
			}
			
			suffixes.add(loc, suffix);
			sufarray.add(loc, i);
		}
		
		String bwt = Src_BA9I.burrowsWheeler(text);
		BetterBurrowsWheeler bbw = new BetterBurrowsWheeler(bwt);
		
		String line;
		List<String> patterns = new ArrayList<String>();
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			patterns.add(line);
		}
		
		List<Integer> locations = new ArrayList<Integer>();
		for(String pattern : patterns) {
			int[] interval = bbw.bwMatchingInterval(pattern);
			if(interval == null) continue;
			
			for(int loc : interval) {
				int suff = sufarray.get(loc);
				if(!locations.contains(suff)) locations.add(suff);
			}
		}
		
		Collections.sort(locations);
		for(int loc : locations) file.type(loc + " ");
		file.write("");
		
		file.close();
	}
}
