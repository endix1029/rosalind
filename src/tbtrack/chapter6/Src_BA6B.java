package tbtrack.chapter6;

import tools.*;
import java.util.*;

public class Src_BA6B {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'b');
		String permLine = file.read();
		
		List<Integer> perm = new ArrayList<Integer>();
		for(String ele : permLine.substring(1, permLine.length() - 1).split(" ")) {
			if(ele.charAt(0) == '+') perm.add(Integer.parseInt(ele.substring(1)));
			else perm.add(-Integer.parseInt(ele.substring(1)));
		}
		
		int bp = 0, init = 0;
		for(int p : perm) {
			if(p - init != 1) bp++;
			init = p;
		}
		
		file.writeObj(bp);
		file.close();
	}
}
