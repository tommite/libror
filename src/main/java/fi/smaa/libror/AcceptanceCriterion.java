package fi.smaa.libror;

import fi.smaa.libror.RORModel.PrefPair;

public class AcceptanceCriterion {

	private RORModel model;

	public AcceptanceCriterion(RORModel model) {
		this.model = model;
	}
	
	/**
	 * Checks whether the given value function passes the acceptance criterion.
	 * 
	 * @param vf the value function to assess
	 * @return true, if vf passess the criterion, false otherwise
	 */
	public boolean check(FullValueFunction vf) {
		PerformanceMatrix pm = model.getPerfMatrix();		
		for (PrefPair pref : model.getPrefPairs()) {
			int[] alevels = pm.getLevelIndices(pref.a);
			int[] blevels = pm.getLevelIndices(pref.b);
			if (vf.evaluate(alevels) < vf.evaluate(blevels)) {
				return false;
			}
		}
		return true;
	}
}
