package fi.smaa.libror.r;

import fi.smaa.libror.FullValueFunction;
import fi.smaa.libror.GibbsValueFunctionSampler;
import fi.smaa.libror.MCValueFunctionSampler;
import fi.smaa.libror.PartialValueFunction;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.RejectionValueFunctionSampler;
import fi.smaa.libror.SamplingException;
import fi.smaa.libror.StatusListener;

public class ValueFunctionSamplerRFacade extends RORRFacade {

	private MCValueFunctionSampler sampler;
	private int nrFuncs;
	private int thinning;
	private int samplerId;

	public ValueFunctionSamplerRFacade(double[] matrix, int nRows, int nrFuncs, int thinning, int samplerId) throws SamplingException {
		super(new RORModel(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		if (samplerId != 1 && samplerId != 2) {
			throw new IllegalArgumentException("Invalid sampler id " + samplerId);
		}
		this.nrFuncs = nrFuncs;
		this.thinning = thinning;
		this.samplerId = samplerId;
	}
	
	public void sample(int updInterval) throws SamplingException {
		if (sampler == null) {
			if (samplerId == 1) {
				sampler = new GibbsValueFunctionSampler(this.model, nrFuncs, thinning);
			} else { // it's 2
				sampler = new RejectionValueFunctionSampler(this.model, nrFuncs);
			}
		}
		sampler.setStatusListener(new FacadeStatusListener(), updInterval);
		sampler.sample();
	}
	
	public int getMisses() {
		if (sampler == null) {
			throw new IllegalStateException("sample() not called");
		}
		return sampler.getMisses();
	}
	
	public double[][] getValueFunctionsForCriterion(int cIndex) {
		if (cIndex < 0 || cIndex >= model.getNrCriteria()) {
			throw new IllegalArgumentException("Invalid criterion index");
		}
		
		FullValueFunction[] vfs = sampler.getValueFunctions();
		int nrValues = vfs[0].getPartialValueFunctions().get(cIndex).getValues().length;
		double[][] ret = new double[vfs.length][nrValues];
		
		for (int i=0;i<vfs.length;i++) {
			FullValueFunction vf = vfs[i];
			PartialValueFunction pvf = vf.getPartialValueFunctions().get(cIndex);
			double w = vf.getWeights()[cIndex];
			double[] vals = pvf.getValues();
			for (int j=0;j<vals.length;j++) {
				ret[i][j] = vals[j] * w;
			}
		}
		
		return ret;
	}

	private class FacadeStatusListener implements StatusListener {
		public void update(int nrItersDone) {
			System.out.println("Sampled " + nrItersDone + " / " + sampler.getValueFunctions().length);
		}
	}
}
