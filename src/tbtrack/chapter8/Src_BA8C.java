package tbtrack.chapter8;

import java.util.ArrayList;
import java.util.List;

import tools.FileIO;
import tools.Functions;

public class Src_BA8C {
	private static int K, M;
	
	public static List<double[]> lloyd(List<double[]> points, List<Integer> clusterNumbers) {
		int[] count = new int[K];
		double[][] coordSums = new double[K][M];
		
		for(int i = 0; i < points.size(); i++) {
			int clusterNumber = clusterNumbers.get(i);
			
			count[clusterNumber]++;
			for(int j = 0; j < M; j++) {
				coordSums[clusterNumber][j] += points.get(i)[j];
			}
		}
		
		List<double[]> newCentroids = new ArrayList<double[]>();
		for(int i = 0; i < K; i++) {
			double[] centroid = new double[M];
			for(int j = 0; j < M; j++) {
				centroid[j] = coordSums[i][j] / count[i];
			}
			newCentroids.add(centroid);
		}
		
		return newCentroids;
	}
	
	private static int assignCluster(double[] point, List<double[]> centroids) {
		double minDist = Double.MAX_VALUE;
		int minCluster = -1;
		
		for(int i = 0; i < K; i++) {
			double dist = Functions.euclideanDouble(point, centroids.get(i));
			if(dist < minDist) {
				minDist = dist;
				minCluster = i;
			}
		}
		
		return minCluster;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(8, 'c');
		
		String vstr = file.read();
		K = Integer.parseInt(vstr.split(" ")[0]);
		M = Integer.parseInt(vstr.split(" ")[1]);
		
		List<double[]> points = new ArrayList<double[]>();
		while(file.isConsole() ? (vstr = file.read()).length() > 0 : (vstr = file.read()) != null) {
			double[] point = new double[M];
			for(int i = 0; i < M; i++) point[i] = Double.parseDouble(vstr.split(" ")[i]);
			points.add(point);
		}
		
		List<double[]> centroids = new ArrayList<double[]>();
		for(int i = 0; i < K; i++) centroids.add(points.get(i));
		
		List<Integer> clusterNumbers= new ArrayList<Integer>();
		for(double[] point : points) {
			clusterNumbers.add(assignCluster(point, centroids));
		}
		
		while(true) {
			centroids = lloyd(points, clusterNumbers);
			
			List<Integer> newClusterNumbers = new ArrayList<Integer>();
			for(double[] point : points) {
				newClusterNumbers.add(assignCluster(point, centroids));
			}
			
			boolean flag = false;
			for(int i = 0; i < clusterNumbers.size(); i++) {
				flag = false;
				if(clusterNumbers.get(i) != newClusterNumbers.get(i)) break;
				flag = true;
			}
			
			if(flag) break;
			clusterNumbers.clear();
			clusterNumbers.addAll(newClusterNumbers);
		}
		
		for(double[] centroid : centroids) {
			for(double coord : centroid) {
				file.type(String.format("%.3f ", coord));
			}
			file.write("");
		}
		
		file.close();
	}
}
