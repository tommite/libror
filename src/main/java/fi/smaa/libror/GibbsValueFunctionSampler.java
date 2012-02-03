package fi.smaa.libror;

import java.util.List;

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
			
		// TODO Auto-generated method stub
		int nrPartVF = currentVF.getPartialValueFunctions().size();
		int[] sizPartVF = getPartialVFSizes(currentVF.getPartialValueFunctions());
		
		int curVFind = 0;
		int curPartVFind = 0; // curPartVF == (amount of part VFs) means to sample the weight
		
		int iter = 0;
		int maxIters = getNrValueFunctions() * thinning;
				
		while (iter < maxIters) {
			if (curPartVFind == sizPartVF[curVFind]) { // update weight
				// if it's the last one, don't do anything (it's dependent on others as \sum_i w_i=1
				if (curVFind == (nrPartVF-1)) {
					// don't count this iteration
					iter--;
				} else {
					assert(curVFind < (nrPartVF-1));
					double newW = sampleWeight(curPartVFind, currentVF.getWeights());
					currentVF.setWeight(curPartVFind, newW);
					setLastWeight(currentVF); // set last weight so that they sum to 1.0
				}
			}
			
			// UPDATE INDICES
			if (curPartVFind < sizPartVF[curVFind]) {
				curPartVFind++;
			}
			else { // advance to next partial vf
				curPartVFind = 0;
				curVFind = (curVFind + 1) % nrPartVF;
			}
			iter++;
		}
		
		
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
		double lb = 0.0;
		double ub = 1.0;
		if (curPartVFind > 0) {
			lb = w[curPartVFind-1];
		}
		if (curPartVFind < (w.length-1)) {
			ub = w[curPartVFind+1];
		}
		
		return lb + (rng.nextDouble() * (ub - lb));
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
