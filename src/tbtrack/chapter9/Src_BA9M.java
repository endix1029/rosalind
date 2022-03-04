package tbtrack.chapter9;

import java.util.*;
import tools.*;

class BetterBurrowsWheeler extends BurrowsWheeler {
	private List<Character> occurrence;
	private int[] firstOccurrence;
	private int[][] count;
	
	private void buildCount() {
		this.occurrence = new ArrayList<Character>();
		this.firstOccurrence = new int[200];
		this.count = new int[200][super.textLen + 1];
		
		for(int i = 1; i <= super.textLen; i++) {
			// count array update
			for(char occur : occurrence) {
				this.count[occur][i] = this.count[occur][i - 1];
			}
			
			// character count
			char ch = super.bwText.charAt(i - 1);
			this.count[ch][i]++;
			if(!this.occurrence.contains(ch)) {
				this.occurrence.add(ch);
			}
			
			// first occurrence check
			ch = super.soChars.get(i - 1);
			if(this.firstOccurrence[ch] == 0) this.firstOccurrence[ch] = i - 1;
		}
	}
	
	public BetterBurrowsWheeler(String bwt) {
		super(bwt);
		this.buildCount();
	}
	
	public int betterBWMatching(String pattern) {
		int top = 0, bot = super.textLen - 1;
		for(int i = pattern.length() - 1; i >= 0; i--) {
			char ch = pattern.charAt(i);
			top = this.firstOccurrence[ch] + this.count[ch][top];
			bot = this.firstOccurrence[ch] + this.count[ch][bot + 1] - 1;
		}
		
		return bot - top + 1;
	}
	
	public int[] bwMatchingInterval(String pattern) {
		int top = 0, bot = super.textLen - 1;
		for(int i = pattern.length() - 1; i >= 0; i--) {
			char ch = pattern.charAt(i);
			top = this.firstOccurrence[ch] + this.count[ch][top];
			bot = this.firstOccurrence[ch] + this.count[ch][bot + 1] - 1;
		}
		
		if(bot < top) return null;
		
		int[] interval = new int[bot - top + 1];
		for(int i = top; i <= bot; i++) interval[i - top] = i;
		return interval;
	}
}

public class Src_BA9M {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'm');
		String btw = file.read();
		
		BetterBurrowsWheeler bbw = new BetterBurrowsWheeler(btw);
		
		String[] patterns = file.read().split(" ");
		for(String pattern : patterns) file.type(bbw.betterBWMatching(pattern) + " ");
		file.write("");
		file.close();
	}
}
