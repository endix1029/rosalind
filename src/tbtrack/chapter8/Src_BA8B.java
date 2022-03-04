package tbtrack.chapter8;

import tools.*;
import java.util.*;

public class Src_BA8B {
	public static double distortion(List<double[]> points, List<double[]> centers) {
		double distort = .0;
		
		for(double[] point : points) {
			double cdist = Src_BA8A.centerDistance(point, centers);
			distort += Math.pow(cdist, 2);
		}
		
		return distort / points.size();
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(8, 'b');
		
		String vstr = file.read();
		int k = Integer.parseInt(vstr.split(" ")[0]), m = Integer.parseInt(vstr.split(" ")[1]);
		
		List<double[]> centers = new ArrayList<double[]>();
		for(int i = 0; i < k; i++) {
			vstr = file.read();
			double[] center = new double[m];
			for(int j = 0; j < m; j++) center[j] = Double.parseDouble(vstr.split(" ")[j]);
			centers.add(center);
		}
		
		file.read(); // -----
		
		List<double[]> points = new ArrayList<double[]>();
		while(file.isConsole() ? (vstr = file.read()).length() > 0 : (vstr = file.read()) != null) {
			double[] point = new double[m];
			for(int i = 0; i < m; i++) point[i] = Double.parseDouble(vstr.split(" ")[i]);
			points.add(point);
		}
		
		file.write(String.format("%.3f", distortion(points, centers)));
		file.close();
	}
}
