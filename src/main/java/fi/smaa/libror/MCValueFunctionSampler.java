package fi.smaa.libror;


public abstract class MCValueFunctionSampler {

	protected StatusListener listener;
	protected int updateInterval = -1;
	protected int misses = 0;
	protected FullValueFunction[] vfs;
	protected RORModel model;
	protected AcceptanceCriterion acceptance;

	public MCValueFunctionSampler(RORModel model, int count) {
		if (count < 1) {
			throw new IllegalArgumentException("PRECOND violated: count < 1");
		}
		vfs = new FullValueFunction[count];
		this.model = model;
		this.acceptance = new AcceptanceCriterion(model);
	}

	public void setStatusListener(StatusListener l, int updateInterval) {
		this.listener = l;
		this.updateInterval = updateInterval;
	}

	public FullValueFunction[] getValueFunctions() {
		return vfs;
	}

	public int getMisses() {
		return misses;
	}
	
	public void sample() throws SamplingException {
		misses = 0;
		doSample();
	}

	protected abstract void doSample() throws SamplingException;

}