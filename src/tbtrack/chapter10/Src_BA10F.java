package tbtrack.chapter10;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.ProfileHMM;

public class Src_BA10F {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(10, 'f');
		String line = file.read();
		double theta = Double.parseDouble(line.split("\t")[0]), pseudo = Double.parseDouble(line.split("\t")[1]);
		
		file.read();
		ProfileHMM phmm = new ProfileHMM(file.read(), theta);
		file.read();
		
		List<String> alignment = new ArrayList<String>();
		while(file.isConsole() ? (line = file.read()).length() > 0 : (line = file.read()) != null) {
			alignment.add(line);
		}
		
		phmm.parseAlignment(alignment);
		phmm.applyPseudoCount(pseudo);
		
		// Transition header
		for(int i = 0; i < phmm.profileStateCount(); i++) {
			file.type("\t" + phmm.getProfileState(i));
		}
		file.write("");
		for(int i = 0; i < phmm.profileStateCount(); i++) {
			file.type(phmm.getProfileState(i));
			for(int j = 0; j < phmm.profileStateCount(); j++) {
				file.type(String.format("\t%.3f", phmm.getTransition(phmm.getProfileState(i), phmm.getProfileState(j))));
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
				file.type(String.format("\t%.3f", phmm.getEmission(phmm.getProfileState(i), phmm.getAlpha(j))));
			}
			file.write("");
		}
		
		file.close();
	}
}
