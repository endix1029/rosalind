package tbtrack.chapter3;

import tools.*;
import java.util.ArrayList;
import java.util.Collections;

public class Src_BA3A {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'a');
		
		int k = Integer.parseInt(file.read());
		String text = file.read();
		
		ArrayList<String> comps = new ArrayList<String>();
		for(int i = 0; i <= text.length() - k; i++) {
			String frag = text.substring(i, i + k);
			if(!comps.contains(frag)) comps.add(frag);
		}
		
		Collections.sort(comps);
		for(String comp : comps) {
			file.write(comp);
		}
		
		file.close();
	}
}
