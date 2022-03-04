package tbtrack.chapter1;

import tools.FileIO;

public class Src_BA1H {
	/**
	 * Returns hamming distance between two String objects.
	 * @param ref Reference string
	 * @param qry Query string
	 * @return HammDist(ref, qry)
	 */
	public static int hamd(String ref, String qry) {
		if(ref.length() != qry.length()) return -1;
		
		int ham = 0;
		for(int i = 0; i < ref.length(); i++) {
			if(ref.charAt(i) != qry.charAt(i)) ham++;
		}
		return ham;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(1, 'h');
		
		String pattern = file.read(), text = file.read();
		int d = Integer.parseInt(file.read());
		
		int lenp = pattern.length(), lent = text.length();
		
		for(int i = lenp; i < lent; i++) {
			if(hamd(pattern, text.substring(i - lenp, i)) <= d) {
				file.type(String.valueOf(i - lenp) + " ");
			}
		}
		
		file.write("");
		file.close();
	}

}
