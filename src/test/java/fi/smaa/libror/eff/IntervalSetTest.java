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

package fi.smaa.libror.eff;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IntervalSetTest {
	
	private IntervalSet s;

	@Before
	public void setUp () {
		s = new IntervalSet();
	}

	@Test
	public void testAddDisjunctIntervals() {
		s.add(new Interval(-1.0, 2.0));
		s.add(new Interval(3.0, 4.0));
		
		List<Interval> ivals = s.getIntervals();
		assertEquals(new Interval(-1.0, 2.0), ivals.get(0));
		assertEquals(new Interval(3.0, 4.0), ivals.get(1));
		assertEquals(2, ivals.size());
	}
		
	@Test
	public void testAddJustExactIntervals() {
		s.add(new Interval(-1.0, 2.0));
		s.add(new Interval(2.0, 3.0));
		
		List<Interval> ivals = s.getIntervals();
		assertEquals(1, ivals.size());	
		assertEquals(new Interval(-1.0, 3.0), ivals.get(0));
	}
	
	@Test
	public void testAddOverlappingIntervals() {
		s.add(new Interval(2.0, 3.0));
		s.add(new Interval(1.0, 2.5));
		
		List<Interval> ivals = s.getIntervals();
		assertEquals(new Interval(1.0, 3.0), ivals.get(0));
		assertEquals(1, ivals.size());
	}	
}
