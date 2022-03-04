package structure;

public class Matrix {
	private int[][] matrix;
	private int r, c;
	private boolean isSquare;
	
	public Matrix(int x, int y) {
		this.matrix = new int[x][y];
		this.r = x;
		this.c = y;
		this.isSquare = r == c;
	}
	
	public void set(int x, int y, int val) {
		matrix[x][y] = val;
	}
	
	public int get(int x, int y) {
		return matrix[x][y];
	}
	
	public Matrix square() {
		if(!isSquare) return null;
		
		Matrix sqMatrix = new Matrix(r, r);
		for(int i = 0; i < r; i++) {
			for(int j = 0; j < r; j++) {
				int sum = 0;
				for(int k = 0; k < r; k++) {
					sum += matrix[i][k] * matrix[k][j];
				}
				sqMatrix.set(i, j, sum);
			}
		}
		return sqMatrix;
	}
	
	public int trace() {
		if(!isSquare) return 0;
		
		int sum = 0;
		for(int i = 0; i < r; i++) {
			sum += matrix[i][i];
		}
		return sum;
	}
}
