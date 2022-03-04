package tbtrack.chapter4;

import tools.*;
import java.util.*;

public class Src_BA4C {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'c');
		MassTable mt = new MassTable(true);
		
		List<Integer> spectrum = new ArrayList<Integer>();
		spectrum.add(0);
		
		String prtn = file.read();
		
		int spec = 0;
		for(int loc = 0; loc < prtn.length(); loc++) {
			spec += mt.getSimpleMass(prtn.charAt(loc));
		}
		spectrum.add(spec);
		
		for(int len = 1; len < prtn.length(); len++) {
			for(int loc = 0; loc < prtn.length(); loc++) {
				String sub;
				if(loc + len > prtn.length()) {
					sub = prtn.substring(loc) + prtn.substring(0, loc + len - prtn.length());
				}
				else {
					sub = prtn.substring(loc, loc + len);
				}
				
				spec = 0;
				for(int i = 0; i < len; i++) {
					spec += mt.getSimpleMass(sub.charAt(i));
				}
				spectrum.add(spec);
			}
		}
		
		Collections.sort(spectrum);
		for(int mass : spectrum) {
			file.type(mass + " ");
		}
		
		file.close();
	}
}
