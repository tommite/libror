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
