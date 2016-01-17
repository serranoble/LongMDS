package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.apache.hadoop.raid.LongMDSCode;
import org.junit.Test;

public class TestLongMDSCode {
	
	//every node is represented by a column
	final int[][] data = { {1,2,3,4}, {5,6,7,8}, 
			{9,10,11,12}, {13,14,15,16}};
	final int[][] parity = { {4,8}, {12,160}, 
			{4,245}, {28,56}};
	final int stripeSize = 6;
	final int paritySize = 2;
	final Random RAND = new Random();

	@Test
	public void testEncoding() {
		int[][] temp = new int[data.length][paritySize];
		LongMDSCode longMDS = new LongMDSCode(stripeSize, paritySize);
		longMDS.encode(data, temp);
		assertTrue("Encoding is not working properly", Arrays.deepEquals(parity, temp));
	}

	@Test
	public void testDecoding() {
		fail("Not yet implemented");
	}
}
