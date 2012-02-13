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

import java.util.List;

import org.apache.commons.math.optimization.linear.LinearConstraint;

public class LinearConstraintHelper {
	
	public static String constraintToString(LinearConstraint c) {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<c.getCoefficients().getData().length;i++) {
			if (i > 0) {
				sb.append(" + ");
			}
			sb.append(c.getCoefficients().getData()[i]);			
			sb.append("*x");
			sb.append(i);
		}
		sb.append(" ");
		sb.append(c.getRelationship().toString());
		sb.append(" ");
		sb.append(c.getValue());
		return sb.toString();
	}
	
	public static void printConstraint(LinearConstraint c) {
		System.out.println(constraintToString(c));
	}
	
	public static void printConstraints(List<LinearConstraint> consts) {
		int index = 1;
		for (LinearConstraint c : consts) {
			System.out.print("[C"+index+"] ");
			printConstraint(c);
			index++;
		}
	}
}
