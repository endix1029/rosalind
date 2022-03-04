package tbtrack.chapter8;

import tools.*;
import java.util.*;

public class Src_BA8E {
	private static int N;
	
	private static double[][] hcluster(double[][] distMatrix, List<List<Integer>> metaClusters, FileIO file) throws java.io.IOException {
		int minx = -1, miny = -1;
		double minDist = Double.MAX_VALUE;
		
		for(int i = 0; i < N; i++) {
			for(int j = i + 1; j < N; j++) {
				if(minDist > distMatrix[i][j]) {
					minDist = distMatrix[i][j];
					minx = i; miny = j;
				}
			}
		}
		
		// merge via metadata
		List<Integer> newCluster = new ArrayList<Integer>();
		newCluster.addAll(metaClusters.get(minx));
		newCluster.addAll(metaClusters.get(miny));
		int sizex = metaClusters.get(minx).size(), sizey = metaClusters.get(miny).size();
		
		metaClusters.remove(miny);
		metaClusters.remove(minx);
		metaClusters.add(newCluster);
		
		double[][] newDistMatrix = new double[N - 1][N - 1];
		int ti = 0, tj = 0;
		for(int i = 0; i < N; i++) {
			if(i == minx || i == miny) continue;
			tj = 0;
			for(int j = 0; j < N; j++) {
				if(j == minx || j == miny) continue;
				newDistMatrix[ti][tj++] = distMatrix[i][j];
			}
			ti++;
		}
		
		ti = 0;
		for(int i = 0; i < N; i++) {
			if(i == minx || i == miny) continue;
			newDistMatrix[ti][N - 2] = (distMatrix[minx][i] * sizex + distMatrix[miny][i] * sizey) / (sizex + sizey);
			newDistMatrix[N - 2][ti] = (distMatrix[i][minx] * sizex + distMatrix[i][miny] * sizey) / (sizex + sizey);
			ti++;
		}
		
		for(int merged : newCluster) {
			file.type((merged + 1) + " ");
		}
		file.write("");
		
		N--;
		return newDistMatrix;
	}
	
	public static void main(String[] args) throws java.io.IOException {
		FileIO file = new FileIO(8, 'e');
		N = Integer.parseInt(file.read());
		
		double[][] distMatrix = new double[N][N];
		List<List<Integer>> metaClusters = new ArrayList<List<Integer>>();
		
		for(int i = 0; i < N; i++) {
			String[] vspl = file.read().split(" ");
			
			for(int j = 0; j < N; j++) distMatrix[i][j] = Double.parseDouble(vspl[j]);
			
			List<Integer> metaCluster = new ArrayList<Integer>();
			metaCluster.add(i);
			metaClusters.add(metaCluster);
		}
		
		for(int i = N; i > 1; i--) distMatrix = hcluster(distMatrix, metaClusters, file);
		file.close();
	}
}
