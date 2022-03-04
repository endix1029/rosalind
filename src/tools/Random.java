package tools;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
	// return random integer in range of [min, max)
	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	// return random permutation array from 0 to size-1
	public static List<Integer> randPerm(int size) {
		List<Integer> perm = new ArrayList<Integer>();
		
		for(int i = 0; i < size; i++) perm.add(i);
		Collections.shuffle(perm);
		
		return perm;
	}
}
