package tbtrack.chapter6;

import tools.*;

public class Src_BA6E {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(6, 'e');
		
		int k = Integer.parseInt(file.read());
		String seqx = file.read(), seqy = file.read();
		String revy = Functions.seq_revcomp(seqy);
		
		for(int i = 0; i <= seqx.length() - k; i++) {
			String kx = seqx.substring(i, i + k);
			System.out.println(String.format("%d / %d", i, seqx.length() - k));
			
			for(int j = 0; j <= seqy.length() - k; j++) {
				String ky = seqy.substring(j, j + k);
				String kry = revy.substring(j, j + k);
				if(kx.equals(ky)) file.write(String.format("(%d, %d)", i, j));
				if(kx.equals(kry)) file.write(String.format("(%d, %d)", i, seqy.length() - k - j));
			}
		}
		
		file.close();
	}
}
