/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
package fi.smaa.common;

import java.util.Arrays;

public class ValueRanker {

	/**
	 * Rank values s.t. ranks[0] is the rank of first alternative (0 = best rank)
	 * @param values
	 * @param ranks
	 */
	public static void rankValues(double[] values, int[] ranks) {
		ValueIndexPair[] pairs = new ValueIndexPair[values.length];
		for (int i=0;i<values.length;i++) {
			pairs[i] = new ValueIndexPair(i, values[i]);
		}
		Arrays.sort(pairs);
		int rank = 0;
		Double oldUtility = pairs.length > 0 ? pairs[0].value : 0.0;
		for (int i=0;i<pairs.length;i++) {
			if (!oldUtility.equals(pairs[i].value)) {
				rank++;
				oldUtility = pairs[i].value;
			}			
			ranks[pairs[i].altIndex] = rank;
		}
	}

}
