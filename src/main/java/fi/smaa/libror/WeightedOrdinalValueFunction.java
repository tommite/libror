package fi.smaa.libror;

import java.util.ArrayList;
import java.util.List;

public class WeightedOrdinalValueFunction {
	public static final double WTOL = 0.01;
	
	private double[] weights = new double[0];
	private List<OrdinalPartialValueFunction> vfs = new ArrayList<OrdinalPartialValueFunction>();

	public void addValueFunction(OrdinalPartialValueFunction v) {
		vfs.add(v);
		reinitWeights();
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

	public String toString() {
		String retStr = "";
		int i=0;
		for (OrdinalPartialValueFunction vf : getPartialValueFunctions()) {
			retStr += vf.toString() + ", w: " + weights[i] + "\n";
			i++;
		}
		return retStr;
	}
}
