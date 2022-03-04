package tbtrack.chapter7;

import tools.*;

public class Src_BA7G extends Src_BA7F {
	private static int INTERNAL; // internal node index
	
	private static void construct(boolean[][] adj, ParsimonyNode[] parsimonyTree, int index, boolean[] visit) {
		visit[index] = true;
		if(parsimonyTree[index].tag) return;
		
		for(int i = 0; i < INTERNAL; i++) {
			if(visit[i]) continue;
			
			if(adj[i][index]) {
				construct(adj, parsimonyTree, i, visit);
				parsimonyTree[index].adopt(parsimonyTree[i]);
			}
		}
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(7, 'g');
		int n = Integer.parseInt(file.read());
		
		ParsimonyNode[] parsimonyTree = new ParsimonyNode[n * 2];
		boolean[][] adjMatrix = new boolean[n * 2][n * 2]; // adjacent matrix for rooted tree construction
		int ci = 0; // child iterator
		int ln = -1, rn = -1;
		for(int i = 0; i < 2 * n - 3; i++) {
			ln = -1;
			rn = -1;
			
			String line = file.read(); // abandon one direction
			line = file.read();
			
			String left = line.split("->")[0], right = line.split("->")[1];
			if(left. charAt(0) <= '9') ln = Integer.parseInt(left);
			if(right.charAt(0) <= '9') rn = Integer.parseInt(right);
			
			if(ln >= 0 && parsimonyTree[ln] == null) parsimonyTree[ln] = new ParsimonyNode();
			if(rn >= 0 && parsimonyTree[rn] == null) parsimonyTree[rn] = new ParsimonyNode();
			
			if(ln < 0) {
				parsimonyTree[ci] = new ParsimonyNode(left);
				ln = ci++;
			}
			if(rn < 0) {
				parsimonyTree[ci] = new ParsimonyNode(right);
				rn = ci++;
			}
			
			adjMatrix[ln][rn] = true;
			adjMatrix[rn][ln] = true;
		}
		
		// tree reconstruction and root assignment
		INTERNAL = 2 * n - 2;
		
		ParsimonyNode root = new ParsimonyNode();
		parsimonyTree[INTERNAL] = root;
		adjMatrix[INTERNAL][ln] = true;
		adjMatrix[ln][INTERNAL] = true;
		adjMatrix[INTERNAL][rn] = true;
		adjMatrix[rn][INTERNAL] = true;
		adjMatrix[ln][rn] = false;
		adjMatrix[rn][ln] = false;
		
		construct(adjMatrix, parsimonyTree, INTERNAL, new boolean[INTERNAL + 1]);
		
		// run simple parsimony algorithm
		// remove and merge root edges
		
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
		
//		ParsimonyNode root = parsimonyTree[2 * n - 2];
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
		
		boolean rootFlag = true;
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
			
			if(rootFlag) {
				file.write(String.format("%s->%s:%d", dseq, sseq, Functions.hamming(dseq, sseq)));
				file.write(String.format("%s->%s:%d", sseq, dseq, Functions.hamming(sseq, dseq)));
				rootFlag = false;
			}
			else {
				file.write(String.format("%s->%s:%d", pnode.seq, dseq, Functions.hamming(pnode.seq, dseq)));
				file.write(String.format("%s->%s:%d", dseq, pnode.seq, Functions.hamming(pnode.seq, dseq)));
				file.write(String.format("%s->%s:%d", pnode.seq, sseq, Functions.hamming(pnode.seq, sseq)));
				file.write(String.format("%s->%s:%d", sseq, pnode.seq, Functions.hamming(pnode.seq, sseq)));
			}
		}
		
		file.close();
	}
}
