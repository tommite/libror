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
