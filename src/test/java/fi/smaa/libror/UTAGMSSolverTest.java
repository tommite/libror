package fi.smaa.libror;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.Relationship;
import org.junit.Before;
import org.junit.Test;

public class UTAGMSSolverTest {
	
	private UTAGMSSolver solver;
	private RealMatrix perfMatrix;
	
	@Before
	public void setUp() {
		perfMatrix = new Array2DRowRealMatrix(3, 3);
		perfMatrix.setRow(0, new double[] {1.0, 1.0, 1.0});
		perfMatrix.setRow(1, new double[] {2.0, 1.0, 1.1});
		perfMatrix.setRow(2, new double[] {2.0, 0.5, 3.0});
		solver = new UTAGMSSolver(perfMatrix);
		solver.addPreference(2, 1); // a3 >= a2
	}
		
	public void testNecessaryRelationResults() {
		//solver.printNecessaryModel(1, 2);
		solver.solve();
		RealMatrix nrel = solver.getNecessaryRelation();
		System.out.println(nrel);
		assertArrayEquals(new double[]{1.0, 0.0, 0.0}, nrel.getRow(0), 0.0001); 
//		assertArrayEquals(new double[]{1.0, 1.0, 0.0}, nrel.getRow(1), 0.0001); 
		assertArrayEquals(new double[]{1.0, 1.0, 1.0}, nrel.getRow(2), 0.0001); 
	}
	
	@Test
	public void testPossibleRelationResults() {
		//solver.printNecessaryModel(1, 2);
		solver.solve();
		RealMatrix nrel = solver.getPossibleRelation();
		System.out.println(nrel);
		assertArrayEquals(new double[]{1.0, 0.0, 0.0}, nrel.getRow(0), 0.0001); 
//		assertArrayEquals(new double[]{1.0, 1.0, 0.0}, nrel.getRow(1), 0.0001); 
		assertArrayEquals(new double[]{1.0, 1.0, 1.0}, nrel.getRow(2), 0.0001); 
	}	
	
	@Test
	public void testBuildRORConstraints() {
		List<LinearConstraint> c = solver.buildRORConstraints();
		// constraint for the preferences
		LinearConstraint con1 = c.get(0);
		assertArrayEquals(new double[]{0.0, 0.0, 1.0, -1.0, 0.0, -1.0, 1.0, -1.0},
				con1.getCoefficients().getData(), 0.001);
		assertEquals(Relationship.GEQ, con1.getRelationship());
		assertEquals(0.0, con1.getValue(), 0.0001);
		// constraints for the monotonicity
		int cIndex = 1;
		int cInIndex = 1;
		for (int i=0;i<solver.getNrCriteria();i++) {
			for (int j=0;j<solver.getLevels()[i].getDimension()-1;j++) {
				LinearConstraint lc = c.get(cIndex);
				double[] vals = new double[8];
				vals[cInIndex-1] = 1.0;
				vals[cInIndex] = -1.0;
				assertArrayEquals(vals, lc.getCoefficients().getData(), 0.0001);
				assertEquals(Relationship.LEQ, lc.getRelationship());
				cIndex++;
				cInIndex += 1;
			}
			cInIndex += 1;
		}
		// constraints for first level being 0
		int offset = 0;
		for (int i=0;i<solver.getNrCriteria();i++) {
			LinearConstraint lc = c.get(cIndex);
			assertEquals(Relationship.EQ, lc.getRelationship());
			assertEquals(0.0, lc.getValue(), 0.000001);
			double[] vals = new double[8];
			vals[offset] = 1.0;
			offset+=solver.getLevels()[i].getDimension();
			assertArrayEquals(vals, lc.getCoefficients().getData(), 0.00001);
			cIndex++;
		}
		// constraints for levels summing to unity
		offset = 0;
		for (int i=0;i<solver.getNrCriteria();i++) {
			LinearConstraint lc = c.get(cIndex);
			cIndex++;
			assertEquals(Relationship.EQ, lc.getRelationship());
			assertEquals(1.0, lc.getValue(), 0.000001);			
			
			double[] vals = new double[8];
			for (int j=0;j<solver.getLevels()[i].getDimension();j++) {
				vals[offset] = 1.0;
				offset++;
			}
			assertArrayEquals(vals, lc.getCoefficients().getData(), 0.00001);			
		}		
	}

}
