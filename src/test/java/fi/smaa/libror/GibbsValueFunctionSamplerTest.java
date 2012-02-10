package fi.smaa.libror;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.junit.Before;
import org.junit.Test;

public class GibbsValueFunctionSamplerTest {

	private GibbsValueFunctionSampler s;
	private Array2DRowRealMatrix perfMat;
	private RORModel ror;
	private FullValueFunction spoint;
	private PartialValueFunction vf1;
	private PartialValueFunction vf2;
	private PartialValueFunction vf3;
	private int[] a1inds;
	private int[] a2inds;

	@Before
	public void setUp() throws InvalidStartingPointException {
		double[][] data = new double[][]{
				{1,2,3},
				{2,1,2}};
		perfMat = new Array2DRowRealMatrix(data);
		ror = new RORModel(new PerformanceMatrix(perfMat));
		spoint = new FullValueFunction();
		vf1 = new PartialValueFunction(2);
		vf2 = new PartialValueFunction(2);
		vf3 = new PartialValueFunction(2);
		spoint.addValueFunction(vf1);
		spoint.addValueFunction(vf2);
		spoint.addValueFunction(vf3);
		ror.addPreference(0, 1);
		spoint.setWeight(0, 0.0);
		spoint.setWeight(1, 1.0);
		spoint.setWeight(2, 0.0);
		
		a1inds = new int[]{0, 1, 1};
		a2inds = new int[]{1, 0, 0};

		s = new GibbsValueFunctionSampler(ror, 10, 2, spoint);
	}
	
	@Test
	public void testGetNrValueFunctions() {
		assertEquals(10, s.getValueFunctions().length);
	}
	
	@Test
	public void testGetThinning() {
		assertEquals(2, s.getThinning());
	}
	
	@Test
	public void testGetStartingPoint() {
		assertEquals(spoint, s.getStartingPoint());
	}
	
	@Test
	public void testWeightSampling() throws SamplingException {
		double[] origw = spoint.getWeights().clone();
		s.sample();
		double[] neww = s.getValueFunctions()[9].getWeights();
		assertFalse(Arrays.equals(origw, neww));
	}
	
	@Test
	public void testValueFunctions() throws SamplingException {
		s.sample();
		for (FullValueFunction vf : s.getValueFunctions()) {
			assertTrue(vf.evaluate(a1inds) > vf.evaluate(a2inds));
		}
	}
	
	@Test(expected=InvalidStartingPointException.class)
	public void testInvalidStartingPointWeights() throws InvalidStartingPointException {
		spoint.setWeight(0, 0.0);
		spoint.setWeight(1, 8.0);
		spoint.setWeight(2, 0.0);
		s = new GibbsValueFunctionSampler(ror, 10, 2, spoint);
	}
	
	@Test(expected=InvalidStartingPointException.class)
	public void testInvalidStartingPointNrPartialVF() throws InvalidStartingPointException {
		spoint = new FullValueFunction();
		spoint.addValueFunction(vf1);
		spoint.addValueFunction(vf2);

		s = new GibbsValueFunctionSampler(ror, 10, 2, spoint);
	}
	
	@Test(expected=InvalidStartingPointException.class)
	public void testInvalidStartingPointNrPartialVFLevels() throws InvalidStartingPointException {
		spoint = new FullValueFunction();
		vf1 = new PartialValueFunction(4);
		spoint.addValueFunction(vf1);
		spoint.addValueFunction(vf2);
		spoint.addValueFunction(vf3);

		s = new GibbsValueFunctionSampler(ror, 10, 2, spoint);
	}
	
	@Test
	public void testGenerateStartingPoint() throws InvalidStartingPointException {
		FullValueFunction point = s.generateStartingPoint();
		assertTrue(point.evaluate(a1inds) > point.evaluate(a2inds));
	}
	
	@Test(expected=InvalidStartingPointException.class)
	public void testGenerateStartingPointInfeasible() throws InvalidStartingPointException {
		double[][] data = new double[][]{
				{1,1,1},
				{0,0,0}};
		perfMat = new Array2DRowRealMatrix(data);
		ror = new RORModel(new PerformanceMatrix(perfMat));
		spoint = new FullValueFunction();
		vf1 = new PartialValueFunction(2);
		vf2 = new PartialValueFunction(2);
		spoint.addValueFunction(vf1);
		spoint.addValueFunction(vf2);
		ror.addPreference(1, 0);

		s = new GibbsValueFunctionSampler(ror, 10, 2);
	}
}
