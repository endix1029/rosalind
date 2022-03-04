package tbtrack.chapter6;

import tools.*;
import java.util.*;

public class Src_BA6G {
	public static List<Integer> cycleToChromosome(List<Integer> cyc){
		List<Integer> chr = new ArrayList<Integer>();
		
		for(int i = 0; i < cyc.size(); i+= 2) {
			if(cyc.get(i) < cyc.get(i + 1)) chr.add(i / 2 + 1);
			else chr.add(-(i / 2 + 1));
		}
		
		return chr;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'g');
		String line = file.read();
		
		List<Integer> cyc = new ArrayList<Integer>();
		for(String ele : line.substring(1, line.length() - 1).split(" ")) {
			cyc.add(Integer.parseInt(ele));
		}
		
		List<Integer> chr = cycleToChromosome(cyc);
		file.write(Src_BA6A.permString(chr));
		file.close();
	}
}
