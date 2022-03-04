package tbtrack.chapter11;

import tools.*;

public class Src_BA11A {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'a');
		String[] mspl = file.read().split(" ");
		
		int[] mlist = new int[mspl.length + 1];
		mlist[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			mlist[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		Peptide pep = new Peptide();
		for(int i = 0; i < mlist.length - 1; i++) {
			for(int j = i + 1; j < mlist.length; j++) {
				char aa = pep.getAcid(mlist[j] - mlist[i]);
				if(aa != '*') {
					file.write(String.format("%d->%d:%c", mlist[i], mlist[j], aa));
				}
			}
		}
		
		file.close();
	}
}
