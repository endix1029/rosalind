package tbtrack.chapter5;

import tools.*;

public class Src_BA5K {
	private static String pepx, pepy;
	private static int indel = 5, mid;
	private static Blosum blosum = null;
	
	private static int[] fromSource() {
		int loc = 0;
		int[] prev = new int[pepx.length() + 1], next = new int[pepx.length() + 1];
		for(int i = 1; i <= pepx.length(); i++) prev[i] = -indel * i;
		
		while(loc++ < mid) {
			next[0] = -indel * loc;
			for(int i = 1; i <= pepx.length(); i++) {
				next[i] = next[i - 1] - indel;
				if(prev[i] - indel > next[i]) next[i] = prev[i] - indel;
				if(prev[i - 1] + blosum.get(pepx.charAt(i - 1), pepy.charAt(loc - 1)) > next[i])
					next[i] = prev[i - 1] + blosum.get(pepx.charAt(i - 1), pepy.charAt(loc - 1));
			}
			
			for(int i = 0; i <= pepx.length(); i++) prev[i] = next[i];
		}
		
		return next;
	}
	
	private static int[] trackMid;
	private static int[] toSink() {
		int loc = pepy.length();
		int[] prev = new int[pepx.length() + 1], next = new int[pepx.length() + 1];
		for(int i = 1; i <= pepx.length(); i++) prev[pepx.length() - i] = -indel * i;
		
		while(loc-- > mid) {
			next[pepx.length()] = -indel * (pepy.length() - loc);
			for(int i = pepx.length() - 1; i >= 0; i--) {
				next[i] = next[i + 1] - indel;
				trackMid[i] = 1;
				
				if(prev[i] - indel > next[i]) {
					next[i] = prev[i] - indel;
					trackMid[i] = 2;
				}
				if(prev[i + 1] + blosum.get(pepx.charAt(i), pepy.charAt(loc)) > next[i]) {
					next[i] = prev[i + 1] + blosum.get(pepx.charAt(i), pepy.charAt(loc));
					trackMid[i] = 3;
				}
			}
			
			for(int i = 0; i <= pepx.length(); i++) prev[i] = next[i];
		}
		
		return next;
	}
	
	// returns [midNode, edgeDirection] of given strings
	public static int[] midEdge(String x, String y, Blosum bs, int top, int bot, int lft, int rgt){
		pepx = x.substring(top, bot);
		pepy = y.substring(lft, rgt);
		mid = pepy.length() / 2;
		blosum = bs;
		trackMid = new int[pepx.length() + 1];
		
		int[] fsource = fromSource();
		int[] tsink = toSink();
		
		int mScore = -(1<<20), mLoc = -1;
		for(int i = 0; i <= pepx.length(); i++) {
			if(mScore < fsource[i] + tsink[i]) {
				mScore = fsource[i] + tsink[i];
				mLoc = i;
			}
		}
		
		int[] ret = {top + mLoc, trackMid[mLoc]};
		return ret;
	}
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'k');
		
		blosum = new Blosum('B');
		pepx = file.read();
		pepy = file.read();
		mid = pepy.length() / 2;
		trackMid = new int[pepx.length() + 1];
		
		int[] fsource = fromSource();
		int[] tsink = toSink();
		
		int mScore = -1, mLoc = -1;
		for(int i = 0; i <= pepx.length(); i++) {
			if(mScore < fsource[i] + tsink[i]) {
				mScore = fsource[i] + tsink[i];
				mLoc = i;
			}
		}
		
		file.type(String.format("(%d, %d) ", mLoc, mid));
		if(trackMid[mLoc] == 1) file.write(String.format("(%d, %d)", mLoc, mid + 1));
		if(trackMid[mLoc] == 2) file.write(String.format("(%d, %d)", mLoc + 1, mid));
		if(trackMid[mLoc] == 3) file.write(String.format("(%d, %d)", mLoc + 1, mid + 1));
		
		file.close();
	}
}
