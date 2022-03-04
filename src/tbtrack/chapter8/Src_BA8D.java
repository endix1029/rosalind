package tbtrack.chapter8;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.Functions;

public class Src_BA8D {
	private static int K, M, N;
	private static double ß; // stiffness
	
	private static List<double[]> eStep(List<double[]> points, List<double[]> centers){
		List<double[]> hiddenMatrix = new ArrayList<double[]>();
		
		for(int i = 0; i < K; i++) {
			double[] row = new double[N];
			
			for(int j = 0; j < N; j++) {
				double sum = .0;
				for(int k = 0; k < K; k++) {
					sum += Math.exp(-ß * Functions.euclideanDouble(points.get(j), centers.get(k)));
				}
				row[j] = Math.exp(-ß * Functions.euclideanDouble(points.get(j), centers.get(i))) / sum;
			}		
			hiddenMatrix.add(row);
		}
		
		return hiddenMatrix;
	}
	
	private static List<double[]> mStep(List<double[]> points, List<double[]> hiddenMatrix){
		List<double[]> vertVectors = new ArrayList<double[]>();
		for(int i = 0; i < M; i++) {
			double[] vertVector = new double[N];
			for(int j = 0; j < N; j++) vertVector[j] = points.get(j)[i];
			vertVectors.add(vertVector);
		}
		
		List<double[]> newCenters = new ArrayList<double[]>();
		for(int i = 0; i < K; i++) {
			double[] centerVector = new double[M];
			for(int j = 0; j < M; j++) {
				centerVector[j] = Functions.dotProduct(hiddenMatrix.get(i), vertVectors.get(j)) / 
						Functions.dotProduct(hiddenMatrix.get(i), Functions.oneVector(N));
			}
			newCenters.add(centerVector);
		}
		
		return newCenters;
	}
	
	public static List<double[]> softClustering(List<double[]> points, List<double[]> centers) {
		List<double[]> hiddenMatrix = eStep(points, centers);
		return mStep(points, hiddenMatrix);
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(8, 'd');
		
		String vstr = file.read();
		K = Integer.parseInt(vstr.split(" ")[0]);
		M = Integer.parseInt(vstr.split(" ")[1]);
		
		ß = Double.parseDouble(file.read());
		
		List<double[]> points = new ArrayList<double[]>();
		while(file.isConsole() ? (vstr = file.read()).length() > 0 : (vstr = file.read()) != null) {
			double[] point = new double[M];
			for(int i = 0; i < M; i++) point[i] = Double.parseDouble(vstr.split(" ")[i]);
			points.add(point);
		}
		N = points.size();
		
		List<double[]> centers = new ArrayList<double[]>();
		for(int i = 0; i < K; i++) centers.add(points.get(i));
		
		for(int i = 0; i < 100; i++) centers = softClustering(points, centers);
		
		for(double[] center : centers) {
			for(double coord : center) {
				file.type(String.format("%.3f ", coord));
			}
			file.write("");
		}
		
		file.close();
	}
}
