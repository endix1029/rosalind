package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1B {
	public static int hash(String seq) {
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
		
		return Integer.parseInt(binseq, 2);
	}
	
	public static String revhash(int val, int k) {
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
	
	public static void main(String[] args) throws java.io.IOException{
		FileIO file = new FileIO(1, 'b');
		
		String text = file.read();
		int k = Integer.parseInt(file.read());
		file.close();
		
		int[] freqs = new int[1 << (k * 2)];
		for(int i = 0; i < text.length() - k; i++) {
			freqs[hash(text.substring(i, i + k))]++;
		}
		
		int max = 0;
		for(int freq : freqs) {
			if(freq > max) {
				max = freq;
			}
		}
		
		for(int i = 0; i < freqs.length; i++) {
			if(max == freqs[i]) {
				System.out.print(revhash(i, k) + " ");
			}
		}
	}
}