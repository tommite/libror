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

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.common.ValueRanker;

public class RORSMAA {

	private MCValueFunctionSampler sampler;
	private RealMatrix poiMatrix;
	private RealMatrix raiMatrix;
	private RORModel model;

	public RORSMAA(RORModel model, MCValueFunctionSampler sampler) {
		this.model = model;
		this.sampler = sampler;
	}
	
	public RORModel getModel() {
		return model;
	}
		
	public MCValueFunctionSampler getSampler() {
		return sampler;
	}
	
	public void compute() throws SamplingException {
		int nrAlt = model.getNrAlternatives();		
		double[] evals = new double[nrAlt];
		int[][] poiHits = new int[nrAlt][nrAlt];
		int[][] raiHits = new int[nrAlt][nrAlt];
		int[] ranks = new int[nrAlt];
		
		sampler.sample();

		for (FullValueFunction vf : sampler.getValueFunctions()) {
			// evaluate all alts
			for (int i=0;i<nrAlt;i++) {
				evals[i] = vf.evaluate(model.getPerfMatrix().getLevelIndices(i));
			}
			// update poi hits
			for (int i=0;i<nrAlt;i++) {
				for (int j=0;j<nrAlt;j++) {
					if (evals[i] >= evals[j]) {
						poiHits[i][j]++;
					}		
				}
			}
			// update rai hits
			ValueRanker.rankValues(evals, ranks);
			for (int i=0;i<nrAlt;i++) {
				raiHits[i][ranks[i]]++;
			}
		}
		poiMatrix = new Array2DRowRealMatrix(nrAlt, nrAlt);
		raiMatrix = new Array2DRowRealMatrix(nrAlt, nrAlt);
		double divider = (double) sampler.getValueFunctions().length;
		for (int i=0;i<nrAlt;i++) {
			for (int j=0;j<nrAlt;j++) {
				poiMatrix.setEntry(i, j, (double) poiHits[i][j] / (double)divider);
				raiMatrix.setEntry(i, j, (double) raiHits[i][j] / (double)divider);
			}
		}
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
	
	/**
	 * PRECOND: compute() executed
	 * @return
	 */
	public RealMatrix getRAIs() {
		if (raiMatrix == null) {
			throw new IllegalStateException("compute() not called");
		}
		return raiMatrix;
	}	
}
