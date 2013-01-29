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

package fi.smaa.libror;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class FullValueFunctionTest {

	private PartialValueFunction vf1;
	private PartialValueFunction vf2;
	private FullValueFunction wf;

	@Before
	public void setUp() {
		vf1 = new PartialValueFunction(3);
		vf2 = new PartialValueFunction(2);
		wf = new FullValueFunction();
		wf.addValueFunction(vf1);
		wf.addValueFunction(vf2);
	}
	
	@Test
	public void testConstructor() {
		assertArrayEquals(new double[]{1.0, 0.0}, wf.getWeights(), 0.0001);
	}
	
	@Test
	public void getPartialValueFunctions() {
		assertEquals(vf1, wf.getPartialValueFunctions().get(0));
		assertEquals(vf2, wf.getPartialValueFunctions().get(1));
	}
	
	@Test
	public void testSetWeight() {
		wf.setWeight(1, 0.8);
		assertArrayEquals(new double[]{1.0, 0.8}, wf.getWeights(), 0.0001);		
	}
	
	@Test
	public void testDeepCopy() {
		FullValueFunction nf = wf.deepCopy();
		assertTrue(Arrays.equals(nf.getWeights(), wf.getWeights()));
		assertEquals(nf.getPartialValueFunctions().size(), wf.getPartialValueFunctions().size());
	}
	
	@Test
	public void testEvaluate() {
		wf.setWeight(0, 0.2);
		wf.setWeight(1, 0.8);

		int[] inds = new int[] {2, 0};
		assertEquals(0.2, wf.evaluate(inds), 0.00001);
		inds = new int[] {0, 1};
		assertEquals(0.8, wf.evaluate(inds), 0.00001);
	}
}
