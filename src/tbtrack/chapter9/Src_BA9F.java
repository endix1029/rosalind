package tbtrack.chapter9;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import structure.TrieNode;

public class Src_BA9F {
	private static String shortestExclusive(TrieNode node) {
		if(!node.xcont) return null;
		if(node.isLeaf()) return node.buildSuffix();
		
		String substr = null;
		if(!node.ycont) substr = node.buildSuffix();
		
		for(TrieNode child : node.getChildren()) {
			String childSubstr = shortestExclusive(child);
			if(childSubstr == null) continue;
			if(childSubstr.contains("#")) continue;
			
			if(substr == null) substr = childSubstr;
			else if(substr.length() > childSubstr.length()) substr = childSubstr;
		}
		
		return substr;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'f');
		
		String tx = file.read(), ty = file.read();
		String query = tx + "#" + ty + "$";
		
		List<String> suffixes = new ArrayList<String>();
		for(int i = 0; i < query.length(); i++) suffixes.add(query.substring(i));
		
		TrieNode suffixTree = TrieNode.constructTree(suffixes);
		Src_BA9E.modifyTree(suffixTree);
		Src_BA9E.colorTree(suffixTree);
		
		file.write(shortestExclusive(suffixTree));
		file.close();
	}
}
