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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RejectionValueFunctionSamplerTest {

	private RejectionValueFunctionSampler sampler;

	@Before
	public void setUp() {
		int rows = 3;
		int cols = 3;
		RealMatrix p = new Array2DRowRealMatrix(rows, cols);
		p.setRow(0, new double[] {1.0, 2.0, 3.0});
		p.setRow(1, new double[] {1.0, 3.0, 4.0});
		p.setRow(2, new double[] {2.0, -1.0, 3.0});
		sampler = new RejectionValueFunctionSampler(new RORModel(new PerformanceMatrix(p)), 5);
	}
	
	@Test
	public void testMonotonousEvals() {
		sampler.sample();
		for (FullCardinalValueFunction fvf : sampler.getValueFunctions()) {
			for (CardinalPartialValueFunction vf : fvf.getPartialValueFunctions()) {
				double[] evals = vf.getEvals();
				double prevVal = -1.0;
				for (double eval : evals) {
					assertTrue(prevVal <= eval);
					prevVal = eval;
				}
			}
		}
	}
	
	@Test
	public void testValueFunctionsSize() {
		assertEquals(5, sampler.getValueFunctions().length);
	}
	
	@Test
	public void testValueFunctionMaxsSumToUnity() {
		sampler.sample();
		for (FullCardinalValueFunction vf : sampler.getValueFunctions()) {
			double sum = 0.0;
			for (CardinalPartialValueFunction v : vf.getPartialValueFunctions()) {
				double[] ev = v.getEvals();
				sum += ev[ev.length-1];
			}
			assertEquals(1.0, sum, 0.0000001);
		}
	}
}
