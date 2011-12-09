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
