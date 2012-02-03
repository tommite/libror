package fi.smaa.libror;

import org.apache.commons.math.random.MersenneTwister;

public abstract class ValueFunctionSampler {

	protected int misses;
	protected double[] w;
	protected MersenneTwister rng = new MersenneTwister(0x667);
	protected RORModel model;

	public ValueFunctionSampler(RORModel model) {
		this.model = model;
		misses = 0;
		w = new double[model.getNrCriteria()];
	}

	public int getMisses() {
		return misses;
	}
	
	public void sample() {
		misses = 0;
		doSample();
	}

	protected abstract void doSample();

	protected void sampleWeights() {
		RandomUtil.createSumToOneRand(w);
	}

}