package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.hadoop.raid.GFMatrix;
import org.apache.hadoop.raid.GaloisField;
import org.junit.Test;

public class TestGFMatrix {

	final int[][] m1 = { { 1, 2 }, { 4, 5 } };
	final int[][] m2 = { { 1, 2 }, { 4, 5 } };
	final int[] v1 = { 1, 2 };
	final int[][] zero = new int[2][2];
	final static GaloisField GF = GaloisField.getInstance();

	@Test
	public void testIdentity() {
		int[][] i3 = { {1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
		int[][] genI3 = GFMatrix.identity(i3.length);
		assertTrue("Identity matrices are not equal!", Arrays.deepEquals(i3, genI3));
	}

	@Test
	public void testAddition() {
		assertTrue("Addition is not correctly implemented!", Arrays.deepEquals(zero, GFMatrix.add(m1, m2)));
	}
	
	@Test
	public void testMultiplication1() {
		int[] r = {	9, 8 };
		int[] temp = GFMatrix.multiply(v1, m1);
		assertTrue("vector-matrix Multiplication is not working well!",
				Arrays.equals(temp, r));
	}
	
	@Test
	public void testMultiplication2() {
		int[] r = { 5 , 14 };
		int[] temp = GFMatrix.multiply(m1, v1);
		assertTrue("matrix-vector Multiplication is not working well!",
				Arrays.equals(temp, r));
	}
}