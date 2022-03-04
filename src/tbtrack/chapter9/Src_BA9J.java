package tbtrack.chapter9;

import tools.FileIO;
import java.util.*;

public class Src_BA9J {
	public static String reverseBWT(String bwt) {
		List<Character> chars = new ArrayList<Character>();
		for(int i = 0; i < bwt.length(); i++) chars.add(bwt.charAt(i));

		List<Character> sortedChars = new ArrayList<Character>();
		sortedChars.addAll(chars);
		Collections.sort(sortedChars);
		
		List<Integer> charIndex = new ArrayList<Integer>();
		int[] charMap = new int[200];
		
		for(int i = 0; i < bwt.length(); i++) {
			charIndex.add(++charMap[chars.get(i)]);
		}
		
		List<Integer> sortedCharIndex = new ArrayList<Integer>();
		charMap = new int[200];
		
		for(int i = 0; i < bwt.length(); i++) {
			sortedCharIndex.add(++charMap[sortedChars.get(i)]);
		}
		
		String text = "";
		char ch = '$'; int index = 1;
		for(int i = 0; i < bwt.length(); i++) {
			text += ch;
			
			int loc = -1;
			for(int j = 0; j < bwt.length(); j++) {
				if(chars.get(j) == ch && charIndex.get(j) == index) {
					loc = j;
					break;
				}
			}
			
			ch = sortedChars.get(loc);
			index = sortedCharIndex.get(loc);
		}
		
		return text.substring(1) + "$";
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO();
		String bwt = file.read();
		
		file.write(reverseBWT(bwt));
		file.close();
	}
}
