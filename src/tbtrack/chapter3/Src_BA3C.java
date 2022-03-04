package tbtrack.chapter3;

import tools.FileIO;
import java.util.ArrayList;
import java.util.Collections;

public class Src_BA3C {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'c');
		
		ArrayList<String> comps = new ArrayList<String>();
		String line = "";
		while((line = file.read()) != null) {
			comps.add(line);
		}
		
		Collections.sort(comps);
		for(String comp : comps) {
			for(String ele : comps) {
				if(comp.substring(1).equals(ele.substring(0, comp.length() - 1))) {
					file.write(comp + " -> " + ele);
					break;
				}
			}
		}
		
		file.close();
	}
}
