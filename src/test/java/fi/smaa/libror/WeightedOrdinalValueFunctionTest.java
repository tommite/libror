package fi.smaa.libror;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WeightedOrdinalValueFunctionTest {

	private OrdinalPartialValueFunction vf1;
	private OrdinalPartialValueFunction vf2;
	private WeightedOrdinalValueFunction wf;

	@Before
	public void setUp() {
		vf1 = new OrdinalPartialValueFunction(3);
		vf2 = new OrdinalPartialValueFunction(2);
		wf = new WeightedOrdinalValueFunction();
		wf.addValueFunction(vf1);
		wf.addValueFunction(vf2);
	}
	
	@Test
	public void testConstructor() {
		assertArrayEquals(new double[]{1.0, 0.0}, wf.getWeights(), 0.0001);
	}
	
	@Test
	public void getPartialValueFunctions() {
		assertEquals(vf1, wf.getPartialValueFunctions().get(0));
		assertEquals(vf2, wf.getPartialValueFunctions().get(1));
	}
	
	@Test
	public void testSetWeight() {
		wf.setWeight(1, 0.8);
		assertArrayEquals(new double[]{1.0, 0.8}, wf.getWeights(), 0.0001);		
	}
	
	@Test
	public void testDeepCopy() {
		WeightedOrdinalValueFunction nf = wf.deepCopy();
		assertTrue(Arrays.equals(nf.getWeights(), wf.getWeights()));
		assertEquals(nf.getPartialValueFunctions().size(), wf.getPartialValueFunctions().size());
	}
	
	@Test
	public void testEvaluate() {
		wf.setWeight(0, 0.2);
		wf.setWeight(1, 0.8);

		int[] inds = new int[] {2, 0};
		assertEquals(0.2, wf.evaluate(inds), 0.00001);
	}
}
