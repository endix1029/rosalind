package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.FileIO;

public class Src_BA9H {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'h');
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
		
		List<String> queries = new ArrayList<String>();
		String line;
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			queries.add(line);
		}
		
		List<Integer> locations = new ArrayList<Integer>();
		for(String query : queries) {
			int min = 0, max = text.length();
			
			while(min < max) {
				int mid = (min + max) / 2;
				if(query.compareTo(text.substring(sufarray.get(mid))) > 0) min = mid + 1;
				else max = mid;
			}
//			int sindex = min;
			
			if(min == text.length()) continue;
			for(int i = min; text.substring(sufarray.get(i)).startsWith(query); i++) {
				if(!locations.contains(sufarray.get(i))) locations.add(sufarray.get(i));
			}
/*			
			max = text.length();
			while(min < max) {
				int mid = (min + max) / 2;
				if(query.compareTo(text.substring(sufarray.get(mid))) < 0) max = mid;
				else min = mid + 1;
			}
			int eindex = max;
			
			for(int i = sindex; i <= eindex; i++) {
				if(!locations.contains(sufarray.get(i))) locations.add(sufarray.get(i));
			} */
		}
		
		Collections.sort(locations);
		for(int loc : locations) file.type(loc + " ");
		file.write("");
		file.close();
	}
}
