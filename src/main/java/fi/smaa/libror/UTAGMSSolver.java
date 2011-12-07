package fi.smaa.libror;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;


@SuppressWarnings("deprecation")
public class UTAGMSSolver extends RORModel {

	private RealMatrix necessaryRelation = null;
	private RealMatrix possibleRelation = null;
	private SimplexSolver solver = new SimplexSolver();

	public UTAGMSSolver(RealMatrix perfMatrix) {
		super(perfMatrix);
	}

	public void solve() {
		necessaryRelation = new Array2DRowRealMatrix(getNrAlternatives(), getNrCriteria());
		possibleRelation = new Array2DRowRealMatrix(getNrAlternatives(), getNrCriteria());
		List<LinearConstraint> baseConstraints = buildRORConstraints();
		for (int i=0;i<getNrAlternatives();i++) {
			for (int j=0;j<getNrAlternatives();j++) {
				necessaryRelation.setEntry(i, j,
						solveRelation(i, j, baseConstraints, true)? 1.0 : 0.0);
				possibleRelation.setEntry(i, j,
						solveRelation(i, j, baseConstraints, false)? 1.0 : 0.0);				
			}
		}
	}
	
	public void printModel(boolean necessary, int a, int b) {
		List<LinearConstraint> constraints = buildRORConstraints();
		addNecOrPrefConstraint(a, b, necessary, constraints);
		LinearConstraintHelper.printConstraints(constraints);
	}

	List<LinearConstraint> buildRORConstraints() {
		List<LinearConstraint> c = new ArrayList<LinearConstraint>();
		for (PrefPair p : prefPairs) {
			c.add(buildPreferredConstraint(p.a, p.b));
		}
		for (int i=0;i<getNrCriteria();i++) {
			c.addAll(buildMonotonousConstraints(i));
		}
		for (int i=0;i<getNrCriteria();i++) {
			c.add(buildFirstLevelZeroConstraint(i));
		}
		for (int i=0;i<getNrCriteria();i++) {
			c.add(buildLevelsAddToUnityConstraint(i));
		}
		
		return c;
	}

	private LinearConstraint buildLevelsAddToUnityConstraint(int critIndex) {
		RealVector levels = getLevels()[critIndex];
		int offset = getConstraintOffset(critIndex);
		double[] vars = new double[getNrLPVariables()];		
		for (int i=0;i<levels.getDimension();i++) {
			vars[offset + i] = 1.0;
		}
		return new LinearConstraint(vars, Relationship.EQ, 1.0);		
	}

	private LinearConstraint buildFirstLevelZeroConstraint(int critIndex) {
		double[] lhsVars = new double[getNrLPVariables()];		
		lhsVars[getConstraintOffset(critIndex)] = 1.0;
		return new LinearConstraint(lhsVars, Relationship.EQ, 0.0);
	}

	private List<LinearConstraint> buildMonotonousConstraints(int critIndex) {
		List<LinearConstraint> constList = new ArrayList<LinearConstraint>();
		RealVector levels = getLevels()[critIndex];
		for (int i=0;i<levels.getDimension()-1;i++) {
			double[] lhs = new double[getNrLPVariables()];
			double[] rhs = new double[getNrLPVariables()];
			lhs[getConstraintOffset(critIndex)+i] = 1.0;
			rhs[getConstraintOffset(critIndex)+i+1] = 1.0;
			constList.add(new LinearConstraint(lhs, 0.0, Relationship.LEQ, rhs, 0.0));
		}
		return constList;
	}

	private LinearConstraint buildPreferredConstraint(int a, int b) {
		double[] lhsVars = new double[getNrLPVariables()];
		double[] rhsVars = new double[getNrLPVariables()];
		setVarsPositive(lhsVars, a);
		setVarsPositive(rhsVars, b);
		// set epsilon
		rhsVars[rhsVars.length-1] = 1.0;
		return new LinearConstraint(lhsVars, 0.0, Relationship.GEQ, rhsVars, 0.0);
	}

	private void setVarsPositive(double[] vars, int altIndex) {
		for (int i=0;i<getNrCriteria();i++) {
			vars[getConstraintIndex(i, altIndex)] = 1.0;
		}
	}

	private int getNrLPVariables() {
		// utility of all alts + epsilon
		int sum = 0;
		for(RealVector vec : getLevels()) {
			sum += vec.getDimension();
		}
		return sum + 1;
	}
	
	private int getConstraintIndex(int critIndex, int altIndex) {
		assert(critIndex >= 0 && altIndex >= 0);
		
		int offset = getConstraintOffset(critIndex);
		int index = Arrays.binarySearch(getLevels()[critIndex].getData(), perfMatrix.getEntry(altIndex, critIndex));
		assert(index >= 0); // sanity check
		
		return offset + index;
	}

	private int getConstraintOffset(int critIndex) {
		int offset = 0;
		for (int i=0;i<critIndex;i++) {
			offset += getLevels()[i].getDimension();
		}
		return offset;
	}

	/**
	 * Check whether necessary relation holds.
	 * 
	 * @param i index of first alternative
	 * @param j index of the second alternative
	 * @param rorConstraints base constraints E_{ROR}^{A^R}
	 * @param necessary TODO
	 * @return
	 */
	private boolean solveRelation(int i, int j, List<LinearConstraint> rorConstraints, boolean necessary) {
		assert (i >= 0 && j >= 0);
		
		if (i ==j) {
			return true;
		}
		
		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>(rorConstraints);
		addNecOrPrefConstraint(i, j, necessary, constraints);
		double[] coeff = new double[getNrLPVariables()];
		coeff[coeff.length-1] = 1.0;
		LinearObjectiveFunction goalFunction = new LinearObjectiveFunction(coeff, 0.0);
		
		boolean result = false;
		try {
			RealPointValuePair res = solver.optimize(goalFunction, constraints, GoalType.MAXIMIZE, true);
			result = res.getValue() >= 0.0;
		} catch (OptimizationException e) {
			result = false;
		}
		return necessary ? result : !result;
	}

	private void addNecOrPrefConstraint(int i, int j, boolean necessary,
			List<LinearConstraint> constraints) {
		if (necessary) {
			constraints.add(buildPreferredConstraint(i, j));
		} else { // possible
			constraints.add(buildPossiblePreferenceConstraint(i, j));
		}
	}

	private LinearConstraint buildPossiblePreferenceConstraint(int a, int b) {
		double[] lhsVars = new double[getNrLPVariables()];
		double[] rhsVars = new double[getNrLPVariables()];
		setVarsPositive(lhsVars, a);
		setVarsPositive(rhsVars, b);
		// set epsilon
		return new LinearConstraint(lhsVars, 0.0, Relationship.GEQ, rhsVars, 0.0);		
	}

	/**
	 * PRECOND: solve() executed and returned true
	 * @return a matrix where >0.0 (1.0) signifies that the relation holds
	 * @throws IllegalStateException if solve() hasn't been succesfully executed 
	 */
	public RealMatrix getNecessaryRelation() throws IllegalStateException {
		if (necessaryRelation == null) {
			throw new IllegalStateException("violating PRECOND");
		}
		return necessaryRelation;
	}

	/**
	 * PRECOND: solve() executed and returned true
	 * @return a matrix where >0.0 (1.0) signifies that the relation holds
	 * @throws IllegalStateException if solve() hasn't been succesfully executed 
	 */
	public RealMatrix getPossibleRelation() {
		if (possibleRelation == null) {
			throw new IllegalStateException("violating PRECOND");
		}
		return possibleRelation;
	}
}
