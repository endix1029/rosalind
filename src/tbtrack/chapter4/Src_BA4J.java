package tbtrack.chapter4;

import tools.*;
import java.util.*;

public class Src_BA4J {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(4, 'j');
		List<Integer> spectrum = Functions.linearSpectrum(file.read());
		for(int spec : spectrum) file.type(spec + " ");
		file.write("");
		file.close();
	}
}
