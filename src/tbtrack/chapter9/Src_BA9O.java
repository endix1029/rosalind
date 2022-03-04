package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.FileIO;

public class Src_BA9O {
	private static int D;
	private static String TEXT;
	private static BetterBurrowsWheeler BBW;
	private static List<Integer> SUFARRAY;
	
	private static boolean extendSeed(String pattern, int init) {
		int countdown = D;
		for(int i = init; i < init + pattern.length(); i++) {
			if(TEXT.charAt(i) != pattern.charAt(i - init)) countdown--;
			if(countdown < 0) return false;
		}
		return true;
	}
	
	public static List<Integer> approxOccur(String pattern) {
		List<Integer> occurrence = new ArrayList<Integer>();
		
		String[] seeds = new String[D + 1];
		int k = pattern.length() / (D + 1);
		
		for(int i = 0; i < D; i++) {
			seeds[i] = pattern.substring(k * i, k * i + k);
		}
		seeds[D] = pattern.substring(k * D);
		
		for(int nseed = 0; nseed <= D; nseed++) {
			int[] interval = BBW.bwMatchingInterval(seeds[nseed]);
			if(interval != null) {
				for(int loc : interval) {
					int init = SUFARRAY.get(loc) - k * nseed;
					if(init < 0 || init + pattern.length() >= BBW.textLen) continue;
					
					if(extendSeed(pattern, init)) {
						if(!occurrence.contains(init)) occurrence.add(init);
					}
				}
			}
		}
		return occurrence;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'o');
		TEXT = file.read() + "$";
		
		List<String>  suffixes = new ArrayList<String>();
		SUFARRAY = new ArrayList<Integer>();
		
		for(int i = 0; i < TEXT.length(); i++) {
			String suffix = TEXT.substring(i);
			
			int loc;
			for(loc = 0; loc < i; loc++) {
				if(suffixes.get(loc).compareTo(suffix) > 0) break;
			}
			
			suffixes.add(loc, suffix);
			SUFARRAY.add(loc, i);
		}
		
		String bwt = Src_BA9I.burrowsWheeler(TEXT);
		BBW = new BetterBurrowsWheeler(bwt);
		
		String[] patterns = file.read().split(" ");
		D = Integer.parseInt(file.read());
		
		List<Integer> positions = new ArrayList<Integer>();
		for(String pattern : patterns) {
			positions.addAll(approxOccur(pattern));
		}
		
		Collections.sort(positions);
		for(int pos : positions) file.type(pos + " ");
		file.write("");
		
		file.close();
	}
}
