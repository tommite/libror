package fi.smaa.libror;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class PerformanceMatrix {

	private RealMatrix matrix;
	private RealVector[] levels;
	

	public PerformanceMatrix(RealMatrix matrix) {
		this.matrix = matrix;
		initializeLevels(matrix);
	}
	
	private void initializeLevels(RealMatrix perfMatrix) {
		levels = new RealVector[perfMatrix.getColumnDimension()];
		for (int i=0;i<levels.length;i++) {
			Set<Double> levelsSet = new TreeSet<Double>();
			for (double d : perfMatrix.getColumn(i)) {
				levelsSet.add(d);
			}
			RealVector lvl = new ArrayRealVector(levelsSet.toArray(new Double[0]));
			levels[i] = lvl;
		}
	}
	
	public RealMatrix getMatrix() {
		return matrix;
	}

	public RealVector[] getLevels() {
		return levels;
	}

}
