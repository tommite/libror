package fi.smaa.libror;

public interface ValueFunctionSampler {
	public int getMisses();
	public void sample() throws SamplingException;
	public WeightedOrdinalValueFunction[] getValueFunctions();
}
