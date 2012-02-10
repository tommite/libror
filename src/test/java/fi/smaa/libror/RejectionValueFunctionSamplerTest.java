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
	private RORModel model;

	@Before
	public void setUp() {
		int rows = 2;
		int cols = 3;
		RealMatrix p = new Array2DRowRealMatrix(rows, cols);
		p.setRow(0, new double[] {1.0, 2.0, 3.0});
		p.setRow(1, new double[] {1.0, 3.0, 4.0});
		model = new RORModel(new PerformanceMatrix(p));
		sampler = new RejectionValueFunctionSampler(model, 5);
	}
	
	@Test
	public void testGetMaxIters() {
		assertEquals(10000000, sampler.getMaxIters());
		sampler = new RejectionValueFunctionSampler(model, 5, 12);
		assertEquals(12, sampler.getMaxIters());
	}
	
	@Test
	public void testValueFunctionsSize() {
		assertEquals(5, sampler.getValueFunctions().length);
	}
	
	@Test
	public void testSampleFunctions() throws SamplingException {
		int rows = 2;
		int cols = 2;
		RealMatrix p = new Array2DRowRealMatrix(rows, cols);
		p.setRow(0, new double[] {1.0, 2.0});
		p.setRow(1, new double[] {2.0, 1.0});
		model = new RORModel(new PerformanceMatrix(p));
		model.addPreference(0, 1); // a0 > a1
		sampler = new RejectionValueFunctionSampler(model, 2);
		sampler.sample();
		for (FullValueFunction vf : sampler.getValueFunctions()) {
			assertEquals(2, vf.getPartialValueFunctions().size());
			assertEquals(new PartialValueFunction(2), vf.getPartialValueFunctions().get(0));
			assertEquals(new PartialValueFunction(2), vf.getPartialValueFunctions().get(1));
			double[] w = vf.getWeights();
			assertTrue(w[0] < w[1]);
		}
	}
	
	@Test(expected=SamplingException.class)
	public void testSampleInfeasibleFunctions() throws SamplingException {
		model.addPreference(0, 1);
		sampler = new RejectionValueFunctionSampler(model, 1, 100);
		sampler.sample();
	}
}