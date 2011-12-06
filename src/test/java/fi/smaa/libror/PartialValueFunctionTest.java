package fi.smaa.libror;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.libror.PartialValueFunction;

public class PartialValueFunctionTest {
	
	private double[] vals = new double[] {1.0, 4.0, 5.0};
	private double[] evals = new double[] {0.0, 0.5, 1.0};
	private PartialValueFunction vf;

	@Before
	public void setUp() {
		 vf = new PartialValueFunction(vals, evals);		
	}

	@Test
	public void testEvaluate() {	
		assertEquals(0.0, vf.evaluate(1.0), 0.0001);
		assertEquals(0.5, vf.evaluate(4.0), 0.0001);
		assertEquals(1.0, vf.evaluate(5.0), 0.0001);
		assertEquals(0.75, vf.evaluate(4.5), 0.0001);
	}
}
