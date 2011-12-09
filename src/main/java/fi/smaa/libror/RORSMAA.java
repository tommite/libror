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

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class RORSMAA extends UTAGMSSolver {

	private static final int NR_ITERS = 10000;
	private GeneralValueFunctionSampler sampler;
	private RealMatrix poiMatrix;

	public RORSMAA(RealMatrix perfMatrix) {
		super(perfMatrix);
		this.sampler = new GeneralValueFunctionSampler(perfMatrix, NR_ITERS);
	}
	
	public void compute() {
		sampler.sample();
		int nrAlt = getNrAlternatives();		
		double[] evals = new double[nrAlt];
		int[][] poiHits = new int[nrAlt][nrAlt];

		for (FullValueFunction vf : sampler.getValueFunctions()) {
			// evaluate all alts
			for (int i=0;i<nrAlt;i++) {
				evals[i] = vf.evaluate(perfMatrix.getRow(i));
			}
			// update poi hits
			for (int i=0;i<nrAlt;i++) {
				for (int j=0;j<nrAlt;j++) {
					if (evals[i] >= evals[j]) {
						poiHits[i][j]++;
					}		
				}
			}
		}
		poiMatrix = new Array2DRowRealMatrix(nrAlt, nrAlt);
		double divider = (double) sampler.getValueFunctions().length;
		for (int i=0;i<nrAlt;i++) {
			for (int j=0;j<nrAlt;j++) {
				poiMatrix.setEntry(i, j, (double) poiHits[i][j] / (double)divider);
			}
		}
	}
	
	@Override
	public void addPreference(int a, int b) {
		super.addPreference(a, b);
		sampler.addPreference(a, b);
	}
	
	/**
	 * PRECOND: compute() executed
	 * @return
	 */
	public RealMatrix getPOIs() {
		if (poiMatrix == null) {
			throw new IllegalStateException("compute() not called");
		}
		return poiMatrix;
	}
}
