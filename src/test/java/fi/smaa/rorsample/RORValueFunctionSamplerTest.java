package fi.smaa.rorsample;

import static org.junit.Assert.*;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

public class RORValueFunctionSamplerTest {

	private RORValueFunctionSampler sampler;

	@Before
	public void setUp() {
		int rows = 3;
		int cols = 3;
		RealMatrix p = new Array2DRowRealMatrix(rows, cols);
		p.setRow(0, new double[] {1.0, 2.0, 3.0});
		p.setRow(1, new double[] {1.0, 3.0, 4.0});
		p.setRow(2, new double[] {2.0, -1.0, 3.0});
		sampler = new RORValueFunctionSampler(p, 5);
	}
	
	@Test
	public void testGetLevels() {
		RealVector[] lvls = sampler.getLevels();
		assertEquals(new ArrayRealVector(new double[]{1.0, 2.0}), lvls[0]);
		assertEquals(new ArrayRealVector(new double[]{-1.0, 2.0, 3.0}), lvls[1]);
		assertEquals(new ArrayRealVector(new double[]{3.0, 4.0}), lvls[2]);
		assertEquals(3, lvls.length);
	}
	
	@Test
	public void testGetValueFunctions() {
		assertEquals(5, sampler.getValueFunctions().length);
	}
}
