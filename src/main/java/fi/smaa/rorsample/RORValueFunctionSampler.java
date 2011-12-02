package fi.smaa.rorsample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.random.MersenneTwister;

public class RORValueFunctionSampler {
	
	private RealMatrix perfMatrix;
	private List<PrefPair> prefPairs = new ArrayList<PrefPair>();
	private RealVector[] levels;
	private FullValueFunction[] vfs;
	
	MersenneTwister rng = new MersenneTwister(0x667);
	double[] w;
	private int misses;
	
	private class PrefPair {
		public PrefPair(int a2, int b2) {
			a = a2;
			b = b2;
		}
		public int a;
		public int b;
	}

	/**
	 * Construct a new sampler with the given performance matrix. The alternatives are in rows, and evaluations in columns.
	 *  
	 * @param perfMatrix the performanceMatrix to use.
	 * @param count the amount of functions to sample, > 0
	 */
	public RORValueFunctionSampler(RealMatrix perfMatrix, int count) {
		init(perfMatrix, count);
	}
		
	private void init(RealMatrix perfMatrix, int count) {
		this.perfMatrix = perfMatrix;
		initializeLevels(perfMatrix);		
		w = new double[getNrCriteria()];
		vfs = new FullValueFunction[count];
	}
	
	public RealMatrix getPerfMatrix() {
		return perfMatrix;
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
	
	public RealVector[] getLevels() {
		return levels;
	}
	

	/**
	 * Adds a preference a >= b
	 * 
	 * @param a index of alternative that is preferred
	 * @param b index of the alternative that is not preferred
	 */
	public void addPreference(int a, int b) {
		prefPairs.add(new PrefPair(a, b));
	}
	
	/**
	 * Samples partial value functions.
	 * 
	 */
	public void sample() {		
		misses = 0;
		for (int i=0;i<vfs.length;i++) {
			while (true) {
				FullValueFunction vf = sampleValueFunction();
				if (isHit(vf)) {
					vfs[i] = vf;
					break;
				} else {
					misses++;
				}
			}
		}
	}
	
	public int getMisses() {
		return misses;
	}
	
	private boolean isHit(FullValueFunction vf) {
		double[] values = new double[getNrAlternatives()];	
		for (int i=0;i<values.length;i++) {
			values[i] = vf.evaluate(perfMatrix.getRow(i));
		}
		
		for (PrefPair p : prefPairs) {
			if (values[p.a]< values[p.b]) {
				return false;
			}
		}
		
		return true;
	}

	public FullValueFunction[] getValueFunctions() {
		return vfs;
	}
	
	public int getNrCriteria() {
		return perfMatrix.getColumnDimension();
	}
	
	public int getNrAlternatives() {
		return perfMatrix.getRowDimension();
	}

	private FullValueFunction  sampleValueFunction() {
		FullValueFunction vf = new FullValueFunction();
		
		List<double[]> partVals = new ArrayList<double[]>();		
		List<double[]> partEvals = new ArrayList<double[]>();
		
		for (int i=0;i<getNrCriteria();i++) {
			double[] vals = levels[i].getData();
			partVals.add(vals);
			partEvals.add(createPartialValues(vals.length));	
		}
		
		// sample weights
		RandomUtil.createSumToOneRand(w);
		
		// scale the partial value functions with weights
		for (int i=0;i<getNrCriteria();i++) {
			double[] evals = partEvals.get(i);
			for (int j=0;j<evals.length;j++) {
				evals[j] *= w[i];
			}
			vf.addValueFunction(new PartialValueFunction(partVals.get(i), evals));
		}
		
		return vf;
	}

	private double[] createPartialValues(int length) {
		double[] vals = new double[length];
		vals[0] = 0.0;
		for (int i=1;i<vals.length-1;i++) {
			vals[i] = rng.nextDouble();
		}
		vals[vals.length-1] = 1.0;
		Arrays.sort(vals);
		return vals;
	}
}
