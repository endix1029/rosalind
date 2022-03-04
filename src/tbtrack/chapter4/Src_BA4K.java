package tbtrack.chapter4;

import tools.*;

import java.io.IOException;
import java.util.*;

public class Src_BA4K {
	public static void main(String[] args) throws IOException {
		FileIO file = new FileIO(4, 'k');
		String prtn = file.read();
		List<Integer> query = new ArrayList<Integer>();
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		for(String v : vspl) query.add(Integer.parseInt(v));
		
		file.writeObj(Functions.linearScore(prtn, query));
		file.close();
	}
}
