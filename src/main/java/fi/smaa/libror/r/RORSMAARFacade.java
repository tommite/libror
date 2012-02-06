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

import fi.smaa.libror.CardinalPartialValueFunction;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.SamplingException;
import fi.smaa.libror.ValueFunctionSampler;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORSMAA;
import fi.smaa.libror.RejectionValueFunctionSampler;

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
		rorsmaa = new RORSMAA(model);
		rorsmaa.setSampler(sampler);
	}

	public String compute() {
		try {
			rorsmaa.compute();
			return "";
		} catch (SamplingException e) {
			return "Error sampling: " + e.getMessage();
		}
	}
	
	public double[] getValueFunctionVals(int vfIndex, int partialVfIndex) {
		CardinalPartialValueFunction vf = rorsmaa.getSampler().getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialVfIndex);		
		return vf.getVals();
	}
	
	public double[] getValueFunctionEvals(int vfIndex, int partialvfIndex) {
		CardinalPartialValueFunction vf = rorsmaa.getSampler().getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialvfIndex);		
		return vf.getEvals();
	}
	
	public int getNrValueFunctions() {
		return rorsmaa.getSampler().getValueFunctions().length;
	}
	
	public int getNrPartialValueFunctions() {
		return rorsmaa.getSampler().getValueFunctions()[0].getPartialValueFunctions().size();
	}
	
	public double evaluate(int vfIndex, double[] point) {
		return rorsmaa.getSampler().getValueFunctions()[vfIndex].evaluate(point);
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
		RealMatrix pm = model.getPerfMatrix().getMatrix();
		double[] alt = pm.getRow(alternative);
		return rorsmaa.getSampler().getValueFunctions()[vfIndex].evaluate(alt);
	}
	
	public int getMisses() {
		if (rorsmaa.getSampler() instanceof RejectionValueFunctionSampler) {
			ValueFunctionSampler s = (ValueFunctionSampler) rorsmaa.getSampler();
			return s.getMisses();
		}
		return 0;
	}
	
	public double[][] getRAIs() {
		return rorsmaa.getRAIs().getData();
	}

	public double[][] getPOIs() {
		return rorsmaa.getPOIs().getData();
	}

}
