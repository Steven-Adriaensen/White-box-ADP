package wb.solvers;

import wb.adp.AlgorithmDesignProblem;
import wb.agent.Agent;

public interface Solver {
	Agent solve(AlgorithmDesignProblem adp);
}
