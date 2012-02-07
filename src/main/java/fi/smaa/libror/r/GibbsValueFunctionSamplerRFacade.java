package fi.smaa.libror.r;

import fi.smaa.libror.GibbsValueFunctionSampler;
import fi.smaa.libror.InvalidStartingPointException;
import fi.smaa.libror.OrdinalPartialValueFunction;
import fi.smaa.libror.PerformanceMatrix;
import fi.smaa.libror.RORModel;
import fi.smaa.libror.SamplingException;
import fi.smaa.libror.WeightedOrdinalValueFunction;

public class GibbsValueFunctionSamplerRFacade extends RORRFacade {

	private GibbsValueFunctionSampler sampler;

	public GibbsValueFunctionSamplerRFacade(double[] matrix, int nRows, int nrFuncs, int thinning) throws InvalidStartingPointException {
		super(new RORModel(new PerformanceMatrix(RHelper.rArrayMatrixToRealMatrix(matrix, nRows))));
		sampler = new GibbsValueFunctionSampler(this.model, nrFuncs, thinning);				
	}
	
	public void sample() throws SamplingException {
		sampler.sample();
	}
	
	public double[][] getValueFunctionsForCriterion(int cIndex) {
		if (cIndex < 0 || cIndex >= model.getNrCriteria()) {
			throw new IllegalArgumentException("Invalid criterion index");
		}
		
		WeightedOrdinalValueFunction[] vfs = sampler.getValueFunctions();
		int nrValues = vfs[0].getPartialValueFunctions().get(cIndex).getValues().length;
		double[][] ret = new double[vfs.length][nrValues];
		
		for (int i=0;i<vfs.length;i++) {
			WeightedOrdinalValueFunction vf = vfs[i];
			OrdinalPartialValueFunction pvf = vf.getPartialValueFunctions().get(cIndex);
			double w = vf.getWeights()[cIndex];
			double[] vals = pvf.getValues();
			for (int j=0;j<vals.length;j++) {
				ret[i][j] = vals[j] * w;
			}
		}
		return ret;
	}

}
