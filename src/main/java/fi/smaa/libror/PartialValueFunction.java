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

public class PartialValueFunction {
	
	private double[] vals;
	private double[] evals;

	/**
	 * 
	 * PRECOND: evals.length == vals.length
	 * PRECOND: evals.length >= 2
	 * PRECOND: evals[0] == 0.0
	 * PRECOND: vals are in ascending order
	 * PRECOND: evals are either in ascending or descending order
	 * @param vals performances (real scores)
	 * @param evals corresponding values v(val)
	 */
	public PartialValueFunction(double[] vals, double[] evals) {
		assert(evals.length == vals.length);
		assert(evals.length >= 2);
		assert(evals[0] == 0.0);
		
		this.evals = evals;
		this.vals = vals;
	}
	
	/**
	 * PRECOND: point within value bounds
	 * @param point
	 * @return evaluation
	 */
	public double evaluate(double point) {
		assert(point >= vals[0]);
		assert(point <= vals[vals.length-1]);

		int geqIndex = 0;
		
		while (point > vals[geqIndex]) {
			geqIndex++;
		}
		
		if (geqIndex == 0) {
			return evals[0];
		}
		
		double prevVal = evals[geqIndex-1];
		double diff = evals[geqIndex] - prevVal;
		
		double prevPoint = vals[geqIndex-1];
		double nextPoint = vals[geqIndex];
				
		return prevVal + diff * ((point - prevPoint) / (nextPoint - prevPoint));
	}
	
	@Override
	public String toString() {
		String retStr = "";
		for (int i=0;i<vals.length;i++) {
			retStr += "(" + vals[i] + ": " + evals[i] + ")";
			if (i != vals.length-1) {
				retStr+=" ";
			}
		}
		return retStr;
	}
	
	public double[] getEvals() {
		return evals;
	}
	
	public double[] getVals() {
		return vals;
	}

}
