package tools;

import java.util.*;

public class ProfileHMM extends HiddenMarkov {
	private List<String> alignment;
	private List<String> states;
	private Map<String, Integer> stateMap;
	
	private boolean[] columnDensity;
	private int alignLen; // actual alignment length
	private int denseLen; // count of dense alignment column
	private double theta; // gap tolerance
	
	public ProfileHMM(String sgstr, double theta) {
		super(sgstr, true);
		this.theta = theta;
	}
	
	private boolean sequenceIntegrity(List<String> alignment) {
		for(String seq : alignment) {
			if(seq.length() != alignLen) return false;
		}
		return true;
	}
	
	private void setupColumn() {
		int acount = alignment.size();
		this.columnDensity = new boolean[alignLen];
		this.denseLen = 0;
		for(int i = 0; i < alignLen; i++) {
			int gapcount = 0;
			for(String seq : alignment) {
				if(seq.charAt(i) == '-') gapcount++;
			}
			
			if((double) gapcount / acount < theta) {
				columnDensity[i] = true;
				denseLen++;
			}
		}
	}
	
	private void setupState() {
		this.states = new ArrayList<String>();
		states.add("S");
		states.add("I0");
		for(int i = 1; i <= denseLen; i++) {
			states.add("M" + i);
			states.add("D" + i);
			states.add("I" + i);
		}
		states.add("E");
		
		this.stateMap = new HashMap<String, Integer>();
		int entry = 0;
		for(String state : states) stateMap.put(state, entry++);
	}
	
	private void buildMatrix() {
		super.transition = new double[this.profileStateCount()][this.profileStateCount()];
		super.emission =   new double[this.profileStateCount()][super.sigmaCount()];
		
		int[]   transTotal = new int[this.profileStateCount()];
		int[][] transCount = new int[this.profileStateCount()][this.profileStateCount()];
		int[]   emissTotal = new int[this.profileStateCount()];
		int[][] emissCount = new int[this.profileStateCount()][super.sigmaCount()];
		
		for(String seq : alignment) {
			String state = "S", nextState;
			int iter = 0;
			
			for(int col = 0; col < seq.length(); col++) {
				char query = seq.charAt(col);
				if(columnDensity[col]) { // dense column
					iter++;
					if(query == '-') { // Delete phase
						nextState = "D" + iter;
						transTotal[stateMap.get(state)]++;
						transCount[stateMap.get(state)][stateMap.get(nextState)]++;
						state = nextState;
					}
					else { // Match phase
						nextState = "M" + iter;
						transTotal[stateMap.get(state)]++;
						transCount[stateMap.get(state)][stateMap.get(nextState)]++;
						state = nextState;
						
						emissTotal[stateMap.get(state)]++;
						emissCount[stateMap.get(state)][sigmaMap.get(query)]++;
					}
				}
				else { // sparse column
					if(query == '-') continue;
					else { // Insert phase
						nextState = "I" + iter;
						transTotal[stateMap.get(state)]++;
						transCount[stateMap.get(state)][stateMap.get(nextState)]++;
						state = nextState;
						
						emissTotal[stateMap.get(state)]++;
						emissCount[stateMap.get(state)][sigmaMap.get(query)]++;
					}
				}
			}
			
			nextState = "E";
			transTotal[stateMap.get(state)]++;
			transCount[stateMap.get(state)][stateMap.get(nextState)]++;
		}
		
		for(int i = 0; i < this.profileStateCount(); i++) {
			for(int j = 0; j < this.profileStateCount(); j++) {
				super.transition[i][j] = (double) transCount[i][j] / (transTotal[i] == 0? 1 : transTotal[i]);
			}
			for(int j = 0; j < super.sigmaCount(); j++) {
				super.emission[i][j] = (double) emissCount[i][j] / (emissTotal[i] == 0? 1 : emissTotal[i]);
			}
		}
	}
	
	public void parseAlignment(List<String> alignment) {
		this.alignLen = alignment.get(0).length();
		this.alignment = alignment;
		
		if(!sequenceIntegrity(alignment)) {
			System.err.println("Improperly aligned sequence exists");
			return;
		}
		setupColumn();
		setupState();
		buildMatrix();
	}
	
	public boolean validTransition(String stateX, String stateY) {
		if(stateX.equals("E")) return false;
		if(stateX.equals("S")) stateX = "I0";
		
		int suffix = Integer.parseInt(stateX.substring(1));
		if(suffix == denseLen && stateY.equals("E")) return true;
		if(stateY.equals("I" + suffix)) return true;
		if(stateY.equals("M" + (suffix + 1))) return true;
		if(stateY.equals("D" + (suffix + 1))) return true;
		
		return false;
	}
	
	private List<String> validDsts(String state){
		List<String> valid = new ArrayList<String>();
		
		for(int i = 0; i < states.size(); i++) {
			if(validTransition(state, states.get(i))) valid.add(states.get(i));
		}
		
		return valid;
	}
	
	public void applyPseudoCount(double pseudo) {
		// fix transition matrix
		for(int i = 0; i < states.size(); i++) {
			List<String> valid = validDsts(states.get(i));
			double sum = .0;
			for(String vdst : valid) sum += transition[i][stateMap.get(vdst)];
			for(String vdst : valid) {
				transition[i][stateMap.get(vdst)] = (transition[i][stateMap.get(vdst)] + pseudo) / (sum + pseudo * valid.size());
			}
		}
		
		// fix emission matrix
		for(int i = 0; i < states.size(); i++) {
			if(states.get(i).charAt(0) != 'I' && states.get(i).charAt(0) != 'M') continue;
			double sum = .0;
			for(int j = 0; j < sigma.size(); j++) sum += emission[i][j];
			for(int j = 0; j < sigma.size(); j++) {
				emission[i][j] = (emission[i][j] + pseudo) / (sum + pseudo * sigma.size());
			}
		}
	}
	
	public int profileStateCount() {return states.size();}
	public String getProfileState(int entry) {return states.get(entry);}
	public int getDenseLen() {return denseLen;}
	
	public double getTransition(String stateX, String stateY) {return transition[stateMap.get(stateX)][stateMap.get(stateY)];}
	public double getEmission(String state, char alpha) {return emission[stateMap.get(state)][sigmaMap.get(alpha)];}
}
