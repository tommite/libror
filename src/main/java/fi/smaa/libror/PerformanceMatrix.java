package fi.smaa.libror;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class PerformanceMatrix {

	private RealMatrix matrix;
	private RealVector[] levels;
	private List<int[]> levelIndices = new ArrayList<int[]>();
	private List<int[]> altLevelIndices = new ArrayList<int[]>();

	public PerformanceMatrix(RealMatrix matrix) {
		this.matrix = matrix;
		initializeLevels(matrix);
		initializeLevelIndices();
		initializeAltLevelIndices();
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
	
	private void initializeLevelIndices() {
		for (int i=0;i<getNrCrit();i++) {
			SortedSet<Double> s = new TreeSet<Double>();
			for (Double d : levels[i].toArray()) {
				s.add(d);
			}
			Double[] arr = s.toArray(new Double[0]);
			int[] colIndices = new int[getNrAlts()];
			for (int j=0;j<getNrAlts();j++) {
				for (int k=0;k<arr.length;k++) {
					if (arr[k] == matrix.getEntry(j, i)) {
						colIndices[j] = k;
						break;
					}
				}
			}
			levelIndices.add(colIndices);
		}
	}
	
	public RealMatrix getMatrix() {
		return matrix;
	}

	public RealVector[] getLevels() {
		return levels;
	}
	
	public int getLevelIndex(int altIndex, int critIndex) {
		return levelIndices.get(critIndex)[altIndex];
	}

	public int[] getLevelIndices(int altIndex) {
		return altLevelIndices.get(altIndex);
	}
	
	private void initializeAltLevelIndices() {
		for (int i=0;i<getNrAlts();i++) {
			int[] lvls = new int[getNrCrit()];
			for (int j=0;j<lvls.length;j++) {
				lvls[j] = getLevelIndex(i, j);
			}
			altLevelIndices.add(lvls);
		}
	}

	private int getNrAlts() {
		return matrix.getRowDimension();
	}

	private int getNrCrit() {
		return matrix.getColumnDimension();
	}
}
