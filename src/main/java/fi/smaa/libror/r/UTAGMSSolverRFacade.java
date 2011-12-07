package fi.smaa.libror.r;

import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.libror.UTAGMSSolver;

public class UTAGMSSolverRFacade {

	private UTAGMSSolver solver;

	/**
	 * @param matrix matrix in row-major representation
	 * @param nRows > 0
	 */
	public UTAGMSSolverRFacade(double[] matrix, int nRows) {
		RealMatrix perfMatrix = RHelper.rArrayMatrixToRealMatrix(matrix, nRows);
		solver = new UTAGMSSolver(perfMatrix);
	}

	public void solve() {
		solver.solve();
	}
	
	public void printModel(boolean necessary, int a, int b) {
		solver.printModel(necessary, a, b);
	}
	
	public double[][] getNecessaryRelation() {
		return solver.getNecessaryRelation().getData();
	}
	
	public double[][] getPossibleRelation() {
		return solver.getPossibleRelation().getData();
	}	
}
