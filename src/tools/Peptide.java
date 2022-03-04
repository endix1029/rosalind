package tools;

import java.util.*;

public class Peptide {
	public static final char[] AA = {
			'A',	'C',	'D',	'E',	'F',
			'G',	'H',	'I',	'K',	'L',
			'M',	'N',	'P',	'Q',	'R',
			'S',	'T',	'V',	'W',	'Y',
//			'X',	'Z'
	};
	
	public static final int[] MASS = {
			71,		103,	115,	129,	147,
			57,		137,	113,	128,	113,
			131,	114,	97,		128,	156,
			87,		101,	99,		186,	163,
//			4,		5
	};
	
	private Map<Character, Integer> pmap;
	private Map<Integer, Character> massmap;
	
	public Peptide() {
		this.pmap = new HashMap<Character, Integer>();
		this.massmap = new HashMap<Integer, Character>();
		
		for(int i = 0; i < AA.length; i++) {
			this.pmap.put(AA[i], i);
		}
		
		for(int i = 0; i < AA.length; i++) {
			this.massmap.put(MASS[i], AA[i]);
		}
	}
	
	public int getMass(char aa) {
		return MASS[pmap.get(aa)];
	}
	
	public char getAcid(int mass) {
		return massmap.get(mass) == null ? '*' : massmap.get(mass);
	}
	
	public int peptideMass(String peptide) {
		int mass = 0;
		for(int i = 0; i < peptide.length(); i++) {
			mass += getMass(peptide.charAt(i));
		}
		return mass;
	}
}
