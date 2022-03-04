package tbtrack.chapter9;

import tools.*;
import java.util.*;
import structure.TrieNode;

public class Src_BA9C {
	public static void traverseMerge(TrieNode v) {
		v.mergedCargo = "" + v.cargo;
		if(v.isLeaf()) return;
		for(TrieNode child : v.getChildren()) traverseMerge(child);
		
		if(v.getChildren().size() == 1) {
			TrieNode child = v.getChildren().get(0);
			v.mergedCargo += child.mergedCargo;
			v.setChildren(child.getChildren());
			
			for(TrieNode newChild : v.getChildren()) newChild.setParent(v);
		}
	}
	
	private static void traversePrint(TrieNode v, FileIO file) throws java.io.IOException {
		file.write(v.mergedCargo);
		if(v.isLeaf()) return;
		for(TrieNode child : v.getChildren()) traversePrint(child, file);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'c');
		String text = file.read();
		
		List<String> suffixes = new ArrayList<String>();
		for(int i = 0; i < text.length(); i++) suffixes.add(text.substring(i));
		
		TrieNode suffixTrie = TrieNode.construct(suffixes);
		traverseMerge(suffixTrie);
		
		for(TrieNode child : suffixTrie.getChildren()) traversePrint(child, file);
		file.close();
	}
}
