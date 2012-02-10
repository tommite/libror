package fi.smaa.libror.r;

import fi.smaa.libror.GibbsValueFunctionSampler;
import fi.smaa.libror.InvalidStartingPointException;
import fi.smaa.libror.MCValueFunctionSampler;
import fi.smaa.libror.PartialValueFunction;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.SamplingException;
import fi.smaa.libror.StatusListener;
import fi.smaa.libror.FullValueFunction;

public class GibbsValueFunctionSamplerRFacade extends RORRFacade {

	private MCValueFunctionSampler sampler;

	public GibbsValueFunctionSamplerRFacade(double[] matrix, int nRows, int nrFuncs, int thinning) throws InvalidStartingPointException {
		super(new RORModel(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		sampler = new GibbsValueFunctionSampler(this.model, nrFuncs, thinning);
	}
	
	public void sample(int updInterval) throws SamplingException {
		sampler.setStatusListener(new FacadeStatusListener(), updInterval);
		sampler.sample();
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
