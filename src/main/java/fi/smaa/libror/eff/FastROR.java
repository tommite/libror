package fi.smaa.libror.eff;

import org.apache.commons.math.linear.RealMatrix;

import fi.smaa.libror.MaximalVectorComputation;
import fi.smaa.libror.RORModel;

public class FastROR {
	
	private RORModel ror;

	public FastROR(RORModel ror) {
		this.ror = ror;
	}

	public boolean[][] computeNecessary() {
		int nalts = ror.getNrAlternatives();
		boolean[][] nec = new boolean[nalts][nalts];
		
		for (int i=0;i<nalts;i++) {
			for (int j=0;j<nalts;j++) {
				if (j == i) {
					continue;
				}
				if (!nec[j][i]) {
					nec[i][j] = isNecessarilyPreferred(i, j);
				}
			}
		}
		return nec;
	}

	private boolean isNecessarilyPreferred(int i, int j) {
		RealMatrix mat = ror.getPerfMatrix().getMatrix();
		if (MaximalVectorComputation.dominates(
				mat.getRowVector(i), mat.getRowVector(j))) {
			return true;
		}
		return false;
	}
}
