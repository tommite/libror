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

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PartialValueFunctionTest {
	
	private PartialValueFunction f;


	@Before
	public void setUp() {
		f = new PartialValueFunction(3);
	}
	

	@Test
	public void testConstructor() {
		assertArrayEquals(new double[] {0.0, 0.0, 1.0}, f.getValues(), 0.000001);
	}
	
	@Test
	public void testSetValue() {
		f.setValue(1, 0.8);
		assertArrayEquals(new double[] {0.0, 0.8, 1.0}, f.getValues(), 0.000001);
	}
	
	@Test
	public void testDeepCopy() {
		f.setValue(1, 0.8);
		PartialValueFunction newf = f.deepCopy();
		assertTrue(Arrays.equals(newf.getValues(), f.getValues()));
	}
	
	@Test
	public void testEquals() {
		f.setValue(1, 0.5);
		PartialValueFunction f2 = new PartialValueFunction(3);
		assertFalse(f.equals(f2));
		f2.setValue(1, 0.5);
		assertEquals(f, f2);
	}
}
