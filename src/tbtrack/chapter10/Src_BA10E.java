package tbtrack.chapter10;

import tools.*;
import java.util.*;

public class Src_BA10E {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'e');
		double theta = Double.parseDouble(file.read());
		
		file.read();
		ProfileHMM phmm = new ProfileHMM(file.read(), theta);
		file.read();
		
		List<String> alignment = new ArrayList<String>();
		String line;
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			alignment.add(line);
		}
		
		phmm.parseAlignment(alignment);
		
		// Transition header
		for(int i = 0; i < phmm.profileStateCount(); i++) {
			file.type("\t" + phmm.getProfileState(i));
		}
		file.write("");
		for(int i = 0; i < phmm.profileStateCount(); i++) {
			file.type(phmm.getProfileState(i));
			for(int j = 0; j < phmm.profileStateCount(); j++) {
				file.type("\t" + phmm.getTransition(phmm.getProfileState(i), phmm.getProfileState(j)));
			}
			file.write("");
		}
		
		file.write("--------");
		
		// Emission header
		for(int i = 0; i < phmm.sigmaCount(); i++) {
			file.type("\t" + phmm.getAlpha(i));
		}
		file.write("");
		for(int i = 0; i < phmm.profileStateCount(); i++) {
			file.type(phmm.getProfileState(i));
			for(int j = 0; j < phmm.sigmaCount(); j++) {
				file.type("\t" + phmm.getEmission(phmm.getProfileState(i), phmm.getAlpha(j)));
			}
			file.write("");
		}
		
		file.close();
	}
}
