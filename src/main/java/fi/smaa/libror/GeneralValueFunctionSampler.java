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

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.random.MersenneTwister;

public class GeneralValueFunctionSampler extends RORModel {
	
	private FullValueFunction[] vfs;
	private MersenneTwister rng = new MersenneTwister(0x667);
	private double[] w;
	private int misses;
	
	/**
	 * Construct a new sampler with the given performance matrix. The alternatives are in rows, and evaluations in columns.
	 *  
	 * @param perfMatrix the performanceMatrix to use.
	 * @param count the amount of functions to sample, > 0
	 */
	public GeneralValueFunctionSampler(RealMatrix perfMatrix, int count) {
		super(perfMatrix);
		w = new double[getNrCriteria()];
		vfs = new FullValueFunction[count];		
	}
		
	/**
	 * Samples partial value functions.
	 * 
	 */
	public void sample() {		
		misses = 0;
		for (int i=0;i<vfs.length;i++) {
			while (true) {
				FullValueFunction vf = sampleValueFunction();
				if (isHit(vf)) {
					vfs[i] = vf;
					break;
				} else {
					misses++;
				}
			}
		}
	}
	
	public int getMisses() {
		return misses;
	}
	
	private boolean isHit(FullValueFunction vf) {
		double[] values = new double[getNrAlternatives()];	
		for (int i=0;i<values.length;i++) {
			values[i] = vf.evaluate(perfMatrix.getRow(i));
		}
		
		for (PrefPair p : prefPairs) {
			if (values[p.a]< values[p.b]) {
				return false;
			}
		}
		
		return true;
	}

	public FullValueFunction[] getValueFunctions() {
		return vfs;
	}
	
	private FullValueFunction  sampleValueFunction() {
		FullValueFunction vf = new FullValueFunction();
		
		List<double[]> partVals = new ArrayList<double[]>();		
		List<double[]> partEvals = new ArrayList<double[]>();
		
		for (int i=0;i<getNrCriteria();i++) {
			double[] vals = levels[i].getData();
			partVals.add(vals);
			partEvals.add(createPartialValues(vals.length));	
		}
		
		// sample weights
		RandomUtil.createSumToOneRand(w);
		
		// scale the partial value functions with weights
		for (int i=0;i<getNrCriteria();i++) {
			double[] evals = partEvals.get(i);
			for (int j=0;j<evals.length;j++) {
				evals[j] *= w[i];
			}
			vf.addValueFunction(new PartialValueFunction(partVals.get(i), evals));
		}
		
		return vf;
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
