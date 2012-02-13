/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-12 Tommi Tervonen.
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
