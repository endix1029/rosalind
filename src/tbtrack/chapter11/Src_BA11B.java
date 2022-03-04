package tbtrack.chapter11;

import tools.*;
import java.util.*;

class SpectrumGraph {
	protected static final Peptide PEP = new Peptide();
	
	public List<Integer> spec;
	protected char[][] edges;
	
	public SpectrumGraph(int[] mlist) {
		this.spec = new ArrayList<Integer>();		
		for(int m : mlist) this.spec.add(m);
		
		this.edges = new char[spec.size()][spec.size()];
		for(int i = 0; i < spec.size() - 1; i++) {
			for(int j = i + 1; j < spec.size(); j++) {
				edges[i][j] = PEP.getAcid(spec.get(j) - spec.get(i));
			}
		}
	}
	
	public SpectrumGraph(int[] mlist, boolean exflag) {
		this.spec = new ArrayList<Integer>();		
		for(int m : mlist) this.spec.add(m);
		this.edges = new char[spec.size()][spec.size()];
	}
	
	private List<List<String>> memoList = new ArrayList<List<String>>();
	// Recursive
	public List<String> paths(int loc) {
		if(loc == 0) {
			for(int i = 0; i < spec.size() - 1; i++) {
				memoList.add(null);
			}
			List<String> last = new ArrayList<String>();
			last.add("");
			memoList.add(last);
		}
		
		if(memoList.get(loc) != null) {
			return memoList.get(loc);
		}
		
		List<String> plist = new ArrayList<String>();
		for(int i = loc + 1; i < spec.size(); i++) {
			if(edges[loc][i] != '*') {
				for(String path : paths(i)) {
					plist.add(edges[loc][i] + path);
				}
			}
		}
		
		memoList.remove(loc);
		memoList.add(loc, plist);
		System.out.println("location " + loc + " resolved.");
		return plist;
	}
}

public class Src_BA11B {
	public static final Peptide PEP = new Peptide();
	
	public static List<Integer> idealSpectrum(String peptide){
		List<Integer> spec = new ArrayList<Integer>();
		spec.add(0);
		
		for(int i = 1; i < peptide.length(); i++) {
			spec.add(PEP.peptideMass(peptide.substring(0, i)));
			spec.add(PEP.peptideMass(peptide.substring(i)));
		}
		spec.add(PEP.peptideMass(peptide));
		
		Collections.sort(spec);
		return spec;
	}
	
	private static boolean equalSpectrum(List<Integer> specx, List<Integer> specy) {
		if(specx.size() != specy.size()) return false;
		for(int i = 0; i < specx.size(); i++) {
			if(specx.get(i) - specy.get(i) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'b');
		String[] mspl = file.read().split(" ");
		
		int[] mlist = new int[mspl.length + 1];
		mlist[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			mlist[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		SpectrumGraph sgraph = new SpectrumGraph(mlist);
		for(String path : sgraph.paths(0)) {
			if(equalSpectrum(sgraph.spec, idealSpectrum(path))) {
				file.write(path);
				break;
			}
		}
		
		file.close();
	}
}
