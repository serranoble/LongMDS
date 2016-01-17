package org.apache.hadoop.raid;

import java.util.ArrayList;

public class LongMDSCode {
	
	private final int stripeSize;
	private final int paritySize;
	private final GaloisField GF = GaloisField.getInstance();
	
	private static ArrayList<int[][]> A = null;
	private static ArrayList<int[][]> S = null;
	
	/**
	 * This class implements a (6,4) Long MDS Code. Other code sizes
	 * should be implemented manually or through a code generalization.
	 * This constructor has been created from a generic point of view,
	 * with the hope that it will be finished later.
	 * 
	 * @param stripeSize size of the stripe (must be 6)
	 * @param paritySize size of the parity (must be 2)
	 */
	public LongMDSCode(int stripeSize, int paritySize) {
		// some constrains that must be satisfied...
		assert(stripeSize + paritySize < GF.getFieldSize());
		assert(stripeSize == 6);
		assert(paritySize == 2);
		
		this.stripeSize = stripeSize;
		this.paritySize = paritySize;
		generateMatrices();
	}
	
	/***
	 * We need to produce some kind of automatic procedure to
	 * generate all these matrices... it's quite painful to be
	 * writing them one by one
	 */
	private void generateMatrices() {
		//prevents to execute again!
		if (A != null && S != null)
			return;
		
		//create all the A matrices
		int[][] a1 = { {8,40,0,0}, {0,32,0,0},
				{0,0,8,40}, {0,0,0,32} };
		int[][] a2 = { {4,0,20,0}, {0,4,0,20},
				{0,0,16,0}, {0,0,0,16} };
		int[][] a3 = { {8,0,0,0}, {0,32,0,0},
				{0,0,8,0}, {0,0,40,32} };
		int[][] a4 = { {4,0,0,0}, {0,4,0,0},
				{20,0,16,0}, {0,20,0,16} };
		int[][] a5 = { {8,0,0,0}, {0,2,0,0},
				{0,0,8,0}, {0,0,0,2} };
		int[][] a6 = { {4,0,0,0}, {0,4,0,0},
				{0,0,1,0}, {0,0,0,1} };
		
		//create all the S matrices
		int[][] s1 = { {1,0,0,0}, {0,0,1,0} };
		int[][] s2 = { {1,0,0,0}, {0,1,0,0} };
		int[][] s3 = { {0,1,0,0}, {0,0,0,1} };
		int[][] s4 = { {0,0,1,0}, {0,0,0,1} };
		int[][] s5 = { {1,1,0,0}, {0,0,1,1} };
		int[][] s6 = { {1,0,1,0}, {0,1,0,1} };
		
		//insertion is done manually...
		if (A == null) {
		    A = new ArrayList<int[][]>();
		    A.add(a1); A.add(a2); A.add(a3); A.add(a4); A.add(a5); A.add(a6);
		}
		
		if (S == null) {
		    S = new ArrayList<int[][]>();
		    S.add(s1); S.add(s2); S.add(s3); S.add(s4); S.add(s5); S.add(s6);
		}
	}

	/*
	 * In some sense, this kind of code works like XOR code but producing
	 * more than one parity. For this reason, the result of the encode
	 * process should be an array.
	 */
	public void encode(int[][] data, int[][] parity) {
		int bufSize = parity.length;
		int numcols = stripeSize - paritySize;
		
		//create the first parity (simple XOR)
		for (int i = 0; i < bufSize; i++)
			parity[i][0] = data[i][0];
		for (int j = 1; j < numcols; j++)
		    for (int i = 0; i < bufSize; i++)
			    parity[i][0] ^= data[i][j];
		
		//create the second parity (multiplication-based)
		for (int i = 0; i < numcols; i++) {
			//create a temporary vector with all the data for that node
			int[] temp = new int[bufSize];
			for (int j = 0; j < bufSize; j++)
				temp[j] = data[j][i];
			//multiply node data with the Ax matrix
			int[] result = GFMatrix.multiply(A.get(i), temp);
			//copy the result into the 2nd parity
			for (int j = 0; j < bufSize; j++)
				//note that we need to ADD the results!!
				parity[j][1] ^= result[j];
		}
	}
	
	/*
	 * With all the survival data and knowing the erased location then it's
	 * possible to calculate all the dependencies required to recover the 
	 * data efficiently. Nevertheless, using this approach force us to
	 * recover all the data from the disk (requires optimizations)
	 */
	public void decode(int[][] data, int erasedLocation, int[] erasedValue) {
		
	}
}
