package tbtrack.chapter11;

import tools.*;

class SpectralAlignmentNode {
	private static final Peptide PEP = new Peptide();
	
	private int pmass, dmass, layer;
	public int maxScore;
	private SpectralAlignmentNode prev;
	
	public SpectralAlignmentNode(int i, int j, int t) {
		this.pmass = i;
		this.dmass = j;
		this.layer = t;
		
		this.maxScore = -(1<<20);
		this.prev = null;
	}
	
	public void init(int iscore) {
		this.maxScore = iscore;
	}
	
	public void compare(int score, SpectralAlignmentNode node) {
		if(node == null) return;
		if(score + node.maxScore > this.maxScore) {
			this.maxScore = score + node.maxScore;
			this.prev = node;
		}
	}
	
	public String trace() {
		if(this.prev == null) return "";
		
		int diff = this.pmass - prev.pmass;
		char aa = PEP.getAcid(diff);
		int mod = this.dmass - prev.dmass - diff;
		
		if(mod == 0) return prev.trace() + aa;
		else return prev.trace() + String.format("%c(%s)", aa, mod < 0 ? String.valueOf(mod) : "+" + mod);
	}
	
	public int getLayer() {return this.layer;}
}

public class Src_BA11J {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'j');
		Peptide pep = new Peptide();
		
		String peptide = file.read();
		int mass = 0;
		int[] diff = new int[peptide.length()];
		for(int i = 0; i < peptide.length(); i++) {
			diff[i] = pep.getMass(peptide.charAt(i));
			mass += diff[i];
		}
		
		String[] mspl = file.read().split(" ");	
		int[] spec = new int[mspl.length + 1];
		spec[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			spec[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		int k = Integer.parseInt(file.read());
		
		SpectralAlignmentNode[][][] graph = new SpectralAlignmentNode[mass + 1][spec.length][k + 1];
		graph[0][0][0] = new SpectralAlignmentNode(0, 0, 0);
		graph[0][0][0].init(0);
		
		for(int t = 0; t <= k; t++) {
			int i = 0;
			for(int x = 0; x < diff.length; x++) {
				i += diff[x];
				for(int j = 0; j < spec.length; j++) {
					graph[i][j][t] = new SpectralAlignmentNode(i, j, t);
					
					if(j - diff[x] >= 0) {
						graph[i][j][t].compare(spec[j], graph[i - diff[x]][j - diff[x]][t]);
					}
					
					if(t > 0) {
						for(int jj = 0; jj < j; jj++) {
							graph[i][j][t].compare(spec[j], graph[i - diff[x]][jj][t - 1]);
						}
					}
				}
			}
		}
		
		int maxScore = -(1<<20);
		String maxTrace = null;
		for(int t = 0; t <= k; t++) {
			int score = graph[mass][spec.length - 1][t].maxScore;
			String trace = graph[mass][spec.length - 1][t].trace();
			if(score > -(1<<20)) {
				if(score > maxScore) {
					maxScore = score;
					maxTrace = trace;
				}
				System.out.println(String.format("Layer %d : %s (score %d)", t, trace, score));
			}
		}
		
		file.write(maxTrace);
		file.close();
	}
}
