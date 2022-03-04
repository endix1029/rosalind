package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiddenMarkov {
	protected List<Character> sigma;
	protected Map<Character, Integer> sigmaMap;
	
	private List<Character> states;
	private Map<Character, Integer> stateMap;
	
	protected double[][] transition;
	protected double[][] emission;
	
	// states only
	public HiddenMarkov(String vstr) {
		this.states = new ArrayList<Character>();
		this.stateMap = new HashMap<Character, Integer>();
		
		int entry = 0;
		for(String sstr : vstr.split("\t")) {
			char state = sstr.charAt(0);
			states.add(state);
			stateMap.put(state, entry++);
		}
		
		this.transition = new double[this.states.size()][this.states.size()];
	}
	
	// sigma and states
	public HiddenMarkov(String sgstr, String ststr) {
		this.sigma		= new ArrayList<Character>();
		this.states		= new ArrayList<Character>();
		this.sigmaMap	= new HashMap<Character, Integer>();
		this.stateMap	= new HashMap<Character, Integer>();
		
		int entry = 0;
		for(String sstr : sgstr.split("\t")) {
			char alpha = sstr.charAt(0);
			sigma.add(alpha);
			sigmaMap.put(alpha, entry++);
		}
		
		entry = 0;
		for(String sstr : ststr.split("\t")) {
			char state = sstr.charAt(0);
			states.add(state);
			stateMap.put(state, entry++);
		}
		
		this.transition	= new double[this.stateCount()][this.stateCount()];
		this.emission	= new double[this.stateCount()][this.sigmaCount()];
	}
	
	// sigma only; for profileHMM
	public HiddenMarkov(String sgstr, boolean isProfile) {
		this.sigma 		= new ArrayList<Character>();
		this.sigmaMap 	= new HashMap<Character, Integer>();
		
		int entry = 0;
		for(String sstr : sgstr.split("\t")) {
			char alpha = sstr.charAt(0);
			sigma.add(alpha);
			sigmaMap.put(alpha, entry++);
		}
	}
	
	public int sigmaCount() {return sigma .size();}
	public int stateCount() {return states.size();}
	public char getState(int entry) {return states.get(entry);}
	public char getAlpha(int entry) {return sigma .get(entry);}
	public int getStateEntry(char state) {return stateMap.get(state);}
	public int getAlphaEntry(char alpha) {return sigmaMap.get(alpha);}
	
	public void setTransition(List<String> mstrList) {
		// header setup
		List<Integer> hlist = new ArrayList<Integer>(); // header state entry list
		String[] headers = mstrList.get(0).split("\t");
		for(int i = 1; i < headers.length; i++) hlist.add(stateMap.get(headers[i].charAt(0)));
		
		// matrix buildup
		for(int i = 1; i < mstrList.size(); i++) {
			String[] mspl = mstrList.get(i).split("\t");
			int tstate = stateMap.get(mspl[0].charAt(0)); // target state
			for(int j = 1; j < mspl.length; j++) {
				transition[tstate][hlist.get(j - 1)] = Double.parseDouble(mspl[j]);
			}
		}
	}
	public void setTransition(char stateX, char stateY, double value) {
		transition[stateMap.get(stateX)][stateMap.get(stateY)] = value;
	}
	
	public void setEmission(List<String> mstrList) {
		// header setup
		List<Integer> hlist = new ArrayList<Integer>(); // header state entry list
		String[] headers = mstrList.get(0).split("\t");
		for(int i = 1; i < headers.length; i++) hlist.add(sigmaMap.get(headers[i].charAt(0)));
		
		// matrix buildup
		for(int i = 1; i < mstrList.size(); i++) {
			String[] mspl = mstrList.get(i).split("\t");
			int tstate = stateMap.get(mspl[0].charAt(0)); // target state
			
			for(int j = 1; j < mspl.length; j++) {
				emission[tstate][hlist.get(j - 1)] = Double.parseDouble(mspl[j]);
			}
		}
	}
	public void setEmission(char state, char alpha, double value) {
		emission[stateMap.get(state)][sigmaMap.get(alpha)] = value;
	}
	
	public double getTransition(char stateX, char stateY) {
		return transition[stateMap.get(stateX)][stateMap.get(stateY)];
	}
	public double getEmission(char state, char alpha) {
		return emission[stateMap.get(state)][sigmaMap.get(alpha)];
	}
}
