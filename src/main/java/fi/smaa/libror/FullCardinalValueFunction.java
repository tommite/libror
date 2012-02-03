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

import java.util.ArrayList;
import java.util.List;

public class FullCardinalValueFunction {
	
	private List<CardinalPartialValueFunction> vfs = new ArrayList<CardinalPartialValueFunction>();

	public FullCardinalValueFunction() {
	}
	
	public void addValueFunction(CardinalPartialValueFunction v) {
		vfs.add(v);
	}
	
	public String toString() {
		String retStr = "";
		for (CardinalPartialValueFunction vf : vfs) {
			retStr += vf.toString() + "\n";
		}
		return retStr;
	}
	
	public List<CardinalPartialValueFunction> getPartialValueFunctions() {
		return vfs;
	}
	
	public double evaluate(double[] point) {
		assert(point.length == vfs.size());
		
		double sum = 0.0;
		for (int i=0;i<point.length;i++) {
			sum += vfs.get(i).evaluate(point[i]);
		}
		return sum;
	}
}
