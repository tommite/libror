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

package fi.smaa.libror.eff;

import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.libror.MaximalVectorComputation;
import fi.smaa.libror.RORModel;

public class FastROR {
	
	private RORModel ror;

	public FastROR(RORModel ror) {
		this.ror = ror;
	}

	public boolean[][] computeNecessary() {
		int nalts = ror.getNrAlternatives();
		boolean[][] nec = new boolean[nalts][nalts];
		
		for (int i=0;i<nalts;i++) {
			for (int j=0;j<nalts;j++) {
				if (j == i) {
					continue;
				}
				if (!nec[j][i]) {
					nec[i][j] = isNecessarilyPreferred(i, j);
				}
			}
		}
		return nec;
	}

	private boolean isNecessarilyPreferred(int i, int j) {
		RealMatrix mat = ror.getPerfMatrix().getMatrix();
		if (MaximalVectorComputation.dominates(
				mat.getRowVector(i), mat.getRowVector(j))) {
			return true;
		}
		return false;
	}
}
