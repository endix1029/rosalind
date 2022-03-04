package tbtrack.chapter8;

import tools.*;
import java.util.*;

public class Src_BA8A {
	public static double centerDistance(double[] point, List<double[]> centers) {
		double mineuc = Double.MAX_VALUE;
		for(double[] center : centers) {
			double euc = Functions.euclideanDouble(point, center);
			if(mineuc > euc) mineuc = euc;
		}
		
		return mineuc;
	}
	
	public static double[] farthestPoint(List<double[]> points, List<double[]> centers) {
		double maxeuc = .0;
		double[] maxpoint = null;
		
		for(double[] point : points) {
			double euc = centerDistance(point, centers);
			if(euc > maxeuc) {
				maxeuc = euc;
				maxpoint = point;
			}
		}
		
		return maxpoint;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(8, 'a');
		
		String vstr = file.read();
		int k = Integer.parseInt(vstr.split(" ")[0]), m = Integer.parseInt(vstr.split(" ")[1]);
		
		List<double[]> points = new ArrayList<double[]>();
		while(file.isConsole() ? (vstr = file.read()).length() > 0 : (vstr = file.read()) != null) {
			double[] point = new double[m];
			for(int i = 0; i < m; i++) point[i] = Double.parseDouble(vstr.split(" ")[i]);
			points.add(point);
		}
		
		List<double[]> centers = new ArrayList<double[]>();
		centers.add(points.get(0));
		
		while(centers.size() < k) centers.add(farthestPoint(points, centers));
		for(double[] center : centers) {
			for(double coord : center) {
				file.type(coord + " ");
			}
			file.write("");
		}
		
		file.close();
	}
}
