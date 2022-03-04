package tbtrack.chapter4;

import java.util.*;
import tools.FileIO;
import tools.Functions;

public class Src_BA4I {
	private static int M, N;
	
	private static boolean valid(String peptide, List<Integer> parents) {
		String[] spl = peptide.split("-");
		List<Integer> pcopy = new ArrayList<Integer>();
		pcopy.addAll(parents);
		
		for(int i = 0; i < spl.length; i++) {
			for(int j = i; j < spl.length; j++) {
				int sum = 0;
				for(int k = i; k <= j; k++) {
					sum += Integer.parseInt(spl[k]);
				}
				if(!pcopy.contains(sum)) return false;
				pcopy.remove(pcopy.indexOf(sum));
			}
		}
		
		return true;
	}
	
	private static boolean usable(String peptide, String mstr, String[] mstrs) {
		int cnt = 0;
		for(String m : mstrs) {
			if(mstr.equals(m)) cnt++;
		}
		
		for(String pep : peptide.split("-")) {
			if(pep.equals(mstr)) cnt--;
		}
		
		return cnt > 0;
	}
	
	private static int msum(String peptide) {
		int sum = 0;
		for(String pep : peptide.split("-")) {
			sum += Integer.parseInt(pep);
		}
		return sum;
	}
	
	private static List<String> trim(List<String> peptides, List<Integer> parents) throws java.io.IOException {
		if(peptides.size() < N) return peptides;
		
		List<Integer> scores = new ArrayList<Integer>();
		for(String peptide : peptides) {
			scores.add(Functions.linearScore(peptide, parents));
		}
		
		Collections.sort(scores);
		int cutoff = scores.get(scores.size() - N - 1);
		
		List<String> trimmed = new ArrayList<String>();
		for(String peptide : peptides) {
			if(Functions.linearScore(peptide, parents) >= cutoff) {
				trimmed.add(peptide);
			}
		}
		
		return trimmed;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'i');
		M = Integer.parseInt(file.read());
		N = Integer.parseInt(file.read());
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		
		List<Integer> parents = new ArrayList<Integer>();
		for(String v : vspl) parents.add(Integer.parseInt(v));
		Collections.sort(parents);
		int pmass = parents.get(parents.size() - 1);
		
		List<Convolution> clist = new ArrayList<Convolution>();
		for(int i = 1; i < parents.size(); i++) {
			for(int j = 0; j < i; j++) {
				int mass = parents.get(i) - parents.get(j);
				if(mass == 0) continue;
				
				if(Convolution.massIndex(clist, mass) < 0) {
					Convolution conv = new Convolution(mass);
					clist.add(conv);
				}
				else clist.get(Convolution.massIndex(clist, mass)).increment();
			}
		}
		
		List<Convolution> tmp = new ArrayList<Convolution>();
		for(Convolution cnv : clist) {
			if(cnv.getMass() <= 200 && cnv.getMass() >= 57) tmp.add(cnv);
		}
		clist = Convolution.sortList(tmp);
		int cutoff = clist.get(M - 1).getCnt();
		for(int i = clist.size() - 1; i >= M; i--) {
			if(clist.get(i).getCnt() < cutoff) clist.remove(i);
			else break;
		}
		
		List<Integer> mlist = new ArrayList<Integer>();
		for(Convolution cnv : clist) mlist.add(cnv.getMass());
		Collections.sort(mlist);
		Integer[] mvals = mlist.toArray(new Integer[0]);
		
		String leader = "";
		List<String> peptides = new ArrayList<String>();
		List<String> copy = new ArrayList<String>();
		
		for(int parent : parents) {
			for(int mval : mvals) {
				if(parent == mval) {
					peptides.add(String.valueOf(parent));
					break;
				}
			}
		}
		
		String[] mstrs = new String[peptides.size()];
		for(int i = 0; i < peptides.size(); i++) mstrs[i] = peptides.get(i);
		
		while(!peptides.isEmpty()) {
			List<String> expand = new ArrayList<String>();
			for(String pep : peptides) {
				for(String mstr : mstrs) {
					if(usable(pep, mstr, mstrs)) expand.add(pep + "-" + mstr);
				}
			}
			
			copy.clear();
			copy.addAll(expand);
			
			for(String pep : expand) {
				if(!valid(pep, parents)) {
					copy.remove(pep);
				}
				else if(msum(pep) == pmass) {
					if(Functions.linearScore(pep, parents) > Functions.linearScore(leader, parents)) {
						leader = pep;
					}
				}
				else if(msum(pep) > pmass) {
					copy.remove(pep);
				}
			}
			
			peptides.clear();
			for(String ele : copy) {
				if(!peptides.contains(ele)) {
					peptides.add(ele);
				}
			}
			
			peptides = trim(peptides, parents);
		}
		
		file.write(leader);
		file.close();
	}
}
