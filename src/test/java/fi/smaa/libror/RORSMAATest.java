/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror.php.
 * Copyright (C) 2011 Tommi Tervonen.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	public static void setUpForAll() throws InfeasibleConstraintsException, SamplingException {
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
		RORModel model = new RORModel(new PerformanceMatrix(perfMat));
		ror = new RORSMAA(model, new RejectionValueFunctionSampler(model, 10000));
		ror.getModel().addPreference(9, 8); // DEN > AUT
		ror.getModel().addPreference(2, 3); // SPA > SWE
		ror.getModel().addPreference(10, 11); // FRA > CZE
		ror.compute();
	}
	
	@Test
	public void testGetNrAltsCrits() {
		assertEquals(4, ror.getModel().getNrCriteria());
		assertEquals(20, ror.getModel().getNrAlternatives());
	}
	
	@Test
	public void testPOIFirstRow() {
		RealMatrix poi = ror.getPOIs();
		assertArrayEquals(new double[]{1.0, 0.7811, 1.0, 1.0, 0.9999, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, poi.getRow(0), 0.02);
	}
		
	@Test
	public void testRAIFirstRow() {
		RealMatrix rai = ror.getRAIs();
		assertArrayEquals(new double[]{0.7811, 0.2188, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, rai.getRow(0), 0.02);		
	}
	
}
