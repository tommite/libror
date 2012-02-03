package fi.smaa.libror;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.junit.Before;
import org.junit.Test;

public class GibbsValueFunctionSamplerTest {

	private GibbsValueFunctionSampler s;
	private Array2DRowRealMatrix perfMat;
	private RORSMAA ror;
	private WeightedOrdinalValueFunction spoint;
	private OrdinalPartialValueFunction vf1;
	private OrdinalPartialValueFunction vf2;
	private OrdinalPartialValueFunction vf3;

	@Before
	public void setUp() {
		double[][] data = new double[][]{
				{1,2,3},
				{2,1,2},
				{1,1,3}};
		perfMat = new Array2DRowRealMatrix(data);
		ror = new RORSMAA(new PerformanceMatrix(perfMat));
		spoint = new WeightedOrdinalValueFunction();
		vf1 = new OrdinalPartialValueFunction(2);
		vf2 = new OrdinalPartialValueFunction(2);
		vf3 = new OrdinalPartialValueFunction(2);
		spoint.addValueFunction(vf1);
		spoint.addValueFunction(vf2);
		spoint.addValueFunction(vf3);
		ror.addPreference(0, 1);
		spoint.setWeight(0, 0.0);
		spoint.setWeight(1, 1.0);
		spoint.setWeight(2, 0.0);

		s = new GibbsValueFunctionSampler(ror, 10, 2, spoint);	
	}
	
	@Test
	public void testGetNrValueFunctions() {
		assertEquals(10, s.getNrValueFunctions());
	}
	
	@Test
	public void testGetThinning() {
		assertEquals(2, s.getThinning());
	}
	
	@Test
	public void testGetStartingPoint() {
		assertEquals(spoint, s.getStartingPoint());
	}
}
