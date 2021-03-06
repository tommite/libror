/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-15 Tommi Tervonen.
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

import static org.junit.Assert.*;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

public class PerformanceMatrixTest {

	private PerformanceMatrix matrix;

	@Before
	public void setUp() {
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
		
		matrix = new PerformanceMatrix(new Array2DRowRealMatrix(data));
	}
	
	@Test
	public void testGetLevels() {
		RealVector[] lvls = matrix.getLevels();
		assertEquals(new ArrayRealVector(new double[]{37.0, 38.0, 39.0, 40.0, 41.0, 42.0, 44.0, 45.0, 47.0, 50.0,
				51.0, 59.0, 74.0, 82.0}), lvls[0]);
		assertEquals(new ArrayRealVector(new double[]{21.0, 26.0, 28.0, 34.0, 36.0, 37.0, 40.0, 41.0, 43.0, 50.0, 
				53.0, 57.0, 59.0, 61.0, 73.0, 77.0, 91.0, 94.0}), lvls[1]);
	}
	
	@Test
	public void testGetLevelIndex() {
		assertEquals(8, matrix.getLevelIndex(0, 2));
		assertEquals(13, matrix.getLevelIndex(0, 0));
	}
	
	@Test
	public void testGetLevelIndices() {
		assertArrayEquals(new int[]{13, 17, 8, 12}, matrix.getLevelIndices(0));
	}
}
