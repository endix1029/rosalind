package tbtrack.chapter9;

import tools.*;
import java.util.*;
import structure.TrieNode;

public class Src_BA9D {
	private static String longestRepeat(TrieNode node) {
		String repeat = "";
		
		if(node.isLeaf()) return repeat;
		if(node.getParent() != null) {
			if(node.leafCount() > 1) repeat = node.buildSuffix();
		}
		for(TrieNode child : node.getChildren()) {
			String childRepeat = longestRepeat(child);
			if(childRepeat.length() > repeat.length()) repeat = childRepeat;
		}
		
		return repeat;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'd');
		String text = file.read() + "$";
		
		List<String> suffixes = new ArrayList<String>();
		for(int i = 0; i < text.length(); i++) suffixes.add(text.substring(i));
		
		TrieNode suffixTree = TrieNode.constructTree(suffixes);
		file.write(longestRepeat(suffixTree));
		file.close();
	}
}
