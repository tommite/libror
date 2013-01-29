/*
 * This file is part of libror.
 * libror is distributed from http://smaa.fi/libror
 * Copyright (C) 2011-13 Tommi Tervonen.
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

import org.apache.commons.math.random.MersenneTwister;

public class RandomUtil {
	
	private static MersenneTwister random = new MersenneTwister(0x667);
		
	/**
	 * Generates a sample from uniform distribution in interval [0.0, 1.0].
	 * 
	 * @return the sampled number
	 */
	public static double createUnif01() {
		return random.nextDouble();
	}

	/**
	 * Creates an array of random numbers that sum to a given amount.
	 * 
	 * @param dest the array to create the numbers to.
	 * @param sumTo The amount the numbers must sum to. Must be >= 0.0
	 * @throws NullPointerException if dest == null
	 */
	public static void createSumToRand(double[] dest, double sumTo) throws NullPointerException {
		if (sumTo < 0.0) {
			throw new IllegalArgumentException("sumTo negative");
		}
		if (dest == null) {
			throw new NullPointerException("destination array null");
		}

		int len = dest.length;
		for (int i=0;i<len-1;i++) {
			dest[i] = createUnif01() * sumTo;
		}

		dest[len-1] = sumTo;

		Arrays.sort(dest);

		double last = 0.0;
		for (int i=0;i<len;i++) {
			double t = dest[i];
			dest[i] = t - last;
			last = t;
		}
	}

	/**
	 * Creates random numbers that sum to 1.0.
	 * 
	 * @param dest the destination array to create the random numbers to
	 * @throws NullPointerException if dest == null
	 */
	public static void createSumToOneRand(double[] dest) throws NullPointerException {
		createSumToRand(dest, 1.0);
	}

	/**
	 * Creates random numbers that sum to 1.0 and are sorted in ascending order.
	 * 
	 * @param dest the destination array to create the random numbers to
	 * @throws NullPointerException if dest == null
	 */
	public static void createSumToOneSorted(double[] dest) throws NullPointerException {
		createSumToOneRand(dest);
		Arrays.sort(dest);
	}
	
	public static void createSorted(double[] dest) {
		for (int i=0;i<dest.length;i++) {
			dest[i] = createUnif01();
		}
		Arrays.sort(dest);
	}
}
