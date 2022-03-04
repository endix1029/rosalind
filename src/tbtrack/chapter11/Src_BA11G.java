package tbtrack.chapter11;

import tools.*;
import java.util.*;

public class Src_BA11G {
	private static boolean VERBOSE = false;
	
	public static List<String> psmSearch(List<int[]> specs, String proteome, int threshold) {
		List<String> psm = new ArrayList<String>();
		System.out.println("[PSMSearch] : Initializing");
		
		int i = 0;
		for(int[] spec : specs) {
			MassSpectrumGraph msg = new MassSpectrumGraph(spec);
			int pcnt = 0;
			
			int maxScore = Integer.MIN_VALUE;
			String maxPath = null;
			for(String path : msg.paths(proteome)) {
				int score = msg.score(path);
				if(score >= threshold) {
					pcnt++;
					if(VERBOSE) {
						System.out.println(String.format("[PSMSearch] : PSM found (SCORE = %d; SEQ = %s)", score, path)); 
					}
					if(score > maxScore) {
						maxScore = score;
						maxPath = path;
						if(VERBOSE) {
							System.out.println(String.format("[PSMSearch] : Maximum PSM renewed (%s)", path));
						}
					}
				}
			}
			
			if(maxPath != null) psm.add(maxPath);
			System.out.println(String.format("[PSMSearch] : Spectrum analyzed; %d PSM found. Progress : %d/%d", pcnt, ++i, specs.size()));
		}
		
		return psm;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(11, 'g');
		String line = file.read();
		
		List<int[]> specs = new ArrayList<int[]>();
		while(line.contains(" ")) {
			String[] mspl = line.split(" ");
			
			int[] mlist = new int[mspl.length + 1];
			mlist[0] = 0;
			for(int i = 0; i < mspl.length; i++) {
				mlist[i + 1] = Integer.parseInt(mspl[i]);
			}
			specs.add(mlist);
			
			line = file.read();
		}
		
		String proteome = line;
		int threshold = Integer.parseInt(file.read());
		
		for(String psm : psmSearch(specs, proteome, threshold)) {
			file.write(psm);
		}
		file.close();
	}
}

