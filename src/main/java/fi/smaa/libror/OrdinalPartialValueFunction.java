package fi.smaa.libror;

public class OrdinalPartialValueFunction {

	private double[] values;

	/**
	 * 
	 * @param nrLevels >= 2
	 */
	public OrdinalPartialValueFunction(int nrLevels) {
		if (nrLevels < 2) {
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
}
