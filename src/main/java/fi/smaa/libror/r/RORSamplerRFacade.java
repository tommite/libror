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

import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.libror.PartialValueFunction;
import fi.smaa.libror.RORValueFunctionSampler;

public class RORSamplerRFacade {
	
	private RORValueFunctionSampler sampler;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 * @param count the amount of functions to sample, > 0
	 */
	public RORSamplerRFacade(double[] matrix, int nRows, int count) {
		RealMatrix perfMatrix = RHelper.rArrayMatrixToRealMatrix(matrix, nRows);
		sampler = new RORValueFunctionSampler(perfMatrix, count);
	}

	public void sample() {
		sampler.sample();
	}
	
	public double[] getValueFunctionVals(int vfIndex, int partialVfIndex) {
		PartialValueFunction vf = sampler.getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialVfIndex);		
		return vf.getVals();
	}
	
	public double[] getValueFunctionEvals(int vfIndex, int partialvfIndex) {
		PartialValueFunction vf = sampler.getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialvfIndex);		
		return vf.getEvals();
	}
	
	public int getNrValueFunctions() {
		return sampler.getValueFunctions().length;
	}
	
	public int getNrPartialValueFunctions() {
		return sampler.getValueFunctions()[0].getPartialValueFunctions().size();
	}
	
	public double evaluate(int vfIndex, double[] point) {
		return sampler.getValueFunctions()[vfIndex].evaluate(point);
	}
	
	public double evaluateAlternative(int vfIndex, int alternative) {
		assert(alternative >= 0);
		RealMatrix pm = sampler.getPerfMatrix();
		double[] alt = pm.getRow(alternative);
		return sampler.getValueFunctions()[vfIndex].evaluate(alt);
	}
	
	public void addPreference(int a, int b) {
		sampler.addPreference(a, b);
	}
	
	public int getMisses() {
		return sampler.getMisses();
	}
}
