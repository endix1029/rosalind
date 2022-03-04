package structure;

import java.util.ArrayList;
import java.util.List;

import tbtrack.chapter9.Src_BA9A;
import tbtrack.chapter9.Src_BA9C;

public class TrieNode {
	public char cargo;
	public int uid;
	public String mergedCargo;
	
	public boolean xcont, ycont; // for combined tree
	public char left; // to find maximal repeat
	
	private TrieNode parent;
	private List<TrieNode> edges;
	
	public TrieNode(char ch) {
		this.parent = null;
		this.cargo = ch;
		this.uid = Src_BA9A.UID++;
		this.edges = new ArrayList<TrieNode>();
	}
	
	public TrieNode(TrieNode pnode, char ch) {
		this.parent = pnode;
		this.cargo = ch;
		this.uid = Src_BA9A.UID++;
		this.edges = new ArrayList<TrieNode>();
	}
	
	public boolean isLeaf() {return edges.size() < 1;}
	public TrieNode getParent() {return this.parent;}
	public void setParent(TrieNode pnode) {this.parent = pnode;}
	public List<TrieNode> getChildren() {return this.edges;}
	public void setChildren(List<TrieNode> newChildren) {this.edges = newChildren;}
	
	public TrieNode childWith(char ch) {
		for(TrieNode child : edges) {
			if(child.cargo == ch) return child;
		}
		return null;
	}
	
	public TrieNode adopt(char ch) {
		TrieNode child = new TrieNode(this, ch);
		this.edges.add(child);
		return child;
	}
	
	public void adoptNode(TrieNode node) {this.edges.add(node);}
	
	public static TrieNode construct(List<String> queries) {
		TrieNode root = new TrieNode('*');
		
		for(String query : queries) {
			TrieNode current = root;
			for(int i = 0; i < query.length(); i++) {
				char ch = query.charAt(i);
				
				TrieNode child;
				if((child = current.childWith(ch)) != null) current = child;
				else {
					child = current.adopt(ch);
					current = child;
				}
			}
		}
		
		return root;
	}
	
	public static TrieNode constructTree(List<String> queries) {
		TrieNode root = construct(queries);
		Src_BA9C.traverseMerge(root);
		return root;
	}
	
	public int leafCount() {
		if(this.isLeaf()) return 1;
		
		int lsum = 0;
		for(TrieNode child : this.edges) lsum += child.leafCount();
		return lsum;
	}
	
	public int nodeCount() {
		int nsum = 1;
		for(TrieNode child : this.edges) nsum += child.nodeCount();
		return nsum;
	}
	
	public String buildSuffix() {
		if(this.parent == null) return "";
		return this.parent.buildSuffix() + this.mergedCargo;
	}
}