package tbtrack.chapter5;

import tools.*;
import java.util.*;

public class Src_BA5D {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(5, 'd');
		int source = Integer.parseInt(file.read()), sink = Integer.parseInt(file.read());
		int maxv = 0;
		
		List<String> lines = new ArrayList<String>();
		String line;
		while((line = file.read()) != null) {
			int src = Integer.parseInt(line.split("->")[0]);
			int dst = Integer.parseInt(line.split("->")[1].split(":")[0]);
			
			if(src > maxv) maxv = src;
			if(dst > maxv) maxv = dst;
			lines.add(line);
		}
		
		int[][] adj = new int[maxv + 1][maxv + 1];
		for(String ln : lines) {
			int src = Integer.parseInt(ln.split("->")[0]);
			int dst = Integer.parseInt(ln.split("->")[1].split(":")[0]);
			int wgh = Integer.parseInt(ln.split("->")[1].split(":")[1]);
			
			adj[src][dst] = wgh;
		}
		
		int[] longest = new int[maxv + 1];
		int[] previous = new int[maxv + 1];
		for(int node = 0; node <= maxv; node++) {
			longest[node] = -1;
			previous[node] = -1;
		}
		longest[source] = 0;
		previous[source] = source;
		
		for(int rep = 0; rep < maxv; rep++) {
		for(int node = 0; node <= maxv; node++) {
			int maxlen = longest[node], maxprev = previous[node];
			
			for(int src = 0; src <= maxv; src++) {
				if(adj[src][node] > 0 && previous[src] >= 0) {
					int len = longest[src] + adj[src][node];
					if(len > maxlen) {
						maxlen = len;
						maxprev = src;
					}
				}
			}
			
			longest[node] = maxlen;
			previous[node] = maxprev;
		}
		}
		
		file.writeObj(longest[sink]);
		
		int node = sink;
		String path = String.valueOf(sink);
		while(node != source) {
			node = previous[node];
			path = String.valueOf(node) + "->" + path;
		}
		
		file.write(path);
		file.close();
	}
}
