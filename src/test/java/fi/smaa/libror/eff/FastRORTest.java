/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-13 Tommi Tervonen.
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
