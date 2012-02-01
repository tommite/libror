package fi.smaa.libror;

import org.apache.commons.math.random.MersenneTwister;

public abstract class ValueFunctionSampler {

	protected FullValueFunction[] vfs;
	protected MersenneTwister rng = new MersenneTwister(0x667);
	protected RORModel model;

	public ValueFunctionSampler(RORModel model, int count) {
		this.model = model;
		vfs = new FullValueFunction[count];			
	}

	/**
	 * Samples partial value functions.
	 * 
	 */
	public abstract void sample();

	public FullValueFunction[] getValueFunctions() {
		if (vfs == null) {
			throw new IllegalStateException("sample() not called");
		}
		return vfs;
	}

}