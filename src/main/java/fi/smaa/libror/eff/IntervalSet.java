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
