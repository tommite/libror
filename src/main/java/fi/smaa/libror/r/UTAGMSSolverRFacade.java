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

package fi.smaa.libror.r;

import fi.smaa.libror.UTAGMSSolver;

public class UTAGMSSolverRFacade extends RORRFacade<UTAGMSSolver>{

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 */
	public UTAGMSSolverRFacade(double[] matrix, int nRows) {
		super(new UTAGMSSolver(RHelper.rArrayMatrixToRealMatrix(matrix, nRows)));
	}

	public void solve() {
		model.solve();
	}
	
	public void printModel(boolean necessary, int a, int b) {
		model.printModel(necessary, a, b);
	}
	
	public double[][] getNecessaryRelation() {
		return model.getNecessaryRelation().getData();
	}
	
	public double[][] getPossibleRelation() {
		return model.getPossibleRelation().getData();
	}	
}
