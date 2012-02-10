package fi.smaa.libror;

import static org.junit.Assert.*;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class AcceptanceCriterionTest {

	private RORModel model;
	private AcceptanceCriterion criterion;

	@Before
	public void setUp() {
		int rows = 2;
		int cols = 2;
		RealMatrix p = new Array2DRowRealMatrix(rows, cols);
		p.setRow(0, new double[] {1.0, 2.0});
		p.setRow(1, new double[] {2.0, 1.0});
		model = new RORModel(new PerformanceMatrix(p));
		model.addPreference(0, 1); // a0 > a1
		criterion = new AcceptanceCriterion(model);
	}
	
	@Test
	public void testCheck() {
		FullValueFunction f = new FullValueFunction();
		f.addValueFunction(new PartialValueFunction(2));
		f.addValueFunction(new PartialValueFunction(2));
		f.setWeight(0, 0.8);
		f.setWeight(1, 0.2);
		assertFalse(criterion.check(f));
		f.setWeight(0, 0.2);
		f.setWeight(1, 0.8);
		assertTrue(criterion.check(f));
	}
}
