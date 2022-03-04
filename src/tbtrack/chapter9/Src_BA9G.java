package tbtrack.chapter9;

import tools.*;
import java.util.*;

public class Src_BA9G {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'g');
		String text = file.read();
		
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
		
		for(int pos : sufarray.subList(0, sufarray.size() - 1)) file.type(pos + ", ");
		file.writeObj(sufarray.get(sufarray.size() - 1));
		file.close();
	}
}
