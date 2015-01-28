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

import java.util.Arrays;

public class PartialValueFunction implements DeepCopiable<PartialValueFunction> {

	private double[] values;

	/**
	 * 
	 * @param nrLevels >= 1
	 */
	public PartialValueFunction(int nrLevels) {
		if (nrLevels < 1) {
			throw new IllegalArgumentException("PRECOND violation");
		}
		values = new double[nrLevels];
		values[values.length-1] = 1.0; 
	}
	
	public double[] getValues() {
		return values;
	}
	
	/**
	 * 
	 * @param index PRECOND: 1 <= index < getValues().length-1
	 * @param value
	 */
	public void setValue(int index, double value) {
		if (index < 1 || index >= (values.length-1)) {
			throw new IllegalArgumentException("invalid value");
		}
		values[index] = value;
	}

	public PartialValueFunction deepCopy() {
		PartialValueFunction n = new PartialValueFunction(values.length);
		for (int i=0;i<values.length;i++) {
			n.values[i] = values[i];
		}
		return n;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PartialValueFunction) {
			PartialValueFunction p2 = (PartialValueFunction) other;
			return Arrays.equals(values, p2.values);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
}
