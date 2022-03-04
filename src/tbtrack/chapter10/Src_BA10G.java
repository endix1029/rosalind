package tbtrack.chapter10;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.ProfileHMM;

class ViterbiNode {
	public int column;
	public String state;
	
	public double maxProbLog = Math.log10(Double.MIN_VALUE);
	public ViterbiNode tracker = null;
	
	public ViterbiNode(int col, String st) {
		this.column = col;
		this.state  = st;
	}
	
	public void compare(ProfileHMM phmm, ViterbiNode prev, String opath) {
		if(!phmm.validTransition(prev.state, this.state)) return;
		if(this.state.charAt(0) == 'D') { // vertical transfer
			if(prev.column != this.column) return;
			
			// update
			double probLog = prev.maxProbLog + Math.log10(phmm.getTransition(prev.state, this.state));
			if(probLog > this.maxProbLog) {
				this.maxProbLog = probLog;
				this.tracker = prev;
			}
		}
		else if(this.state.charAt(0) == 'E') { // sink exception
			if(prev.column + 1 != this.column) return;
			
			// update
			double probLog = prev.maxProbLog + Math.log10(phmm.getTransition(prev.state, this.state));
			
			if(probLog > this.maxProbLog) {
				this.maxProbLog = probLog;
				this.tracker = prev;
			}
		}
		else { // horizontal transfer
			if(prev.column + 1 != this.column) return; 
			
			// update
			double probLog = prev.maxProbLog + Math.log10(phmm.getTransition(prev.state, this.state))
					+ Math.log10(phmm.getEmission(this.state, opath.charAt(prev.column)));
			if(probLog > this.maxProbLog) {
				this.maxProbLog = probLog;
				this.tracker = prev;
			}
		}
	}
}

public class Src_BA10G {
	public static String profileViterbi(ProfileHMM phmm, String opath) {
		// Build Viterbi node topology
		List<ViterbiNode> viterbiGraph = new ArrayList<ViterbiNode>();
		
		// First column buildup
		viterbiGraph.add(new ViterbiNode(0, "S"));
		for(int i = 1; i <= phmm.getDenseLen(); i++) viterbiGraph.add(new ViterbiNode(0, "D" + i));
		
		// Internal column buildup
		int col = 1;
		while(col <= opath.length()) {
			viterbiGraph.add(new ViterbiNode(col, "I0"));
			for(int i = 1; i <= phmm.getDenseLen(); i++) {
				viterbiGraph.add(new ViterbiNode(col, "M" + i));
				viterbiGraph.add(new ViterbiNode(col, "D" + i));
				viterbiGraph.add(new ViterbiNode(col, "I" + i));
			}
			col++;
		}
		
		// Sink node
		viterbiGraph.add(new ViterbiNode(col, "E"));
		
		// Update graph by implementing bubble sort algorithm
		viterbiGraph.get(0).maxProbLog = .0;
		for(int i = 1; i < viterbiGraph.size(); i++) {
			for(int j = 0; j < i; j++) viterbiGraph.get(i).compare(phmm, viterbiGraph.get(j), opath);
		}
		
		
		// Backtrack
		ViterbiNode vnode = viterbiGraph.get(viterbiGraph.size() - 1);
		String optPath = "";
		while(vnode.tracker != null) {
			optPath = vnode.tracker.state + " " + optPath;
			vnode = vnode.tracker;
		}
		
		return optPath.substring(2);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'g');
		String path = file.read();
		
		file.read();
		String line = file.read();
		double theta = Double.parseDouble(line.split(" ")[0]), pseudo = Double.parseDouble(line.split(" ")[1]);
		
		file.read();
		ProfileHMM phmm = new ProfileHMM(file.read().replace(" ", "\t"), theta);
		file.read();
		
		List<String> alignment = new ArrayList<String>();
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			alignment.add(line);
		}
		
		phmm.parseAlignment(alignment);
		phmm.applyPseudoCount(pseudo);
		
		file.write(profileViterbi(phmm, path));
		file.close();
	}
}
