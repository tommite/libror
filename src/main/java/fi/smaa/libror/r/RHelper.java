/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror.php.
 * Copyright (C) 2011 Tommi Tervonen.
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

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

public class RHelper {

	public static RealMatrix rArrayMatrixToRealMatrix(double[] matrix, int nRows) {
		assert(nRows > 0);
		int nCols = matrix.length / nRows;
		assert(matrix.length == nRows * nCols);
		RealMatrix perfMatrix = new Array2DRowRealMatrix(nRows, nCols);
		for (int i=0;i<nRows;i++) {
			for (int j=0;j<nCols;j++) {
				perfMatrix.setEntry(i, j, matrix[i*nCols + j]);
			}
		}
		return perfMatrix;
	}

}
