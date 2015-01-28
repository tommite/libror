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
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class MaximalVectorComputationTest {

	private RealMatrix data;

	@Before
	public void setUp() {
		data = new Array2DRowRealMatrix(new double[][]{
				{1.0, 2.0, 3.0},
				{1.0, 2.0, 2.0},
				{2.0, 1.0, 3.0}
		});
	}
	
	@Test
	public void testCompute() {
		RealMatrix exp = new Array2DRowRealMatrix(new double[][]{
				{1.0, 2.0, 3.0},
				{2.0, 1.0, 3.0}
		});
		
		assertEquals(exp, MaximalVectorComputation.computeBEST(data));
	}
	
	@Test
	public void testBug() {
		RealMatrix data = new Array2DRowRealMatrix(new double[][]{
				{0.1823507, 0.5232321, 0.7595968, 0.2964752, 0.1676054},
				{0.5408093, 0.1604821, 0.4699517, 0.4170541, 0.5357071},
				{0.1292226, 0.2366909, 0.7583132, 0.3765545, 0.4587448}
		});
		
		assertArrayEquals(new int[]{0, 1, 2}, MaximalVectorComputation.computeBESTindices(data));
	}
	
}
