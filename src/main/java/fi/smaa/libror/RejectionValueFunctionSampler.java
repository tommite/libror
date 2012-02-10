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

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.random.MersenneTwister;

import fi.smaa.libror.RORModel.PrefPair;


public class RejectionValueFunctionSampler {

	protected WeightedOrdinalValueFunction[] vfs;
	private int maxTries = Integer.MAX_VALUE;
	protected int misses;
	protected double[] w;
	protected MersenneTwister rng = new MersenneTwister(0x667);
	protected RORModel model;

	/**
	 * Construct a new sampler with the given performance matrix. The alternatives are in rows, and evaluations in columns.
	 *  
	 * @param perfMatrix the performanceMatrix to use.
	 * @param count the amount of functions to sample, > 0
	 */
	public RejectionValueFunctionSampler(RORModel model, int count) {
		this.model = model;
		misses = 0;
		w = new double[model.getNrCriteria()];
		vfs = new WeightedOrdinalValueFunction[count];
	}
	
	public RejectionValueFunctionSampler(RORModel model, int count, int maxIters) {
		this(model, count);
		maxTries = maxIters;
	}
	
	public WeightedOrdinalValueFunction[] getValueFunctions() {
		if (vfs == null) {
			throw new IllegalStateException("sample() not called");
		}
		return vfs;
	}

		
	private WeightedOrdinalValueFunction  sampleValueFunction() {
		WeightedOrdinalValueFunction vf = new WeightedOrdinalValueFunction();
			
		for (int i=0;i<model.getNrCriteria();i++) {
			RealVector lvls = model.getPerfMatrix().getLevels()[i];
			OrdinalPartialValueFunction pvf = new OrdinalPartialValueFunction(lvls.getDimension());
			vf.addValueFunction(pvf);
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
	
	private boolean isHit(WeightedOrdinalValueFunction vf) {
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

	public int getMisses() {
		return misses;
	}

	protected void sampleWeights() {
		RandomUtil.createSumToOneRand(w);
	}
}
