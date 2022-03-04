package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.FileIO;

public class Src_BA9K {
	public static int lastToFirst(String bwt, int last) {
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
		
		char lastCh = chars.get(last);
		int lastInd = charIndex.get(last);
		
		int first;
		for(first = 0; sortedChars.get(first) != lastCh || sortedCharIndex.get(first) != lastInd ; first++);
		
		return first;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'k');
		
		String bwt = file.read();
		int last = Integer.parseInt(file.read());
		
		file.writeObj(lastToFirst(bwt, last));
		file.close();
	}
}
