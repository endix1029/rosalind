package tbtrack.chapter3;

import tools.FileIO;

public class Src_BA3L {
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(3, 'l');
		
		String vstr = file.read();
		String[] vspl = vstr.split(" ");
		int k = Integer.parseInt(vspl[0]), d = Integer.parseInt(vspl[1]);
		
		vstr = file.read();
		vspl = vstr.split("\\|");
		String head = vspl[0], tail = vspl[1];
		while((vstr = file.read()) != null) {
			vspl = vstr.split("\\|");
			head += vspl[0].charAt(k - 1);
			tail += vspl[1].charAt(k - 1);
		}
		
		file.write(head + tail.substring(tail.length() - k - d));
		file.close();
	}
}
