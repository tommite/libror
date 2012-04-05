package fi.smaa.libror.eff;

import static org.junit.Assert.*;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;

public class FastRORTest {
	
	private FastROR ror;
	private RealMatrix perf;

	@Before
	public void setUp() {
		perf = new Array2DRowRealMatrix(new double[][]{
				{1.0, 2.0, 3.0},
				{2.0, 2.0, 2.0},
				{3.0, 1.0, 2.0},
				{1.0, 3.0, 2.0},
				{1.0, 1.0, 1.0}
		});
		RORModel model = new RORModel(new PerformanceMatrix(perf));
		model.addPreference(0, 1);
		ror = new FastROR(model);
	}

	@Test
	public void testNecessaryComputation() {
		boolean[][] nec = ror.computeNecessary();
				
		assertTrue(nec[0][4]);
		assertTrue(nec[1][4]);
		assertTrue(nec[2][4]);
		assertTrue(nec[3][4]);
	}
}
