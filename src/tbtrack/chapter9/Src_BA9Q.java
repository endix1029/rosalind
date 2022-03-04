package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;

public class Src_BA9Q {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'q');
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
		
		int k = Integer.parseInt(file.read());
		for(int i = 0; i < sufarray.size(); i++) {
			if(sufarray.get(i) % k == 0) {
				file.write(String.format("%d,%d", i, sufarray.get(i)));
			}
		}
		
		file.close();
	}
}
