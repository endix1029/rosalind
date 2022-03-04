package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1A {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'a');
		
		String text, pattern;
		text = file.read();
		pattern = file.read();
		
		file.close();
		
		int count = 0;
		for(int i = 0; i < text.length() - pattern.length(); i++) {
			if(text.substring(i, i + pattern.length()).equals(pattern)) count++;
		}
		
		System.out.println(count);
	}
}
