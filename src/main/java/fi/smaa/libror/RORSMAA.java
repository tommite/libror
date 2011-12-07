package fi.smaa.libror;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class RORSMAA {

	private static final int NR_ITERS = 10000;
	private RealMatrix perfMatrix;
	private RORValueFunctionSampler sampler;
	private RealMatrix raiMatrix;
	private RealMatrix poiMatrix;

	public RORSMAA(RealMatrix perfMatrix) {
		this.perfMatrix = perfMatrix;
		this.sampler = new RORValueFunctionSampler(perfMatrix, NR_ITERS);
	}
	
	public void compute() {
		sampler.sample();
		computeRankAcceptabilities();
		computePairwiseIndices();
	}

	private void computePairwiseIndices() {
		poiMatrix = new Array2DRowRealMatrix(perfMatrix.getRowDimension(), perfMatrix.getColumnDimension());
		for (int i=0;i<sampler.getNrAlternatives();i++) {
			for (int j=0;j<sampler.getNrCriteria();j++) {
				if (i == j) {
					poiMatrix.setEntry(i, j, 1.0);
				} else if (i < j) {
					poiMatrix.setEntry(i, j, computePoi(i, j));
				}
			}
		}
		for (int i=0;i<sampler.getNrAlternatives();i++) {
			for (int j=0;j<sampler.getNrCriteria();j++) {
				if (i > j) {
					poiMatrix.setEntry(i, j, poiMatrix.getEntry(j, i));
				}
			}
		}
	}

	private double computePoi(int i, int j) {
		int iHits = 0;
		double[] iVal = perfMatrix.getRow(i);
		double[] jVal = perfMatrix.getRow(j);
		for (FullValueFunction vf : sampler.getValueFunctions()) {
			if (vf.evaluate(iVal) >= vf.evaluate(jVal)) {
				iHits++;
			}
		}
		return iHits / (double) sampler.getValueFunctions().length;
	}

	private void computeRankAcceptabilities() {
		raiMatrix = new Array2DRowRealMatrix(perfMatrix.getRowDimension(), perfMatrix.getColumnDimension());
	}
}
