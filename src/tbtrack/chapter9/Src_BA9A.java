package tbtrack.chapter9;

import java.util.*;
import tools.*;
import structure.TrieNode;

public class Src_BA9A {
	public static int UID = 0;
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'a');
		
		List<String> queries = new ArrayList<String>();
		
		String line;
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			queries.add(line);
		}
		
		TrieNode root = new TrieNode('*');
		for(String query : queries) {
			TrieNode current = root;
			for(int i = 0; i < query.length(); i++) {
				char ch = query.charAt(i);
				
				TrieNode child;
				if((child = current.childWith(ch)) != null) current = child;
				else {
					child = current.adopt(ch);
					file.write(String.format("%d->%d:%c", child.getParent().uid, child.uid, ch));
					current = child;
				}
			}
		}
		
		file.close();
	}
}
