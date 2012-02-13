/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-12 Tommi Tervonen.
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