/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-13 Tommi Tervonen.
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

public class RORModel {
	
	public class PrefPair {
		public PrefPair(int a2, int b2) {
			a = a2;
			b = b2;
		}
		public int a;
		public int b;
	}	

	private PerformanceMatrix perfMatrix;
	private List<PrefPair> prefPairs = new ArrayList<PrefPair>();

	public RORModel(PerformanceMatrix perfMatrix) {
		this.perfMatrix = perfMatrix;
	}

	public PerformanceMatrix getPerfMatrix() {
		return perfMatrix;
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
		return perfMatrix.getMatrix().getColumnDimension();
	}

	public int getNrAlternatives() {
		return perfMatrix.getMatrix().getRowDimension();
	}

	public List<PrefPair> getPrefPairs() {
		return prefPairs;
	}
	
	

}