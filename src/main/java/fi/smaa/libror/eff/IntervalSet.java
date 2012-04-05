package fi.smaa.libror.eff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IntervalSet {
	
	private List<Interval> ivals = new LinkedList<Interval>();

	/**
	 * Constructs an empty IntervalSet instance.
	 */
	public IntervalSet() {
	}
	
	/**
	 * Adds an interval to the set. Effectively performs an union.
	 * 
	 * @param i the interval to add
	 */
	public void add(Interval i) {
		List<Interval> overlaps = new ArrayList<Interval>();
		
		Iterator<Interval> it = ivals.iterator();
		while (it.hasNext()) {
			Interval ival = it.next();
			if (ival.overlaps(i)) {
				if (ival.includes(i)) {
					return;
				} else if (i.includes(ival)) {
					it.remove();
					add(i);
					return;
				} else {
					overlaps.add(ival);
				}
			}
		}
		if (overlaps.size() > 0) {
			overlaps.add(i);
			ivals.removeAll(overlaps);
			ivals.add(Interval.enclosingInterval(overlaps));
		} else {
			ivals.add(i);
		}
	}
	
	/**
	 * Returns the intervals that are part of this set.
	 * 
	 * @return the intervals in this set
	 */
	public List<Interval> getIntervals() {
		return ivals;
	}
}
