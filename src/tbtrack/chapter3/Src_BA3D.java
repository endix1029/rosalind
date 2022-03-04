package tbtrack.chapter3;

import tools.FileIO;
import java.util.ArrayList;
import java.util.Collections;

public class Src_BA3D {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'd');
		
		int k = Integer.parseInt(file.read());
		String text = file.read();
		
		ArrayList<ArrayList<String>> debrujin = new ArrayList<ArrayList<String>>();
		for(int i = 0; i <= text.length() - k; i++) {
			String seq = text.substring(i, i + k);
			
			int loc = -1;
			for(int j = 0; j < debrujin.size(); j++) {
				ArrayList<String> path = debrujin.get(j);
				
				if(path == null) continue;
				if(path.get(0).equals(seq.substring(0, k - 1))) {
					loc = j;
					break;
				}
			}
			
			if(loc < 0) {
				ArrayList<String> newPath = new ArrayList<String>();
				newPath.add(seq.substring(0, k - 1));
				newPath.add(seq.substring(1));
				debrujin.add(newPath);
			}
			else {
				debrujin.get(loc).add(seq.substring(1));
			}
		}
		
		ArrayList<ArrayList<String>> deb_sort = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> path : debrujin) {
			int loc;
			for(loc = 0; loc < deb_sort.size(); loc++) {
				if(path.get(0).compareTo(deb_sort.get(loc).get(0)) < 0) break;
			}
			
			if(loc == deb_sort.size()) deb_sort.add(path);
			else deb_sort.add(loc, path);
		}
		
		for(ArrayList<String> path : deb_sort) {
			file.type(path.get(0) + " -> ");
			path.remove(0);
			Collections.sort(path);
			
			file.type(path.get(0));
			for(int i = 1; i < path.size(); i++) {
				file.type("," + path.get(i));
			}
			file.write("");
		}
		
		file.close();
	}
}
