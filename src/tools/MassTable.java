package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MassTable {
	private double[] mass = new double[20];
	private int[] mass_simple = new int[20];
	
	private int[] ahash = new int[30];
	private char[] ihash = new char[30];
	
	public MassTable(boolean simple) throws IOException {
		BufferedReader fstream = new BufferedReader(
				new FileReader("lib/table/mass" + (simple ? "_simple" : "") + ".txt"));
		
		String line;
		int iter = 0;
		while((line = fstream.readLine()) != null) {
			String[] spl = line.split("\t");
			
			char acid = spl[0].charAt(0);
			ahash[(int) acid - 'A'] = iter;
			ihash[iter] = acid;
			
			if(simple) {
				mass_simple[iter++] = Integer.parseInt(spl[1]);
			}
			else {
				mass[iter++] = Double.parseDouble(spl[1]);
			}
		}
		
		fstream.close();
	}

	public double getMass(char acid) {
		return mass[ahash[(int) acid - 'A']];
	}
	
	public int getSimpleMass(char acid) {
		return mass_simple[ahash[(int) acid - 'A']];
	}
	
	public int getSimpleIndex(int index) {
		return mass_simple[index];
	}

	public char hashMass(double val, double error){
		int iter = 0;
		for(double mval : mass){
			if(Math.abs(mval - val) < error) return ihash[iter];
		}
		iter++;

		return 'X';
	}
	
	public char hashSimpleMass(int val){
		int iter = 0;
		for(int mval : mass_simple){
			if(mval == val) return ihash[iter];
			iter++;
		}

		return 'X';
	}
}
