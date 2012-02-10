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

import java.util.Arrays;

import org.apache.commons.math.linear.RealVector;


public class RejectionValueFunctionSampler extends MCValueFunctionSampler {

	private int maxTries = 10000000;

	/**
	 * Construct a new sampler with the given performance matrix. The alternatives are in rows, and evaluations in columns.
	 *  
	 * @param perfMatrix the performanceMatrix to use.
	 * @param count the amount of functions to sample, > 0
	 */
	public RejectionValueFunctionSampler(RORModel model, int count) {
		super(model, count);
	}
	
	public RejectionValueFunctionSampler(RORModel model, int count, int maxIters) {
		super(model, count);
		maxTries = maxIters;
	}
	
	public int getMaxIters() {
		return maxTries;
	}
	
	public FullValueFunction[] getValueFunctions() {
		if (vfs == null) {
			throw new IllegalStateException("sample() not called");
		}
		return vfs;
	}
		
	private FullValueFunction sampleValueFunction() throws SamplingException {			
		int curIter = 1;
		
		do {
			FullValueFunction vf = new FullValueFunction();
			for (int i=0;i<model.getNrCriteria();i++) {
				RealVector lvls = model.getPerfMatrix().getLevels()[i];
				PartialValueFunction pvf = new PartialValueFunction(lvls.getDimension());
				vf.addValueFunction(pvf);
				sampleRandomPartialVF(pvf);
			}

			double[] w = new double[model.getNrCriteria()];
			RandomUtil.createSumToOneRand(w);
			vf.setWeights(w);
			if (acceptance.check(vf)) {
				return vf;
			}
			misses++;
			curIter++;
		} while (curIter <= maxTries);
		throw new SamplingException("Cannot sample a VF within " + maxTries + " iterations");
	}
	
	private void sampleRandomPartialVF(PartialValueFunction pvf) {
		double[] vals = pvf.getValues();
		for (int i=1;i<vals.length-1;i++) {
			vals[i] = RandomUtil.createUnif01();
		}
		Arrays.sort(vals);
	}

	public void doSample() throws SamplingException {
		for (int i=0;i<vfs.length;i++) {
			vfs[i] = sampleValueFunction();
		}
	}

}
