package tbtrack.chapter9;

import java.util.*;
import tools.*;
import structure.TrieNode;

public class Src_BA9P {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(9, 'p');
		List<TrieNode> nodeList = new ArrayList<TrieNode>();
		
		String line;
		List<String> queries = new ArrayList<String>();
		while((line = file.read()).length() > 1) queries.add(line);
		
		// process leaf nodes
		boolean[] mark = new boolean[queries.size()]; int i = 0;
		for(String query : queries) {
			if(query.contains("{")) {
				mark[i] = true;
				int n = Integer.parseInt(query.split(" -> ")[0]), nsize = nodeList.size();
				if(nsize <= n) for(int j = 0; j <= n - nsize; j++) nodeList.add(null);
				
				nodeList.remove(n);
				nodeList.add(n, new TrieNode('*'));
			}
			i++;
		}
		
		for(--i; i >= 0; i--) if(mark[i]) queries.remove(i);
		
		// assign colors
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			int n = Integer.parseInt(line.split(":")[0]);
			boolean isRed = line.split(": ")[1].equals("red");
			
			if(isRed) nodeList.get(n).xcont = true;
			else      nodeList.get(n).ycont = true;
		}
		
		// process ripe queries one at a time
		while(!queries.isEmpty()) {
			int[] childs = null;
			
			for(i = 0; i < queries.size(); i++) {
				String[] cstrs = queries.get(i).split(" -> ")[1].split(",");
				childs = new int[cstrs.length];
				
				boolean notRipe = false;
				for(int j = 0; j < cstrs.length; j++) {
					childs[j] = Integer.parseInt(cstrs[j]);
					if(nodeList.get(childs[j]) == null) {
						notRipe = true; break;
					}
				}
				if(notRipe) continue;
				
				break;
			}
			
			String query = queries.get(i);
			queries.remove(i);
			
			int n = Integer.parseInt(query.split(" -> ")[0]), nsize = nodeList.size();
			if(nsize <= n) for(int j = 0; j <= n - nsize; j++) nodeList.add(null);
			
			nodeList.remove(n);
			nodeList.add(n, new TrieNode('*'));
			
			boolean px = false, py = false;
			for(int ch : childs) {
				px |= nodeList.get(ch).xcont;
				py |= nodeList.get(ch).ycont;
			}
			
			nodeList.get(n).xcont = px;
			nodeList.get(n).ycont = py;
		}
		
		i = 0;
		for(TrieNode node : nodeList) {
			file.type(i++ + ": ");
			if(node.xcont & node.ycont) file.write("purple");
			else if(node.xcont) file.write("red");
			else if(node.ycont) file.write("blue"); 
		}
		
		file.close();
	}
}
