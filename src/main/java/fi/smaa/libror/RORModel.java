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
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class RORModel {
	
	protected class PrefPair {
		public PrefPair(int a2, int b2) {
			a = a2;
			b = b2;
		}
		public int a;
		public int b;
	}	

	protected RealMatrix perfMatrix;
	protected List<PrefPair> prefPairs = new ArrayList<PrefPair>();
	protected RealVector[] levels;

	protected RORModel(RealMatrix perfMatrix) {
		this.perfMatrix = perfMatrix;
		initializeLevels(perfMatrix);
	}

	public RealMatrix getPerfMatrix() {
		return perfMatrix;
	}

	protected void initializeLevels(RealMatrix perfMatrix) {
		levels = new RealVector[perfMatrix.getColumnDimension()];
		for (int i=0;i<levels.length;i++) {
			Set<Double> levelsSet = new TreeSet<Double>();
			for (double d : perfMatrix.getColumn(i)) {
				levelsSet.add(d);
			}
			RealVector lvl = new ArrayRealVector(levelsSet.toArray(new Double[0]));
			levels[i] = lvl;
		}
	}

	public RealVector[] getLevels() {
		return levels;
	}

	/**
	 * Adds a preference a >= b
	 * 
	 * @param a index of alternative that is preferred
	 * @param b index of the alternative that is not preferred
	 */
	public void addPreference(int a, int b) {
		prefPairs.add(new PrefPair(a, b));
	}

	public int getNrCriteria() {
		return perfMatrix.getColumnDimension();
	}

	public int getNrAlternatives() {
		return perfMatrix.getRowDimension();
	}

}