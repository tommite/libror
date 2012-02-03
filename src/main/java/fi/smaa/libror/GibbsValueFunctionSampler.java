package fi.smaa.libror;

import java.util.Arrays;
import java.util.List;

import fi.smaa.libror.RORModel.PrefPair;

public class GibbsValueFunctionSampler extends ValueFunctionSampler {
	
	private WeightedOrdinalValueFunction[] vfs;
	private int thinning;
	private WeightedOrdinalValueFunction startingPoint;
	
	public GibbsValueFunctionSampler(RORModel model, int count, int thinning, WeightedOrdinalValueFunction startingPoint) {
		super(model);
		if (startingPoint != null && !startingPoint.areValidWeights()) {
			throw new IllegalArgumentException("PRECOND: starting point has invalid weights (not summing to 1.0)");
		}
		this.startingPoint = startingPoint;
		vfs = new WeightedOrdinalValueFunction[count];
		this.thinning = thinning;
	}

	public GibbsValueFunctionSampler(RORModel model, int count, int thinning) {
		this(model, count, thinning, null);
	}
	
	public WeightedOrdinalValueFunction[] getValueFunctions() {
		return vfs;
	}
	
	public int getThinning() {
		return thinning;
	}
	
	public int getNrValueFunctions() {
		return vfs.length;
	}

	@Override
	public void doSample() {
		WeightedOrdinalValueFunction currentVF = startingPoint;
		if (currentVF == null) {
			generateStartingPoint();
		}
			
		int nrPartVF = currentVF.getPartialValueFunctions().size();
		int[] sizPartVF = getPartialVFSizes(currentVF.getPartialValueFunctions());
		
		int curVFind = 0;
		int curPartVFind = 1; // curPartVF == (amount of part VFs) means to sample the weight
		// and 0 is always 0.0 value so we don't sample it 
		
		int iter = 0;
		
		boolean store = true;
		int index = 1;
				
		vfs[0] = currentVF.deepCopy();
		
		while (index < vfs.length) {
			store = false;
			if (curPartVFind == sizPartVF[curVFind]-1) { // update weight
				// if it's the last one, don't do anything (it's dependent on others as \sum_i w_i=1
				if (curVFind != (nrPartVF-1)) {
					assert(curVFind < (nrPartVF-1));
					double[] weights = currentVF.getWeights();
					double newW = sampleWeight(curVFind, weights);
					double[] oldWeights = Arrays.copyOf(weights, weights.length);
					currentVF.setWeight(curVFind, newW);
					setLastWeight(currentVF); // set last weight so that they sum to 1.0
					if (failsRejectCriterion(currentVF)) {
						currentVF.setWeights(oldWeights);
					}
					store = true;
				}
			} else { // update characteristic point
				OrdinalPartialValueFunction vf = currentVF.getPartialValueFunctions().get(curVFind);
				double point = samplePoint(curPartVFind, vf);
				vf.setValue(curPartVFind, point);
				store = true;
			}
			
			// UPDATE INDICES
			if (curPartVFind < sizPartVF[curVFind]-1) {
				curPartVFind++;
			}
			else { // advance to next partial vf
				curPartVFind = 1;
				curVFind = (curVFind + 1) % nrPartVF;
			}
			if (store) {
				iter++;
				if (iter % thinning == 0) {
					vfs[index] = currentVF.deepCopy();
					index++;
				}
			}
		}
		
		
	}

	private boolean failsRejectCriterion(WeightedOrdinalValueFunction vf) {
		PerformanceMatrix pm = model.getPerfMatrix();		
		for (PrefPair pref : model.getPrefPairs()) {
			int[] alevels = pm.getLevelIndices(pref.a);
			int[] blevels = pm.getLevelIndices(pref.b);
			if (vf.evaluate(alevels) < vf.evaluate(blevels)) {
				return true;
			}
		}
		return false;
	}

	private double samplePoint(int ind, OrdinalPartialValueFunction vf) {
		double[] vals = vf.getValues();
		double lb = vals[ind-1];
		double ub = vals[ind+1];
		
		return lb + (rng.nextDouble() * (ub - lb));
	}

	private void setLastWeight(WeightedOrdinalValueFunction vf) {
		double[] w = vf.getWeights();
		double sum = 0.0;
		for (int i=0;i<w.length-1;i++) {
			sum += w[i];
		}
		vf.setWeight(w.length-1, 1.0 - sum);
	}

	private double sampleWeight(int curPartVFind, double[] w) {
		double sum = 0.0;
		for (int i=0;i<w.length-1;i++) {
			if (i != curPartVFind) {
				sum += w[i];
			}
		}		
		return rng.nextDouble() * (1.0 - sum);
	}

	private int[] getPartialVFSizes(List<OrdinalPartialValueFunction> levels) {
		int[] sizes = new int[levels.size()];
		for (int i=0;i<levels.size();i++) {
			sizes[i] = levels.get(i).getValues().length;
		}
		return sizes;
	}

	private WeightedOrdinalValueFunction generateStartingPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	public WeightedOrdinalValueFunction getStartingPoint() {
		return startingPoint;
	}

}