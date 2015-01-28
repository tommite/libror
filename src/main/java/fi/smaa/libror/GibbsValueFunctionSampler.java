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

package fi.smaa.libror;

import java.util.List;

import org.apache.commons.math.linear.RealVector;


public class GibbsValueFunctionSampler extends MCValueFunctionSampler {
	
	public static final int MAX_STARTINGPOINT_ITERS = RejectionValueFunctionSampler.DEFAULT_MAX_ITERS;
	private int thinning;
	private FullValueFunction startingPoint;
	
	public GibbsValueFunctionSampler(RORModel model, int count, int thinning, FullValueFunction startingPoint)
	throws InvalidStartingPointException {
		super(model, count);
		checkStartingPoint(startingPoint);
		init(thinning, startingPoint);
	}
	
	public GibbsValueFunctionSampler(RORModel model, int count, int thinning) throws SamplingException {
		super(model, count);
		startingPoint = generateStartingPoint();
		init(thinning, startingPoint);
	}	
	
	private void init(int thinning, FullValueFunction startingPoint) {
		if (thinning < 1) {
			throw new IllegalArgumentException("PRECOND violated: thinning < 1");
		}
		this.startingPoint = startingPoint;
		this.thinning = thinning;
	}
	
	private void checkStartingPoint(FullValueFunction p) throws InvalidStartingPointException {
		if (!p.areValidWeights()) {
			throw new InvalidStartingPointException("Weights not summing to 1.0");
		}
		List<PartialValueFunction> pvfs = p.getPartialValueFunctions();
		RealVector[] lvls = model.getPerfMatrix().getLevels();
		if (lvls.length != pvfs.size()) {
			throw new InvalidStartingPointException("Incorrect amount of partial value functions");
		}
		for (int i=0;i<lvls.length;i++) {
			if (pvfs.get(i).getValues().length != lvls[i].getDimension()) {
				throw new InvalidStartingPointException("Incorrect amount of levels in partial value function #" + (i+1));
			}
		}
	}
	
	public int getThinning() {
		return thinning;
	}
	
	private double samplePoint(int ind, PartialValueFunction vf) {
		double[] vals = vf.getValues();
		double lb = vals[ind-1];
		double ub = vals[ind+1];
		
		return lb + (RandomUtil.createUnif01() * (ub - lb));
	}

	private void setLastWeight(FullValueFunction vf) {
		double[] w = vf.getWeights();
		double sum = 0.0;
		for (int i=0;i<w.length-1;i++) {
			sum += w[i];
		}
		vf.setWeight(w.length-1, 1.0 - sum);
	}

	private double sampleWeight(int curPartVFind, double[] w) {
		double sum = 0.0;
		for (int i=0;i<w.length-1;i++) {
			if (i != curPartVFind) {
				sum += w[i];
			}
		}		
		return RandomUtil.createUnif01() * (1.0 - sum);
	}

	private int[] getPartialVFSizes(List<PartialValueFunction> levels) {
		int[] sizes = new int[levels.size()];
		for (int i=0;i<levels.size();i++) {
			sizes[i] = levels.get(i).getValues().length;
		}
		return sizes;
	}

	public FullValueFunction generateStartingPoint() throws SamplingException {
		RejectionValueFunctionSampler sampler = new RejectionValueFunctionSampler(model, 1, MAX_STARTINGPOINT_ITERS);
		sampler.sample();
		return sampler.getValueFunctions()[0];
	}

	public FullValueFunction getStartingPoint() {
		return startingPoint;
	}

	public void doSample() throws SamplingException {
		FullValueFunction currentVF = startingPoint.deepCopy();
			
		int nrPartVF = currentVF.getPartialValueFunctions().size();
		int[] sizPartVF = getPartialVFSizes(currentVF.getPartialValueFunctions());
		
		int curVFind = 0;
		int curPartVFind = 1; // curPartVF == (amount of part VFs) means to sample the weight
		// and 0 is always 0.0 value so we don't sample it 
				
		vfs[0] = currentVF.deepCopy();
		int iter = 1;
		int index = 1;
		
		while (index < vfs.length) {
			boolean store = false;
			if (curPartVFind == sizPartVF[curVFind]-1) { // update weight
				// if it's the last one, don't do anything (it's dependent on others as \sum_i w_i=1
				if (curVFind != (nrPartVF-1)) {
					assert(curVFind < (nrPartVF-1));
					double[] weights = currentVF.getWeights();
					double newW = sampleWeight(curVFind, weights);
					double[] oldWeights = ArrayCopy.copyOf(weights);
					currentVF.setWeight(curVFind, newW);
					setLastWeight(currentVF); // set last weight so that they sum to 1.0
					if (!acceptance.check(currentVF)) {
						currentVF.setWeights(oldWeights);
						misses++;
					}
					store = true;
				}
			} else { // update characteristic point
				PartialValueFunction vf = currentVF.getPartialValueFunctions().get(curVFind);
				double oldPoint = vf.getValues()[curPartVFind];
				double point = samplePoint(curPartVFind, vf);
				vf.setValue(curPartVFind, point);
				if (!acceptance.check(currentVF)) {
					vf.setValue(curPartVFind, oldPoint);
					misses++;
				}
				store = true;
			}
			
			// UPDATE INDICES
			if (curPartVFind < sizPartVF[curVFind]-1) {
				curPartVFind++;
			}
			else { // advance to next partial vf
				curPartVFind = 1;
				curVFind = (curVFind + 1) % nrPartVF;
			}
			if (store) {
				if (iter % thinning == 0) {
					vfs[index] = currentVF.deepCopy();
					if (updateInterval > 0 && (index+1) % updateInterval == 0) {
						listener.update(index+1);
					}
					index++;
				}
				iter++;
			}
		}
	}


}
