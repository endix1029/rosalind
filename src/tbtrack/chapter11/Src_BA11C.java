package tbtrack.chapter11;

import tools.*;

public class Src_BA11C {
	public static boolean[] pepVector(String peptide) {
		Peptide pep = new Peptide();
		
		boolean[] pvec = new boolean[pep.peptideMass(peptide)];
		for(int i = 1; i <= peptide.length(); i++) {
			pvec[pep.peptideMass(peptide.substring(0, i)) - 1] = true;
		}
		
		return pvec;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'c');
		String peptide = file.read();
		
		boolean[] pvec = pepVector(peptide);
		for(boolean b : pvec) {
			file.type(b ? "1 " : "0 ");
		}
		
		file.write("");
		file.close();
	}
}
