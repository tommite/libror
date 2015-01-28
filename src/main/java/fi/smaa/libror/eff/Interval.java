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

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

public final class Interval {	
	
	private Double start;
	private Double end;
	
	public Interval() {
		start = 0.0;
		end = 0.0;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws InvalidIntervalException if end < start
	 */
	public Interval(Double start, Double end) throws InvalidIntervalException {
		if (end < start) {
			throw new InvalidIntervalException();
		}
		this.start = start;
		this.end = end;
	}
	
	public Double getStart() {
		return start;
	}
	
	public Double getEnd() {
		return end;
	}
	
	public void setStart(Double start) {
		this.start = start;
	}
	
	public void setEnd(Double end) {
		this.end = end;
	}

	@Override
	public String toString() {
		DecimalFormat fmt = new DecimalFormat("#0.00");
		return "[" + fmt.format(start) + " - " + fmt.format(end) + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Interval)) {
			return false;
		}
		Interval io = (Interval) other;
		return getStart().equals(io.getStart()) && getEnd().equals(io.getEnd());
	}
	
	public static Interval enclosingInterval(Collection<Interval> intervals) {
		if (intervals.size() == 0) {
			return null;
		}
		Iterator<Interval> it = intervals.iterator();
		Interval enclosing = it.next().deepCopy();
		while (it.hasNext()) {
			Interval other = it.next();
			if (other.getStart() < enclosing.getStart()) {
				enclosing.setStart(other.getStart());
			}
			if (other.getEnd() > enclosing.getEnd()) {
				enclosing.setEnd(other.getEnd());
			}
		}
		return enclosing;
	}
	
	public Double getLength() {
		return end - start;
	}
	
	public boolean includes(Interval other) {
		return other.getStart() >= getStart() && other.getEnd() <= getEnd(); 
	}

	public Interval deepCopy() {
		return new Interval(start, end);
	}

	public Interval getRange() {
		return this;
	}
	
	public boolean includes(Double val) {
		return val >= getStart() && val <= getEnd();
	}

	public Double getMiddle() {
		return (getStart() + getEnd()) / 2.0;
	}
		
	public boolean overlaps(Interval other) {
		if (start <= other.getStart()) {
			if (this.end >= other.start) {
				return true;
			}
		} else { // start > other.start()
			if (other.end >= start) {
				return true;
			}
		}
		return false;
	}	
}
