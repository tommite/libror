package fi.smaa.libror.r;

import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.libror.MaximalVectorComputation;

public class MaximalVectorComputationRFacade {

	private RealMatrix mat;

	public MaximalVectorComputationRFacade(double[] matrix, int nRows) {
		RealMatrix mat = RHelper.rArrayMatrixToRealMatrix(matrix, nRows);
		this.mat = mat;
	}
	
	public double[][] computeBEST() {
		return MaximalVectorComputation.computeBEST(mat).getData();
	}
}
