package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1E {
	public static void main(String[] args) throws java.io.IOException {
		long iTime = System.nanoTime();
		
		FileIO file = new FileIO(1, 'e');
		
		String genome = file.read(), valset = file.read();
		String[] valsplit = valset.split(" ");
		int k = Integer.parseInt(valsplit[0]), l = Integer.parseInt(valsplit[1]), t = Integer.parseInt(valsplit[2]);
		
		boolean[] exist = new boolean[1 << (k * 2)];
		int[] freqs = new int[1 << (k * 2)];
		
		// initial clump
		String clump = genome.substring(0, l);
		String kFirst, kLast; // queued first and last k-mer
		
		// frequency table initialization by initial clump counting
		for(int i = 0; i <= clump.length() - k; i++) {
			freqs[Src_BA1B.hash(clump.substring(i, i + k))]++;
		}

		// scan initial frequency table to determine clump k-mers
		for(int i = 0; i < freqs.length; i++) {
			if(freqs[i] >= t) {
				if(!exist[i]) {
					file.type(Src_BA1B.revhash(i, k) + " ");
				}
				exist[i] = true;
			}
		}
		
		// queue terminal strings
		kFirst = clump.substring(0, k);
		kLast = clump.substring(l - k, l);
		
		// iterate through whole genome, base by base
		for(int i = l; i < genome.length(); i++) {
			freqs[Src_BA1B.hash(kFirst)]--; // remove previously first k-mer frequency
			kFirst = genome.substring(i - l + 1, i - l + k + 1); // renew first k-mer
			
			kLast = genome.substring(i - k + 1, i + 1); // renew last k-mer
			int lhash = Src_BA1B.hash(kLast);
			freqs[lhash]++;
			
			if(freqs[lhash] >= t) {
				if(!exist[lhash]) {
					file.type(Src_BA1B.revhash(lhash, k) + " ");
				}
				exist[lhash] = true;
			}
		}

		file.write("");
		file.close();
		
		System.out.println((System.nanoTime() - iTime) / 1000000);
	}
}
