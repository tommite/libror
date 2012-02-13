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

package fi.smaa.libror;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

/**
 * Implements Maximal Vector Computation
 *
 * @author Tommi Tervonen <tommi@smaa.fi>
 */
public class MaximalVectorComputation {

	/**
	 * Implements the Best algorithm as described in Godfrey & al., VLDB Journal, 2007.
	 * 
	 * @param mat The matrix of values (each row = 1 vector of input)
	 * 
	 * @return Matrix containing rows from the input s.t. none are dominated
	 */
	public static RealMatrix computeBEST(RealMatrix mat) {		
		LinkedList<RealVector> list = matrixToListOfRows(mat);
		LinkedList<RealVector> results = new LinkedList<RealVector>();
		
		while (list.size() > 0) {
			Iterator<RealVector> iter = list.iterator();
			RealVector b = iter.next(); // Get the first
			iter.remove();
			while (iter.hasNext()) { // Find a max
				RealVector t = iter.next();
				if (dominates(b, t)) {
					iter.remove();
				} else if(dominates(t, b)) {
					iter.remove();
					b = t;
				}
			}
			results.add(b);
			iter = list.iterator();
			while (iter.hasNext()) { // Clean up
				RealVector t = iter.next();
				if (dominates(b, t)) {
					iter.remove();
				}
			}
		}
		
		return listOfRowsToMatrix(results);
	}

	private static RealMatrix listOfRowsToMatrix(LinkedList<RealVector> results) {
		RealMatrix res = new Array2DRowRealMatrix(results.size(), results.getFirst().getDimension());
		
		for (int i=0;i<results.size();i++) {
			res.setRowVector(i, results.get(i));
		}
		return res;
	}

	/**
	 * Checks whether v1 dominates v2.
	 * 
	 * @param v1
	 * @param v2
	 * @return true, if v1 \succ v2, false otherwise
	 */
	private static boolean dominates(RealVector v1, RealVector v2) {
		assert(v1.getDimension() == v2.getDimension());
		
		boolean largerFound = false;
		
		for (int i=0;i<v1.getDimension();i++) {
			if (v1.getEntry(i) > v2.getEntry(i)) {
				largerFound = true;
			} else if (v1.getEntry(i) < v2.getEntry(i)) {
				return false;
			}
		}
		return largerFound;
	}

	private static LinkedList<RealVector> matrixToListOfRows(RealMatrix mat) {
		LinkedList<RealVector> list = new LinkedList<RealVector>();
		for (int i=0;i<mat.getRowDimension();i++) {
			list.add(mat.getRowVector(i));
		}
		return list;
	}
}
