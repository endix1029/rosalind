package tbtrack.chapter3;

import tools.FileIO;
import java.util.ArrayList;
import java.util.Collections;

public class Src_BA3E {
	private static int searchHeader(ArrayList<ArrayList<String>> ls, String header) {
		for(int i = 1; i < ls.size(); i++) {
			if(header.equals(ls.get(i).get(0))) return i;
			if(header.compareTo(ls.get(i).get(0)) < 0) return -i;
		}
		
		return -ls.size();
	}
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'e');
		ArrayList<ArrayList<String>> debrujin = new ArrayList<ArrayList<String>>();
		debrujin.add(null);
		
		String line = "";
		while((line = file.read()) != null) {
			String header = line.substring(0, line.length() - 1);
			String footer = line.substring(1);
			int loc = searchHeader(debrujin, header);
			if(loc < 0) {
				ArrayList<String> newDb = new ArrayList<String>();
				newDb.add(header);
				newDb.add(footer);
				debrujin.add(-loc, newDb);
			}
			else {
				debrujin.get(loc).add(footer);
			}
		}
		
		debrujin.remove(0);
		for(ArrayList<String> ls : debrujin) {
			file.type(ls.get(0) + " -> ");
			ls.remove(0);
			Collections.sort(ls);
			
			file.type(ls.get(0));
			ls.remove(0);
			for(String seq : ls) {
				file.type("," + seq);
			}
			
			file.write("");
		}
		
		file.close();
	}
}
