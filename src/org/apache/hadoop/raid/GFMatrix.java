package org.apache.hadoop.raid;

/***
 * A very basic Matrix class that offers support to 
 * some operations over a finite field. Heavily based on 
 * http://introcs.cs.princeton.edu/java/22library/Matrix.java.html
 * 
 * All the operations here defined return integer arrays by default.
 * This decision was taken to keep consistency with Hadoop's byte 
 * representation.
 * 
 * These implementations are not optimal. They are based on the most 
 * naive algorithms to perform arithmetic over matrices. Please,
 * do not use for critical tasks. It is intended just for our research
 * project.
 * 
 * @author pabloserrano
 *
 */
public class GFMatrix {
	
    private static GaloisField GF = GaloisField.getInstance();

    // return n-by-n identity matrix I
    public static int[][] identity(int n) {
        int[][] I = new int[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1;
        return I;
    }
    
    // return a random m-by-n matrix with values between 0 and field size
    public static int[][] random(int m, int n) {
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = (int) (Math.random() * (GF.getFieldSize() - 1));
        return C;
    }
    
    // return C = A + B
    public static int[][] add(int[][] A, int[][] B) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
            	//C[i][j] = A[i][j] + B[i][j];
            	C[i][j] = GF.add(A[i][j], B[i][j]);
        return C;
    }
    
    // vector-matrix multiplication (y = x^T A)
    /***
     * Multiplies a vector times a Matrix (in that order). This means
     * y = x^T A.
     * 
     * @param x input vector
     * @param A input matrix
     * @return a vector with the value
     */
    public static int[] multiply(int[] x, int[][] A) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        int[] y = new int[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                //y[j] += A[i][j] * x[i];
            	y[j] = GF.add(y[j], GF.multiply(A[i][j], x[i]));
        return y;
    }
    
    // matrix-vector multiplication (y = A * x)
    /***
     * Multiplies a Matrix by a vector (in that order). This means
     * y = A * x.
     * 
     * @param x input vector
     * @param A input matrix
     * @return a vector with the value
     */
    public static int[] multiply(int[][] A, int[] x) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        int[] y = new int[m];
        for (int i = 0; i < m; i++)
        	for (int j = 0; j < n; j++)
        		//y[i] += A[i][j] * x[j];
        		y[i] = GF.add(y[i], GF.multiply(A[i][j], x[j]));
        return y;
    }
    
    // return C = A^T
    public static int[][] transpose(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        int[][] C = new int[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }
}
