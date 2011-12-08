package fi.smaa.libror.r;

import fi.smaa.libror.RORModel;

public class RORRFacade<T extends RORModel> {

	protected T model;

	protected RORRFacade(T model) {
		this.model = model;
	}

	public void addPreference(int a, int b) {
		model.addPreference(a, b);
	}

}