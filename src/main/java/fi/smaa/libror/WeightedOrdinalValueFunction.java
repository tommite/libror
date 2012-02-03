package fi.smaa.libror;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightedOrdinalValueFunction implements DeepCopiable<WeightedOrdinalValueFunction> {
	public static final double WTOL = 0.01;
	
	private double[] weights = new double[0];
	private List<OrdinalPartialValueFunction> vfs = new ArrayList<OrdinalPartialValueFunction>();

	public void addValueFunction(OrdinalPartialValueFunction v) {
		vfs.add(v);
		reinitWeights();
	}
	
	public double evaluate(int[] indices) {
		double sum = 0.0;
		for (int i=0;i<vfs.size();i++) {
			OrdinalPartialValueFunction f = vfs.get(i);
			double[] vals = f.getValues();
			sum += vals[indices[i]] * weights[i];
		}
		return sum;
	}

	private void reinitWeights() {
		weights = new double[getPartialValueFunctions().size()];
		weights[0] = 1.0;
	}
	
	public List<OrdinalPartialValueFunction> getPartialValueFunctions() {
		return vfs;
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public boolean areValidWeights() {
		double sum = 0.0;
		for (int i=0;i<weights.length;i++ ){
			sum += weights[i];
		}
		return Math.abs(sum - 1.0) < WTOL;
	}
	
	/**
	 * 
	 * @param index PRECOND: 0 <= index < getWeights().length
	 * @param weight
	 */
	public void setWeight(int index, double weight) {
		if (index < 0 || index >= weights.length) {
			throw new IllegalArgumentException("invalid value");
		}
		weights[index] = weight;
	}

	@Override
	public String toString() {
		String retStr = "";
		int i=0;
		for (OrdinalPartialValueFunction vf : getPartialValueFunctions()) {
			retStr += vf.toString() + ", w: " + weights[i] + "\n";
			i++;
		}
		return retStr;
	}

	public WeightedOrdinalValueFunction deepCopy() {
		WeightedOrdinalValueFunction f = new WeightedOrdinalValueFunction();
		for (OrdinalPartialValueFunction pf : vfs) {
			f.addValueFunction(pf.deepCopy());
		}
		f.weights = Arrays.copyOf(weights, weights.length);
		return f;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}
}
