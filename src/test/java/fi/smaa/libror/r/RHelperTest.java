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

package fi.smaa.libror.r;

import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RHelperTest {

	@Test
	public void testRArrayMatrixToRealMatrix() {
		double[] data = new double[]{1.0, 4.0, 2.0, 5.0, 3.0, 6.0};
		RealMatrix mat = RHelper.rArrayMatrixToRealMatrix(data, 2);
		assertArrayEquals(new double[] {1.0, 2.0, 3.0}, mat.getRow(0), 0.0001);
		assertArrayEquals(new double[] {4.0, 5.0, 6.0}, mat.getRow(1), 0.0001);
	}
}
