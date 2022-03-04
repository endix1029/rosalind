package tbtrack.chapter6;

import tools.*;
import java.util.*;

public class Src_BA6A {
	
	// exclusive inversal of permutation; [x, y)
	private static void inverse(List<Integer> perm, int x, int y) {
		List<Integer> subPerm = new ArrayList<Integer>();
		for(int i = x; i < y; i++) subPerm.add(perm.get(i));
		
		for(int i = x; i < y; i++) perm.remove(x);
		for(int sp : subPerm) perm.add(x, -sp);
	}
	
	public static String permString(List<Integer> perm) {
		if(perm.isEmpty()) return null;
		
		String ps = "";
		for(int p : perm) {
			ps += ' ';
			if(p > 0) ps += '+';
			ps += String.valueOf(p);
		}
		
		return "(" + ps.substring(1) + ")";
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'a');
		String permLine = file.read();
		
		List<Integer> perm = new ArrayList<Integer>();
		for(String ele : permLine.substring(1, permLine.length() - 1).split(" ")) {
			if(ele.charAt(0) == '+') perm.add(Integer.parseInt(ele.substring(1)));
			else perm.add(-Integer.parseInt(ele.substring(1)));
		}
		
		for(int p = 1; p <= perm.size(); p++) {
			if(perm.get(p - 1) != p) {
				int pos = perm.indexOf(p), neg = perm.indexOf(-p);
				if(pos > 0) {
					inverse(perm, p - 1, pos + 1);
					file.write(permString(perm));
					inverse(perm, p - 1, p);
					file.write(permString(perm));
				}
				else if(neg > 0) {
					inverse(perm, p - 1, neg + 1);
					file.write(permString(perm));
				}
			}
		}
		
		file.close();
	}
}
