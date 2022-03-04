package tbtrack.chapter6;

import java.util.*;
import tools.*;

public class Src_BA6F {
	public static String listString(List<Integer> list) {
		String ps = "";
		for(int p : list) ps += ' ' + String.valueOf(p);
		return "(" + ps.substring(1) + ")";
	}
	
	public static List<Integer> chromosomeToCycle(List<Integer> chr) {
		List<Integer> cyc = new ArrayList<Integer>();
		for(int base : chr) {
			if(base > 0) {
				cyc.add(base * 2 - 1);
				cyc.add(base * 2);
			}
			else {
				cyc.add(base * -2);
				cyc.add(base * -2 - 1);
			}
		}
		
		return cyc;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'f');
		String line = file.read();
		
		List<Integer> chr = new ArrayList<Integer>();
		for(String ele : line.substring(1, line.length() - 1).split(" ")) {
			if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
			else chr.add(-Integer.parseInt(ele.substring(1)));
		}
		
		List<Integer> cyc = chromosomeToCycle(chr);		
		file.write(listString(cyc));
		file.close();
	}
}
