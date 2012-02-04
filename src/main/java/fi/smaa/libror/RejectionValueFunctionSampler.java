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
import java.util.Arrays;
import java.util.List;

import fi.smaa.libror.RORModel.PrefPair;


public class RejectionValueFunctionSampler extends ValueFunctionSampler {

	protected FullCardinalValueFunction[] vfs;
	private int maxTries = Integer.MAX_VALUE;

	/**
	 * Construct a new sampler with the given performance matrix. The alternatives are in rows, and evaluations in columns.
	 *  
	 * @param perfMatrix the performanceMatrix to use.
	 * @param count the amount of functions to sample, > 0
	 */
	public RejectionValueFunctionSampler(RORModel model, int count) {
		super(model);
		vfs = new FullCardinalValueFunction[count];
	}
	
	public RejectionValueFunctionSampler(RORModel model, int count, int maxIters) {
		super(model);
		vfs = new FullCardinalValueFunction[count];
		maxTries = maxIters;
	}
	
	public FullCardinalValueFunction[] getValueFunctions() {
		if (vfs == null) {
			throw new IllegalStateException("sample() not called");
		}
		return vfs;
	}

		
	public void doSample() throws SamplingException {
		misses = 0;
		for (int i=0;i<vfs.length;i++) {
			int currentTry = 0;
			while (currentTry < maxTries) {
				FullCardinalValueFunction vf = sampleValueFunction();
				if (isHit(vf)) {
					vfs[i] = vf;
					break;
				} else {
					misses++;
				}
				currentTry++;
			}
			if (currentTry == maxTries) {
				throw new SamplingException("No sample found within " + maxTries + " rejection iterations");
			}
		}
	}

	
	private FullCardinalValueFunction  sampleValueFunction() {
		FullCardinalValueFunction vf = new FullCardinalValueFunction();
		
		List<double[]> partVals = new ArrayList<double[]>();		
		List<double[]> partEvals = new ArrayList<double[]>();
		
		for (int i=0;i<model.getNrCriteria();i++) {
			double[] vals = model.getPerfMatrix().getLevels()[i].getData();
			partVals.add(vals);
			partEvals.add(createPartialValues(vals.length));	
		}
		
		
		sampleWeights();
		
		// scale the partial value functions with weights
		for (int i=0;i<model.getNrCriteria();i++) {
			double[] evals = partEvals.get(i);
			for (int j=0;j<evals.length;j++) {
				evals[j] *= w[i];
			}
			vf.addValueFunction(new CardinalPartialValueFunction(partVals.get(i), evals));
		}
		
		return vf;
	}
	
	private boolean isHit(FullCardinalValueFunction vf) {
		double[] values = new double[model.getNrAlternatives()];	
		for (int i=0;i<values.length;i++) {
			values[i] = vf.evaluate(model.getPerfMatrix().getMatrix().getRow(i));
		}
		
		for (PrefPair p : model.getPrefPairs()) {
			if (values[p.a]< values[p.b]) {
				return false;
			}
		}
		
		return true;
	}

	private double[] createPartialValues(int length) {
		double[] vals = new double[length];
		vals[0] = 0.0;
		for (int i=1;i<vals.length-1;i++) {
			vals[i] = rng.nextDouble();
		}
		vals[vals.length-1] = 1.0;
		Arrays.sort(vals);
		return vals;
	}
}
