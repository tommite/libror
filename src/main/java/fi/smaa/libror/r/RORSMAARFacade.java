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

import fi.smaa.libror.GeneralValueFunctionSampler;
import fi.smaa.libror.PartialValueFunction;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORSMAA;

public class RORSMAARFacade extends RORRFacade<RORSMAA> {
	
	private static final int NR_ITERS = 10000;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 * @param count the amount of functions to sample, > 0
	 */
	public RORSMAARFacade(double[] matrix, int nRows) {
		super(new RORSMAA(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		GeneralValueFunctionSampler sampler = new GeneralValueFunctionSampler(model, NR_ITERS);
		model.setSampler(sampler);
	}

	public void compute() {
		model.compute();
	}
	
	public double[] getValueFunctionVals(int vfIndex, int partialVfIndex) {
		PartialValueFunction vf = model.getSampler().getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialVfIndex);		
		return vf.getVals();
	}
	
	public double[] getValueFunctionEvals(int vfIndex, int partialvfIndex) {
		PartialValueFunction vf = model.getSampler().getValueFunctions()[vfIndex].getPartialValueFunctions().get(partialvfIndex);		
		return vf.getEvals();
	}
	
	public int getNrValueFunctions() {
		return model.getSampler().getValueFunctions().length;
	}
	
	public int getNrPartialValueFunctions() {
		return model.getSampler().getValueFunctions()[0].getPartialValueFunctions().size();
	}
	
	public double evaluate(int vfIndex, double[] point) {
		return model.getSampler().getValueFunctions()[vfIndex].evaluate(point);
	}
	
	public double evaluateAlternative(int vfIndex, int alternative) {
		assert(alternative >= 0);
		RealMatrix pm = model.getPerfMatrix().getMatrix();
		double[] alt = pm.getRow(alternative);
		return model.getSampler().getValueFunctions()[vfIndex].evaluate(alt);
	}
	
	public int getMisses() {
		if (model.getSampler() instanceof GeneralValueFunctionSampler) {
			GeneralValueFunctionSampler s = (GeneralValueFunctionSampler) model.getSampler();
			return s.getMisses();
		}
		return 0;
	}
	
	public double[][] getRAIs() {
		return model.getRAIs().getData();
	}

	public double[][] getPOIs() {
		return model.getPOIs().getData();
	}

}
