package tbtrack.chapter11;

import tools.*;

/*
 MassSpectrumGraph.paths(proteome)
	public List<String> paths(String proteome) {
		List<List<String>> plist = new ArrayList<List<String>>();
		
		for(int i = 0; i < spec.size() - 2; i++){
			plist.add(null);
		}
		List<String> tmp = new ArrayList<String>();
		tmp.add("");
		plist.add(tmp);
		
		for(int i = spec.size() - 2; i >= 0; i--) {
			List<String> ps = new ArrayList<String>();
			for(int j = i + 1; j < spec.size(); j++) {
				if(plist.get(j) == null) continue;
				if(edges[i][j] != '*') {
					for(String subpath : plist.get(j)) {
						if(proteome.contains(edges[i][j] + subpath)) {
							ps.add(edges[i][j] + subpath);
						}
						if(edges[i][j] == 'Q') {
							if(proteome.contains('K' + subpath)) {
								ps.add('K' + subpath);
							}
						}
						if(edges[i][j] == 'L') {
							if(proteome.contains('I' + subpath)) {
								ps.add('I' + subpath);
							}
						}
					}
				}
			}
			
			if(ps.size() > 0) {
				plist.remove(i);
				plist.add(i, ps);
			}
		}
		
		return plist.get(0);
	}
 */

public class Src_BA11F {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'f');
		String[] mspl = file.read().split(" ");
		String proteome = file.read();
		
		int[] mlist = new int[mspl.length + 1];
		mlist[0] = 0;
		for(int i = 0; i < mspl.length; i++) {
			mlist[i + 1] = Integer.parseInt(mspl[i]);
		}
		
		MassSpectrumGraph msg = new MassSpectrumGraph(mlist);
		
		int maxScore = Integer.MIN_VALUE;
		String maxPath = null;		
		for(String path : msg.paths(proteome)) {
			int score = msg.score(path);
			if(score > maxScore) {
				maxScore = score;
				maxPath = path;
			}
		}
		
		file.write(maxPath);
		file.close();
	}
}
