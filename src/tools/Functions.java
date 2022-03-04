/*
 *	tools.Functions
 *	Static functions for general bioinformatics
 */

package tools;

import java.util.*;
import java.io.IOException;

public class Functions {
	public static char base_reverse(char base){
		switch(base){
			case 'A' : return 'T';
			case 'T' : return 'A';
			case 'C' : return 'G';
			case 'G' : return 'C';
			default : return 'X';
		}	
	}

	public static String seq_revcomp(String seq){
		String rev = "";

		for(int i = seq.length(); i > 0; i--){
			rev += base_reverse(seq.charAt(i - 1));
		}

		return rev;
	}

	public static int seq_LCS(String seqX, String seqY){
		int[][] table = new int[seqX.length() + 1][seqY.length() + 1];
		for(int i = 0; i <= seqX.length(); i++) table[i][0] = 0;
		for(int j = 0; j <= seqY.length(); j++) table[0][j] = 0;

		for(int i = 1; i <= seqX.length(); i++){
			for(int j = 1; j <= seqY.length(); j++){
				if(seqX.charAt(i - 1) == seqY.charAt(j - 1)) table[i][j] = table[i - 1][j - 1] + 1;
				else table[i][j] = table[i - 1][j] > table[i][j - 1] ? table[i - 1][j] : table[i][j - 1];
			}
		}

		return table[seqX.length()][seqY.length()];
	}
	
	public static int hamming(String seqX, String seqY) {
		if(seqX.length() != seqY.length()) return 1 << 31;
		
		int dist = 0;
		for(int i = 0; i < seqX.length(); i++) {
			if(seqX.charAt(i) != seqY.charAt(i)) dist++;
		}
		
		return dist;
	}
	
	public static int seq_hash(String seq) {
		String binseq = "";
		
		for(int i = 0; i < seq.length(); i++) {
			switch(seq.charAt(i)) {
			case 'A':
				binseq += "00";
				break;
			case 'C':
				binseq += "01";
				break;
			case 'G':
				binseq += "10";
				break;
			case 'T': case 'U':
				binseq += "11";
				break;
			default:
				break;
			}
		}
		
		return Integer.parseInt(binseq, 2);
	}
	
	public static long seq_longhash(String seq) {
		String binseq = "";
		
		for(int i = 0; i < seq.length(); i++) {
			switch(seq.charAt(i)) {
			case 'A':
				binseq += "00";
				break;
			case 'C':
				binseq += "01";
				break;
			case 'G':
				binseq += "10";
				break;
			case 'T':
				binseq += "11";
				break;
			default:
				break;
			}
		}
		
		return Long.parseLong(binseq, 2);
	}
	
	public static String seq_revhash(int val, int k) {
		String binseq = Integer.toBinaryString(val);
		int lzero = k * 2 - binseq.length();
		for(int i = 0; i < lzero; i++) {
			binseq = "0" + binseq;
		}
		
		String seq = "";
		
		for(int i = 0; i < binseq.length() / 2; i++) {
			String binfrag = binseq.substring(i * 2, i * 2 + 2);
			if(binfrag.equals("00")) {
				seq += "A";
			}
			else if(binfrag.equals("01")) {
				seq += "C";
			}
			else if(binfrag.equals("10")) {
				seq += "G";
			}
			else {
				seq += "T";
			}
		}
		
		return seq;
	}
	
