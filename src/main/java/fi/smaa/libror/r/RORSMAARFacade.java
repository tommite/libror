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

package fi.smaa.libror.r;

import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.RORSMAA;
import fi.smaa.libror.RejectionValueFunctionSampler;
import fi.smaa.libror.SamplingException;

public class RORSMAARFacade extends RORRFacade {
	
	private static final int NR_ITERS = 10000;
	private RORSMAA rorsmaa;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 * @param count the amount of functions to sample, > 0
	 */
	public RORSMAARFacade(double[] matrix, int nRows) {
		super(new RORModel(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		RejectionValueFunctionSampler sampler = new RejectionValueFunctionSampler(model, NR_ITERS);
		rorsmaa = new RORSMAA(model, sampler);
	}

	public String compute() {
		try {
			rorsmaa.compute();
			return "";
		} catch (SamplingException e) {
			return "Error sampling: " + e.getMessage();
		}
	}
				
	/**
	 * 
	 * @param vfIndex PRECOND: >= 0
	 * @param alternative PRECOND: >= 0
	 * @return
	 */
	public double evaluateAlternative(int vfIndex, int alternative) {
		if (alternative < 0) {
			throw new IllegalArgumentException("alternative < 0");
		}
		if (vfIndex < 0) {
			throw new IllegalArgumentException("vfIndex < 0");
		}
		return rorsmaa.getSampler().getValueFunctions()[vfIndex].evaluate(model.getPerfMatrix().getLevelIndices(alternative));
	}
	
	public int getMisses() {
		return rorsmaa.getSampler().getMisses();
	}
	
	public double[][] getRAIs() {
		return rorsmaa.getRAIs().getData();
	}

	public double[][] getPOIs() {
		return rorsmaa.getPOIs().getData();
	}

}
