package fi.smaa.libror;

import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class RORSMAATest {

	private static RealMatrix perfMat;
	private static RORSMAA ror;

	@BeforeClass
	public static void setUpForAll() {
		double[][] data = new double[][]{
				{82,94,80,91},
				{74,91,96,82},
				{59,73,72,67},
				{47,77,90,46},
				{50,73,88,47},
				{51,50,84,55},
				{42,59,88,39},
				{44,57,84,41},
				{42,53,88,38},
				{42,61,68,39},
				{45,37,80,44},
				{41,43,80,40},
				{41,41,60,40},
				{38,37,72,34},
				{40,40,60,34},
				{39,34,48,38},
				{38,36,44,34},
				{39,28,40,34},
				{39,26,36,37},
				{37,21,8,37}};
		perfMat = new Array2DRowRealMatrix(data);
		ror = new RORSMAA(perfMat);
		ror.addPreference(9, 8); // DEN > AUT
		ror.addPreference(2, 3); // SPA > SWE
		ror.addPreference(10, 11); // FRA > CZE
		ror.solve();
		ror.compute();
	}
	
	@Test
	public void testGetNrAltsCrits() {
		assertEquals(4, ror.getNrCriteria());
		assertEquals(20, ror.getNrAlternatives());
	}

	@Test
	public void testNecessaryRelationFirstRow() {
		RealMatrix necessaryRelation = ror.getNecessaryRelation();
		assertArrayEquals(new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, necessaryRelation.getRow(0), 0.001);
	}
	
	@Test
	public void testPossibleRelationThirdRow() {
		RealMatrix possibleRelation = ror.getPossibleRelation();
		assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, possibleRelation.getRow(2), 0.001);
	}
	
	@Test
	public void testPOIFirstRow() {
		RealMatrix poi = ror.getPOIs();
		assertArrayEquals(new double[]{1.0, 0.7811, 1.0, 1.0, 0.9999, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, poi.getRow(0), 0.02);
	}
	
	@Test
	public void testSameDimMatrices() {
		assertEquals(ror.getPossibleRelation().getColumnDimension(), ror.getPOIs().getColumnDimension());
		assertEquals(ror.getPossibleRelation().getRowDimension(), ror.getPOIs().getRowDimension());
	}
	
	@Test
	public void testPOIsWithPossibleRelations() {
		RealMatrix poi = ror.getPOIs();
		RealMatrix pos = ror.getPossibleRelation();		
		for (int i=0;i<pos.getRowDimension();i++) {
			for (int j=0;j<pos.getColumnDimension();j++) {
				if (poi.getEntry(i, j) > 0.0) {
					assertTrue("pos vs poi failed at ("+i+","+j+")", pos.getEntry(i, j) > 0.0);
				}
			}
		}
	}
	
}
