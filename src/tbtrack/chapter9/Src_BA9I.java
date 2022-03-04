package tbtrack.chapter9;

import tools.*;
import java.util.*;

public class Src_BA9I {
	public static String burrowsWheeler(String text) {
		int n = text.length();
		
		List<String> bwMatrix = new ArrayList<String>();
		for(int i = 0; i < n; i++) {
			bwMatrix.add(text);
			text = text.charAt(n - 1) + text.substring(0, n - 1);
		}	
		Collections.sort(bwMatrix);
	
		String bwt = "";
		for(String cyclic : bwMatrix) bwt += cyclic.charAt(n - 1);
		return bwt;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'i');
		String text = file.read();
		
		file.write(burrowsWheeler(text));
		file.close();
	}
}
