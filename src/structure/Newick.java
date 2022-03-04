package structure;

import java.util.*;

public class Newick {
	public List<List<String>> taxa;
	public boolean[][] imap;
	private int nTaxon = 0;
	
	private boolean alpha(char ch) {
		return ch != '(' && ch != ')' && ch != ',' && ch != ';';
	}
	
	private void parse(String nwk) {
		// count internal node
		int ni = 0;
		for(int i = 0; i < nwk.length(); i++) {
			if(nwk.charAt(i) == '(') ni++;
		}
		this.imap = new boolean[ni][ni];
		
		Stack<Integer> nstack = new Stack<>();
		nstack.push(-1);
		
		int ci = 0, pc = -1, ii = 0;
		while(nwk.charAt(ci) != ';') {
			if(nwk.charAt(ci) == '(') { // internal node start
				if(pc >= 0) {
					imap[pc][ii] = true;
					imap[ii][pc] = true;
				}
				
				pc = ii++;
				taxa.add(new ArrayList<String>());
				nstack.push(pc);
				
				ci++;
			}
			else if(nwk.charAt(ci) == ')') { // internal node end
				nstack.pop();
				pc = nstack.peek();
				
				ci++;
			}
			else { // taxon
				String taxon = "";
				while(alpha(nwk.charAt(ci))) {
					taxon += nwk.charAt(ci++);
				}
				taxa.get(pc).add(taxon);
				nTaxon++;
			}
			
			while(nwk.charAt(ci) == ',') ci++;
		}
	}
	
	// create empty newick tree
	public Newick() {}
	
	// create newick tree with newick string
	public Newick(String nwk) {
		this.taxa = new ArrayList<List<String>>();
		this.parse(nwk);
	}
	
	// create newick tree consist of three taxa
	public Newick(String tx, String ty, String tz) {
		this.taxa = new ArrayList<List<String>>();
		this.imap = new boolean[1][1];
		
		List<String> tlist = new ArrayList<String>();
		tlist.add(tx);
		tlist.add(ty);
		tlist.add(tz);
		this.taxa.add(tlist);
		
		this.nTaxon = 3;
	}
	
	/*
	 * add new taxa by specific edge marked with integer
	 * edge enumeration:
	 * 		[0 ... nTaxon) 		: trivial edges designated by taxon
	 * 		[nTaxon ... nEdge)	: edges between internal nodes in ascending order of i-j
	 * 
	 * 		* nEdge = nTaxon * 2 - 3
	 */
	public void addEdge(String taxon, int edge) {
		if(edge < nTaxon) { // trivial edge
			// locate edge (#internal, #taxon)
			int nint = -1, ntax = -1, iter = 0;
			for(nint = 0; nint < taxa.size(); nint++) {
				boolean flag = false;
				
				List<String> tlist = taxa.get(nint);
				for(ntax = 0; ntax < tlist.size(); ntax++) {
					if(iter == edge) { // match
						flag = true;
						break;
					}
					iter++;
				}
				
				if(flag) break;
			}
			
			// process edge addition
			taxa.add(new ArrayList<String>());
			taxa.get(taxa.size() - 1).add(taxa.get(nint).get(ntax));
			taxa.get(taxa.size() - 1).add(taxon);
			taxa.get(nint).remove(ntax);
			
			boolean[][] nimap = new boolean[imap.length + 1][imap.length + 1];
			for(int i = 0; i < imap.length; i++) {
				for(int j = 0; j < imap.length; j++) {
					nimap[i][j] = imap[i][j];
				}
			}
			nimap[nint][imap.length] = true;
			nimap[imap.length][nint] = true;
			imap = nimap;
		}
		else { // internal edge
			// locate edge (#int_1, #int_2)
			int nint = -1, mint = -1, iter = nTaxon;
			for(nint = 0; nint < imap.length - 1; nint++) {
				boolean flag = false;
				for(mint = nint + 1; mint < imap.length; mint++) {
					if(imap[nint][mint]) {
						if(iter == edge) {
							flag = true;
							break;
						}
						iter++;
					}
				}
				if(flag) break;
			}
			
			// process edge addition
			taxa.add(new ArrayList<String>());
			taxa.get(taxa.size() - 1).add(taxon);
			
			boolean[][] nimap = new boolean[imap.length + 1][imap.length + 1];
			for(int i = 0; i < imap.length; i++) {
				for(int j = 0; j < imap.length; j++) {
					nimap[i][j] = imap[i][j];
				}
			}
			nimap[nint][mint] = false;
			nimap[mint][nint] = false;
			nimap[nint][imap.length] = true;
			nimap[imap.length][nint] = true;
			nimap[mint][imap.length] = true;
			nimap[imap.length][mint] = true;
			
			imap = nimap;
		}
		
		nTaxon++;
	}
	
	
	private boolean[] ivisit = null;
	// recursively create newick string of internal node
	private String nwkInternal(int ni) {
		ivisit[ni] = true;
		
		String nwk = "(";
		boolean empty = true;
		if(taxa.get(ni).size() > 0) {
			nwk += taxa.get(ni).get(0);
			empty = false;
		}
		for(int i = 1; i < taxa.get(ni).size(); i++) {
			nwk += "," + taxa.get(ni).get(i);
		}
		
		boolean first = true;
		for(int i = 0; i < imap.length; i++) {
			if(imap[ni][i] && (!ivisit[i])) {
				if(first) {
					if(!empty) nwk += ",";
					nwk += nwkInternal(i);
					first = false;
				}
				else {
					nwk += "," + nwkInternal(i);
				}
			}
		}
		
		return nwk + ")";
	}
	
	// create newick string of this tree
	public String nwkString() {
		ivisit = new boolean[imap.length];
		return nwkInternal(0) + ";";
	}
}