	public static ArrayList<String> neighbor(String kmer, int d){
		ArrayList<String> neighs = new ArrayList<String>();
		neighs.add(kmer);
		
		for(int i = 0; i < d; i++) {
			ArrayList<String> prev = new ArrayList<String>();
			for(String neigh : neighs) prev.add(neigh);
			
			for(String nprev : prev) {
				for(int loc = 0; loc < nprev.length(); loc++) {
					String subA = nprev.substring(0, loc) + "A" + nprev.substring(loc + 1);
					String subC = nprev.substring(0, loc) + "C" + nprev.substring(loc + 1);
					String subG = nprev.substring(0, loc) + "G" + nprev.substring(loc + 1);
					String subT = nprev.substring(0, loc) + "T" + nprev.substring(loc + 1);
					
					switch(nprev.charAt(loc)) {
					case 'A':
						neighs.add(subC);
						neighs.add(subG);
						neighs.add(subT);
						break;
					case 'C':
						neighs.add(subA);
						neighs.add(subG);
						neighs.add(subT);
						break;
					case 'G':
						neighs.add(subA);
						neighs.add(subC);
						neighs.add(subT);
						break;
					case 'T':
						neighs.add(subA);
						neighs.add(subC);
						neighs.add(subG);
						break;
					}
				}
			}
		}
		
		ArrayList<String> distincts = new ArrayList<String>();
		boolean[] flags = new boolean[1 << (kmer.length() * 2)];
		
		for(String neigh : neighs) {
			int hval = Functions.seq_hash(neigh);
			if(!flags[hval]) {
				flags[hval] = true;
				distincts.add(neigh);
			}
		}
		
		return distincts;
	}
	
	private static String CODONTABLE = "KNKNTTTTRSRSIIMIQHQHPPPPRRRRLLLLEDEDAAAAGGGGVVVVXYXYSSSSXCWCLFLF";
	public static char tsln_codon(String codon) {
		if(codon.length() != 3) return '-';
		return CODONTABLE.charAt(seq_hash(codon));
	}
	
	public static String tsln_seq(String seq, int dir) {
		if(dir != 3 && dir != 5) return null;
		if(seq.length() % 3 != 0) return null;
		
		if(dir == 5) seq = seq_revcomp(seq);
		seq = seq.replace('T', 'U');
		
		String prtn = "";
		for(int i = 0; i < seq.length(); i += 3) {
			prtn += tsln_codon(seq.substring(i, i + 3));
		}
		
		return prtn;
	}
	
	public static List<Integer> linearSpectrum(String prtn) throws IOException {
		MassTable mt = new MassTable(true);
		List<Integer> spectrum = new ArrayList<Integer>();
		spectrum.add(0);
		
		if(prtn.contains("-")) {
			String newprtn = "";
			for(String val : prtn.split("-")) {
				newprtn += mt.hashSimpleMass(Integer.parseInt(val));
			}
			prtn = newprtn;
		}
		
		int spec = 0;
		for(int loc = 0; loc < prtn.length(); loc++) {
			spec += mt.getSimpleMass(prtn.charAt(loc));
		}
		spectrum.add(spec);
		
		for(int len = 1; len < prtn.length(); len++) {
			for(int loc = 0; loc < prtn.length(); loc++) {
				String sub;
				if(loc + len > prtn.length()) continue;
				else {
					sub = prtn.substring(loc, loc + len);
				}
				
				spec = 0;
				for(int i = 0; i < len; i++) {
					spec += mt.getSimpleMass(sub.charAt(i));
				}
				spectrum.add(spec);
			}
		}
		
		Collections.sort(spectrum);
		return spectrum;
	}
	
	public static int linearScore(String prtn, List<Integer> query) throws IOException {
		List<Integer> spectrum = linearSpectrum(prtn);
		
		int i = 0, j = 0, score = 0;
		while(i < spectrum.size() && j < query.size()) {
			int ref = spectrum.get(i), qry = query.get(j);
			if(ref == qry) {
				score++;
				i++;
				j++;
			}
			else if(ref > qry) j++;
			else i++;
		}
		
		return score;
	}
	
	public static double euclideanDouble(double[] px, double[] py) {
		if(px.length != py.length) return -1;
		int dim = px.length;
		
		double dist = .0;
		for(int i = 0; i < dim; i++) dist += (px[i] - py[i]) * (px[i] - py[i]);
		return Math.sqrt(dist);
	}
	
	public static double[] oneVector(int size) {
		double[] vector = new double[size];
		for(int i = 0; i < size; i++) vector[i] = 1.0;
		return vector;
	}
	
	public static double dotProduct(double[] vecx, double[] vecy) {
		if(vecx.length != vecy.length) return -1;
		
		double product = .0;
		for(int i = 0; i < vecx.length; i++) product += vecx[i] * vecy[i];
		
		return product;
	}
}
