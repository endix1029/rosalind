package tbtrack.chapter4;

import tools.*;
import java.util.*;

public class Src_BA4M {
	private static int MAX, DEPTH = 0;
	
	private static List<Integer> dcopy(List<Integer> list){
		List<Integer> copy = new ArrayList<Integer>();
		for(int ele : list) copy.add(ele);
		return copy;
	}
	
	private static boolean able(List<Integer> pikes, List<Integer> status, int pike) {
		List<Integer> pikes_copy = dcopy(pikes);
		
		for(int stat : status) {
			int index = pikes_copy.indexOf(Math.abs(stat - pike));
			if(index < 0) return false;
			else pikes_copy.remove(index);
		}
		
		return true;
	}
	
	private static void addStatus(List<Integer> pikes, List<Integer> status, int pike) {
		for(int stat : status) {
			int index = pikes.indexOf(Math.abs(stat - pike));
			pikes.remove(index);
		}
		status.add(pike);
		Collections.sort(status);
	}
	
	private static List<Integer> turnpike(List<Integer> pikes, List<Integer> status){
		if(status.size() == DEPTH) return status;
		int maxpike = pikes.get(pikes.size() - 1);
		List<Integer> turn;
		
		boolean left = able(pikes, status, MAX - maxpike), right = able(pikes, status, maxpike);
		if(!(left | right)) return null;
		
		List<Integer> pikes_lcopy = dcopy(pikes), status_lcopy = dcopy(status);
		List<Integer> pikes_rcopy = dcopy(pikes), status_rcopy = dcopy(status);
		if(left) {
			addStatus(pikes_lcopy, status_lcopy, MAX - maxpike);
			turn = turnpike(pikes_lcopy, status_lcopy);
			if(turn != null) return turn;
		}
		if(right) {
			addStatus(pikes_rcopy, status_rcopy, maxpike);
			turn = turnpike(pikes_rcopy, status_rcopy);
			if(turn != null) return turn;
		}
		
		return null;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'm');
		List<Integer> pikes_init = new ArrayList<Integer>();
		for(String v : file.read().split(" ")) pikes_init.add(Integer.parseInt(v));
		
		int iter = 0;
		for(; pikes_init.get(iter) <= 0; iter++) {
			if(pikes_init.get(iter) == 0) DEPTH++;
		}
		
		List<Integer> pikes = new ArrayList<Integer>();
		for(; iter < pikes_init.size(); iter++) pikes.add(pikes_init.get(iter));
		
		List<Integer> status = new ArrayList<Integer>();
		status.add(0);
		MAX = pikes.get(pikes.size() - 1);
		addStatus(pikes, status, MAX);
		addStatus(pikes, status, pikes.get(pikes.size() - 1));
		
		status = turnpike(pikes, status);
		for(int pike : status) file.type(pike + " ");
		file.write("");
		file.close();
	}
}
