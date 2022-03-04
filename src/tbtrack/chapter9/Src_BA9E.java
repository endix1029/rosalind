package tbtrack.chapter9;

import tools.*;
import java.util.*;
import structure.TrieNode;

public class Src_BA9E {
	public static void modifyTree(TrieNode node) {
		if(node.isLeaf()) {
			int loc;
			if((loc = node.mergedCargo.indexOf('#')) >= 0) {
				node.mergedCargo = node.mergedCargo.substring(0, loc + 1);
			}
			return;
		}
		else for(TrieNode child : node.getChildren()) modifyTree(child);
	}
	
	public static void colorTree(TrieNode node) {
		if(node.isLeaf()) {
			node.xcont = node.mergedCargo.contains("#");
			node.ycont = node.mergedCargo.contains("$");
		}
		else for(TrieNode child : node.getChildren()) colorTree(child);
		
		for(TrieNode child : node.getChildren()) {
			node.xcont |= child.xcont;
			node.ycont |= child.ycont;
		}
	}
	
	private static String longestSubstring(TrieNode node) {
		String substr = "";
		
		if(node.isLeaf()) return substr;
		if(node.getParent() != null) {
			if(node.xcont & node.ycont) substr = node.buildSuffix();
		}
		for(TrieNode child : node.getChildren()) {
			String childSubstr = longestSubstring(child);
			if(childSubstr.length() > substr.length()) substr = childSubstr;
		}
		
		return substr;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'e');
		
		String tx = file.read(), ty = file.read();
		String query = tx + "#" + ty + "$";
		
		List<String> suffixes = new ArrayList<String>();
		for(int i = 0; i < query.length(); i++) suffixes.add(query.substring(i));
		
		TrieNode suffixTree = TrieNode.constructTree(suffixes);
		modifyTree(suffixTree);
		colorTree(suffixTree);
		
		file.write(longestSubstring(suffixTree));
		file.close();
	}
}
