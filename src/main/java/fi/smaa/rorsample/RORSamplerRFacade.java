package fi.smaa.rorsample;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class RORSamplerRFacade {
	
	private RORValueFunctionSampler sampler;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows
	 * @param count the amount of functions to sample, > 0
	 */
	public RORSamplerRFacade(double[] matrix, int nRows, int count) {
		int nCols = matrix.length / nRows;
		assert(matrix.length == nRows * nCols);
		RealMatrix perfMatrix = new Array2DRowRealMatrix(nRows, nCols);
		for (int i=0;i<nRows;i++) {
			for (int j=0;j<nCols;j++) {
				perfMatrix.setEntry(i, j, matrix[i*nCols + j]);
			}
		}
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

}
