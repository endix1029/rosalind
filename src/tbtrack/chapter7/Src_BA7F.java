package tbtrack.chapter7;

import tools.*;

class ParsimonyNode {
	String seq;
	int[][] parsimony;
	boolean tag;
//	boolean ripeness;
	ParsimonyNode daughter, son;
	int[][] minBase;
	
	// leaf constructor
	public ParsimonyNode(String seq) {
		this.seq = seq;
		this.parsimony = new int[4][seq.length()];
		this.minBase = new int[4][seq.length()];
		this.tag = true;
//		this.ripeness = false;
		this.daughter = null;
		this.son = null;
		
		for(int i = 0; i < seq.length(); i++) {
			this.parsimony[0][i] = Integer.MAX_VALUE / 3;
			this.parsimony[1][i] = Integer.MAX_VALUE / 3;
			this.parsimony[2][i] = Integer.MAX_VALUE / 3;
			this.parsimony[3][i] = Integer.MAX_VALUE / 3;
			if(seq.charAt(i) == 'A') this.parsimony[0][i] = 0;
			if(seq.charAt(i) == 'C') this.parsimony[1][i] = 0;
			if(seq.charAt(i) == 'G') this.parsimony[2][i] = 0;
			if(seq.charAt(i) == 'T') this.parsimony[3][i] = 0;
		}
	}
	
	// internal node constructor
	public ParsimonyNode() {
		this.seq = null;
		this.parsimony = null;
		this.tag = false;
//		this.ripeness = true;
		this.daughter = null;
		this.son = null;
	}
	
	public void adopt(ParsimonyNode child) {
		if(this.daughter == null) {
			this.daughter = child;
			this.parsimony = new int[4][child.parsimony[0].length];
			this.minBase = new int[4][child.parsimony[0].length];
		}
		else this.son = child;
	}
	
	public boolean isRipe() {
		if(this.daughter == null || this.son == null) return false;
		return !(this.tag) & this.daughter.tag & this.son.tag; 
	}
}

public class Src_BA7F {
	protected static int bton(char b) {
		switch(b) {
		case 'A': return 0;
		case 'C': return 1;
		case 'G': return 2;
		case 'T': return 3;
		}
		return -1;
	}
	
	protected static char ntob(int n) {
		switch(n) {
		case 0: return 'A';
		case 1: return 'C';
		case 2: return 'G';
		case 3: return 'T';
		}
		return 'X';
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'f');
		int n = Integer.parseInt(file.read());
		
		ParsimonyNode[] parsimonyTree = new ParsimonyNode[n * 2];
		int ci = 0; // child iterator
		for(int i = 0; i < 2 * n - 2; i++) {
			String line = file.read();
			int p = Integer.parseInt(line.split("->")[0]);
			if(parsimonyTree[p] == null) parsimonyTree[p] = new ParsimonyNode();
			
			if(line.split("->")[1].charAt(0) <= '9') {
				int c = Integer.parseInt(line.split("->")[1]);
				parsimonyTree[p].adopt(parsimonyTree[c]);
			}
			else {
				parsimonyTree[ci] = new ParsimonyNode(line.split("->")[1]);
				parsimonyTree[p].adopt(parsimonyTree[ci++]);
			}
		}
		
		int len = parsimonyTree[0].seq.length();
		int next = n;
		do {
			ParsimonyNode p = parsimonyTree[next];
			p.tag = true;
			
			for(int i = 0; i < len; i++) {
				for(int k = 0; k < 4; k++) {
					int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
					for(int x = 0; x < 4; x++) {
						int tx = p.daughter.parsimony[x][i] + (k == x ? 0 : 1);
						if(minX > tx) {
							minX = tx;
							p.daughter.minBase[k][i] = x;
						}
					}
					for(int y = 0; y < 4; y++) {
						int ty = p.son.parsimony[y][i] + (k == y ? 0 : 1);
						if(minY > ty) {
							minY = ty;
							p.son.minBase[k][i] = y;
						}
					}
					p.parsimony[k][i] = minX + minY;
				}
			}
			for(next = n; parsimonyTree[next] != null && !parsimonyTree[next].isRipe(); next++);
		}
		while(next <= 2 * n - 2);
		
		ParsimonyNode root = parsimonyTree[2 * n - 2];
		int minParsimony = 0;
		String pseq = "";
		for(int i = 0; i < len; i++) {
			int localMin = root.parsimony[0][i], minK = 0;
			
			for(int k = 1; k < 4; k++) {
				if(localMin > root.parsimony[k][i]) {
					localMin = root.parsimony[k][i];
					minK = k;
				}
			}
			
			minParsimony += localMin;
			pseq += ntob(minK);
		}
		root.seq = pseq;
		file.writeObj(minParsimony);
		
		for(int i = 2 * n - 2; i >= n; i--) {
			ParsimonyNode pnode = parsimonyTree[i];
			if(pnode.daughter == null) break;
			
			String dseq = "", sseq = "";
			for(int j = 0; j < len; j++) {
				int bcode = bton(pnode.seq.charAt(j));
				
				dseq += ntob(pnode.daughter.minBase[bcode][j]);
				sseq += ntob(pnode.son.minBase[bcode][j]);
			}
			
			pnode.daughter.seq = dseq;
			pnode.son.seq = sseq;
			
			file.write(String.format("%s->%s:%d", pnode.seq, dseq, Functions.hamming(pnode.seq, dseq)));
			file.write(String.format("%s->%s:%d", dseq, pnode.seq, Functions.hamming(pnode.seq, dseq)));
			file.write(String.format("%s->%s:%d", pnode.seq, sseq, Functions.hamming(pnode.seq, sseq)));
			file.write(String.format("%s->%s:%d", sseq, pnode.seq, Functions.hamming(pnode.seq, sseq)));
		}
		
		file.close();
	}
}
