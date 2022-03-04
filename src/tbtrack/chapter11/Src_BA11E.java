package tbtrack.chapter11;

import tools.*;
import java.util.*;

class MassSpectrumGraph extends SpectrumGraph {
	public MassSpectrumGraph(int[] mlist) {
		super(mlist, true);
		super.edges = new char[spec.size()][spec.size()];
		
		for(int i = 0; i < spec.size() - 1; i++) {
			for(int j = i + 1; j < spec.size(); j++) {
				super.edges[i][j] = PEP.getAcid(j - i);
			}
		}
	}
	
	public int score(boolean[] pvec) {
		if(spec.size() - 1 != pvec.length) return -1;
		
		int s = 0;
		for(int i = 0; i < pvec.length; i++) {
			if(pvec[i]) s += spec.get(i + 1);
		}
		return s;
	}
	
	public int score(String path) {
		int s = 0, msum = 0;
		for(int i = 0; i < path.length(); i++) {
			msum += PEP.getMass(path.charAt(i));
			s += spec.get(msum);
		}
		return s;
	}
	
	public String maxPath() {
		List<String>  mplist = new ArrayList<String>();
		List<Integer> scores = new ArrayList<Integer>();
		
		for(int i = 0; i < spec.size() - 1; i++) {
			mplist.add(null);
			scores.add(Integer.MIN_VALUE);
		}
		mplist.add("");
		scores.add(0);
		
		for(int i = spec.size() - 2; i >= 0; i--) {
			int maxScore = Integer.MIN_VALUE;
			String maxSubpath = "";
			
			for(int j = i + 1; j < spec.size(); j++) {
				if(mplist.get(j) == null) continue;
				
				if(edges[i][j] != '*') {
					if(maxScore < scores.get(j) + spec.get(i)) {
						maxScore = scores.get(j) + spec.get(i);
						maxSubpath = edges[i][j] + mplist.get(j);
					}
				}
			}
			
			if(maxScore > Integer.MIN_VALUE) {
				mplist.remove(i);
				mplist.add(i, maxSubpath);
				scores.remove(i);
				scores.add(i, maxScore);
			}
		}
		
		return mplist.get(0);
	}
	
	public List<String> paths(String proteome) {
		List<List<String>> plist = new ArrayList<List<String>>();
		
		for(int i = 0; i < spec.size() - 1; i++){
			plist.add(null);
		}
		List<String> tmp = new ArrayList<String>();
		tmp.add("");
		plist.add(tmp);
		
		for(int i = spec.size() - 2; i >= 0; i--) {
			List<String> ps = new ArrayList<String>();
			for(int j = i + 1; j < spec.size(); j++) {
				if(plist.get(j) == null) continue;
				if(edges[i][j] != '*') {
					for(String subpath : plist.get(j)) {
						if(proteome.contains(edges[i][j] + subpath)) {
							ps.add(edges[i][j] + subpath);
						}
						if(edges[i][j] == 'Q') {
							if(proteome.contains('K' + subpath)) {
								ps.add('K' + subpath);
							}
						}
						if(edges[i][j] == 'L') {
							if(proteome.contains('I' + subpath)) {
								ps.add('I' + subpath);
							}
						}
					}
				}
			}
			
			if(ps.size() > 0) {
				plist.remove(i);
				plist.add(i, ps);
			}
		}
		
		return plist.get(0) == null ? new ArrayList<String>() : plist.get(0);
	}
}

public class Src_BA11E {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'e');
		String[] mspl = file.read().split(" ");
		
		int[] mlist = new int[mspl.length + 1];
		mlist[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			mlist[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		MassSpectrumGraph msg = new MassSpectrumGraph(mlist);
		
		file.write(msg.maxPath());
		file.close();
	}
}
