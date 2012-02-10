package fi.smaa.libror;

import java.util.Arrays;

public class PartialValueFunction implements DeepCopiable<PartialValueFunction> {

	private double[] values;

	/**
	 * 
	 * @param nrLevels >= 1
	 */
	public PartialValueFunction(int nrLevels) {
		if (nrLevels < 1) {
			throw new IllegalArgumentException("PRECOND violation");
		}
		values = new double[nrLevels];
		values[values.length-1] = 1.0; 
	}
	
	public double[] getValues() {
		return values;
	}
	
	/**
	 * 
	 * @param index PRECOND: 1 <= index < getValues().length-1
	 * @param value
	 */
	public void setValue(int index, double value) {
		if (index < 1 || index >= (values.length-1)) {
			throw new IllegalArgumentException("invalid value");
		}
		values[index] = value;
	}

	public PartialValueFunction deepCopy() {
		PartialValueFunction n = new PartialValueFunction(values.length);
		for (int i=0;i<values.length;i++) {
			n.values[i] = values[i];
		}
		return n;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PartialValueFunction) {
			PartialValueFunction p2 = (PartialValueFunction) other;
			return Arrays.equals(values, p2.values);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
}
