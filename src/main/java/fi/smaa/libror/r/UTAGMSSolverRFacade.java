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

package fi.smaa.libror.r;

import fi.smaa.libror.InfeasibleConstraintsException;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.UTAGMSSolver;

public class UTAGMSSolverRFacade extends RORRFacade {

	private UTAGMSSolver solver;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 */
	public UTAGMSSolverRFacade(double[] matrix, int nRows) {
		super(new RORModel(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		solver = new UTAGMSSolver(this.model);
	}
	
	public void setStrictValueFunctions(boolean strict) {
		solver.setStrictValueFunctions(strict);
	}
	
	public int solve() {
		try {
			solver.solve();
		} catch (InfeasibleConstraintsException e) {
			return 0;
		}
		return 1;
	}
	
	public void printModel(boolean necessary, int a, int b) {
		solver.printModel(necessary, a, b);
	}
	
	public double[][] getNecessaryRelation() {
		return solver.getNecessaryRelation().getData();
	}
	
	public double[][] getPossibleRelation() {
		return solver.getPossibleRelation().getData();
	}	
}
