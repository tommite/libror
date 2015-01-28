/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-15 Tommi Tervonen.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
