package tbtrack.chapter9;

import tools.*;
import java.util.*;

class BurrowsWheeler {
	public String bwText; // Burrows-Wheeler text
	public String ogText; // Original text
	public int textLen; // Text length
	
	protected List<Character> bwChars; // Text character list
	protected List<Character> soChars; // Sorted character list
	
	private List<Integer> bwIndex; // Character index list
	private List<Integer> soIndex; // Sorted character index list
	
	private void buildList() {
		this.bwChars = new ArrayList<Character>();
		for(int i = 0; i < this.textLen; i++) {
			this.bwChars.add(this.bwText.charAt(i));
		}
		
		this.soChars = new ArrayList<Character>();
		this.soChars.addAll(this.bwChars);
		Collections.sort(this.soChars);
		
		int[] chMap;
		
		this.bwIndex = new ArrayList<Integer>();
		chMap = new int[200];
		for(int i = 0; i < this.textLen; i++) {
			this.bwIndex.add(++chMap[this.bwChars.get(i)]);
		}
		
		this.soIndex = new ArrayList<Integer>();
		chMap = new int[200];
		for(int i = 0; i < this.textLen; i++) {
			this.soIndex.add(++chMap[this.soChars.get(i)]);
		}
	}
	
	private String reverseBWT() {
		String text = "";
		char ch = '$'; int id = 1;
		for(int i = 0; i < this.textLen; i++) {
			text += ch;
			
			int loc = -1;
			for(int j = 0; j < this.textLen; j++) {
				if(this.bwChars.get(j) == ch && this.bwIndex.get(j) == id) {
					loc = j;
					break;
				}
			}
			
			ch = this.soChars.get(loc);
			id = this.soIndex.get(loc);
		}
		
		return text.substring(1) + "$";
	}
	
	private int lastFirst(int last) {
		char lastCh = this.bwChars.get(last);
		int  lastId = this.bwIndex.get(last);
		
		int first;
		for(first = 0; this.soChars.get(first) != lastCh || this.soIndex.get(first) != lastId ; first++);
		
		return first;
	}
	
	public int bwMatching(String pattern) {
		int top = 0, bot = this.textLen - 1;
		while(top <= bot) {
			for(int i = pattern.length() - 1; i >= 0; i--) {
				char ch = pattern.charAt(i);
				int ttop, tbot;
				for(ttop = top; this.bwChars.get(ttop) != ch && ttop < bot; ttop++);
				for(tbot = bot; this.bwChars.get(tbot) != ch && tbot > top; tbot--);
				
				if(this.bwChars.get(ttop) != ch | this.bwChars.get(tbot) != ch) return 0;
				
				top = lastFirst(ttop);
				bot = lastFirst(tbot);
			}
			return bot - top + 1;
		}
		
		return -1;
	}
	
	public BurrowsWheeler(String bwt) {
		this.bwText = bwt;
		this.textLen = bwt.length();
		this.buildList();
		
		this.ogText = this.reverseBWT();
	}
}

public class Src_BA9L {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'l');
		BurrowsWheeler bw = new BurrowsWheeler(file.read());
		
		String[] patterns = file.read().split(" ");
		for(String pattern : patterns) file.type(bw.bwMatching(pattern) + " ");
		file.write("");
		file.close();
	}
}
