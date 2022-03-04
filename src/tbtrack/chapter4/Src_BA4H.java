package tbtrack.chapter4;

import tools.*;
import java.util.*;

class Convolution {
	private int mass, cnt;
	public Convolution(int mass) {
		this.mass = mass;
		this.cnt = 1;
	}
	
	public void increment() {cnt++;}
	public int getMass() {return this.mass;}
	public int getCnt() {return this.cnt;}
	
	public static int compareCnt(Convolution cx, Convolution cy) {
		return cx.getCnt() - cy.getCnt();
	}
	
	public static int massIndex(List<Convolution> clist, int mass) {
		for(int i = 0; i < clist.size(); i++) {
			if(clist.get(i).getMass() == mass) return i;
		}
		
		return -1;
	}
	
	public static List<Convolution> sortList(List<Convolution> clist){
		List<Convolution> slist = new ArrayList<Convolution>();
		
		int n = clist.size();
		while(n-- > 0) {
			int max = -1, maxi = -1;
			
			for(int i = 0; i <= n; i++) {
				if(clist.get(i).getCnt() > max) {
					max = clist.get(i).getCnt();
					maxi = i;
				}
			}
			
			slist.add(clist.get(maxi));
			clist.remove(maxi);
		}
		
		return slist;
	}
}

public class Src_BA4H {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO();
		
		List<Integer> spectrum = new ArrayList<Integer>();
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		for(String v : vspl) spectrum.add(Integer.parseInt(v));
		Collections.sort(spectrum);
		
		List<Convolution> clist = new ArrayList<Convolution>();
		for(int i = 1; i < spectrum.size(); i++) {
			for(int j = 0; j < i; j++) {
				int mass = spectrum.get(i) - spectrum.get(j);
				if(mass == 0) continue;
				
				if(Convolution.massIndex(clist, mass) < 0) {
					Convolution conv = new Convolution(mass);
					clist.add(conv);
				}
				else clist.get(Convolution.massIndex(clist, mass)).increment();
			}
		}
		
		clist = Convolution.sortList(clist);
		for(Convolution cv : clist) {
			for(int i = 0; i < cv.getCnt(); i++) {
				file.type(cv.getMass() + " ");
			}
		}
		file.write("");
		file.close();
	}
}
