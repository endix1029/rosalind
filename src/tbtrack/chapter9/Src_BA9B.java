package tbtrack.chapter9;

import tools.*;
import java.util.*;
import structure.TrieNode;

public class Src_BA9B {
	private static boolean prefixMatch(String text, TrieNode trie) {
		text += '$';
		int loc = 0;
		char ch = text.charAt(loc++);
		TrieNode v = trie;
		
		while(!v.isLeaf()) {
			if(v.childWith(ch) != null) {
				v = v.childWith(ch);
				ch = text.charAt(loc++);
			}
			else return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'b');
		String text = file.read();
		
		List<String> queries = new ArrayList<String>();
		String line;
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			queries.add(line);
		}
		
		TrieNode trie = TrieNode.construct(queries);
		for(int i = 0; i < text.length(); i++) {
			if(prefixMatch(text.substring(i), trie)) file.type(i + " ");
		}
		
		file.write("");
		file.close();
	}
}
