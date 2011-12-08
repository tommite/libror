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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.libror.PartialValueFunction;

public class PartialValueFunctionTest {
	
	private double[] vals = new double[] {1.0, 4.0, 5.0};
	private double[] evals = new double[] {0.0, 0.5, 1.0};
	private PartialValueFunction vf;

	@Before
	public void setUp() {
		 vf = new PartialValueFunction(vals, evals);		
	}

	@Test
	public void testEvaluate() {	
		assertEquals(0.0, vf.evaluate(1.0), 0.0001);
		assertEquals(0.5, vf.evaluate(4.0), 0.0001);
		assertEquals(1.0, vf.evaluate(5.0), 0.0001);
		assertEquals(0.75, vf.evaluate(4.5), 0.0001);
	}
}
