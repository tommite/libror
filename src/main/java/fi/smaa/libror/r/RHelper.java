package fi.smaa.libror.r;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class RHelper {

	public static RealMatrix rArrayMatrixToRealMatrix(double[] matrix, int nRows) {
		assert(nRows > 0);
		int nCols = matrix.length / nRows;
		assert(matrix.length == nRows * nCols);
		RealMatrix perfMatrix = new Array2DRowRealMatrix(nRows, nCols);
		for (int i=0;i<nRows;i++) {
			for (int j=0;j<nCols;j++) {
				perfMatrix.setEntry(i, j, matrix[i*nCols + j]);
			}
		}
		return perfMatrix;
	}

}
