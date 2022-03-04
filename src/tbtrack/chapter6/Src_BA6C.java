package tbtrack.chapter6;

import java.util.*;
import tools.*;

public class Src_BA6C {
	public static int cycles(List<int[]> edges) {
		int count = 0;
		
		boolean[] visited = new boolean[edges.size()];
		for(int i = 0; i < edges.size(); i++) {
			if(visited[i]) continue;
			
			count++;
			int current = i, next = -1;
			while(true) {
				visited[current] = true;
				boolean flag = false;
				for(int j = 0; j < edges.size(); j++) {
					if(!visited[j]) {
						if(edges.get(j)[0] == edges.get(current)[0]) {
							next = j; flag = true; break;
						}
						if(edges.get(j)[0] == edges.get(current)[1]) {
							next = j; flag = true; break;
						}
						if(edges.get(j)[1] == edges.get(current)[0]) {
							next = j; flag = true; break;
						}
						if(edges.get(j)[1] == edges.get(current)[1]) {
							next = j; flag = true; break;
						}
					}
				}
				if(!flag) break;
				current = next;
			}
		}
		
		return count;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'c');
		String gstr;
		
		List<List<Integer>> genp = new ArrayList<List<Integer>>();
		List<List<Integer>> genq = new ArrayList<List<Integer>>();
		
		gstr = file.read();
		for(String cstr : gstr.split("\\)")) {
			List<Integer> chr = new ArrayList<Integer>();
			
			for(String ele : cstr.substring(1).split(" ")) {
				if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
				else chr.add(-Integer.parseInt(ele.substring(1)));
			}
			
			genp.add(chr);
		}
		gstr = file.read();
		for(String cstr : gstr.split("\\)")) {
			List<Integer> chr = new ArrayList<Integer>();
			
			for(String ele : cstr.substring(1).split(" ")) {
				if(ele.charAt(0) == '+') chr.add(Integer.parseInt(ele.substring(1)));
				else chr.add(-Integer.parseInt(ele.substring(1)));
			}
			
			genq.add(chr);
		}
		
		List<int[]> edgep = Src_BA6H.coloredEdges(genp);
		List<int[]> edgeq = Src_BA6H.coloredEdges(genq);
		
		List<int[]> qq = new ArrayList<int[]>();
		List<int[]> pq = new ArrayList<int[]>();
		for(int[] p : edgep) pq.add(p);
		for(int[] q : edgeq) qq.add(q);
		for(int[] q : edgeq) {
			pq.add(q); qq.add(q);
		}
		
		file.writeObj(cycles(qq) - cycles(pq));
		file.close();
	}
}
