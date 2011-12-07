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
