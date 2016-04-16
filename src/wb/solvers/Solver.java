package wb.solvers;

import wb.adp.AlgorithmDesignProblem;
import wb.agent.Agent;

/**
 * This interface represents a solver for the algorithm design problem.
 * 
 * @author Steven Adriaensen
 *
 */
public interface Solver {
	/**
	 * @param adp the algorithm design problem to be solved
	 * @return an agent trained to solve the algorithm design problem.
	 */
	Agent solve(AlgorithmDesignProblem adp);
}
