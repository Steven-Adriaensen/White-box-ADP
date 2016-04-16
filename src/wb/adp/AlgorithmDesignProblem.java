package wb.adp;
import java.util.List;

import wb.agent.Agent;

/**
 * 
 * This interface specifies an Algorithm Design Problem.
 * 
 * It specifies:
 * - a list of open design choices in this adp
 * - a function evaluating an agent's capability to make these decisions in this adp 
 * 
 * @author Steven Adriaensen
 *
 */
public interface AlgorithmDesignProblem {
	/**
	 * Returns the list of open design choices in this adp.
	 * In the current format these need to be specified statically 
	 * (and can't change after construction)
	 * 
	 * @return list of design choices in a fixed invariant order
	 */
	List<DesignChoice<?>> getDesignChoices();
	
	/**
	 * Evaluates a given agent on this adp.
	 * 
	 * @param agent the agent to be evaluated
	 * @return a list of a list of transitions (white box info) 
	 * indicating in order 
	 * the choice points encountered, 
	 * decisions made by the agent and
	 * rewards received for making that decision.
	 */
	List<Transition> evaluate(Agent agent);
	
	/**
	 * Record holding information about a single transition.
	 * 
	 * @author Steven Adriaensen
	 *
	 */
	public class Transition{
		public DesignChoice<?> dc; //design choice faced
		public int dd; //decision made by the agent
		public double reward; //the (direct) reward it received for that decision (before next dc) 
		
		Transition(DesignChoice<?> dc, int dd){
			this.dc = dc;
			this.dd = dd;
		}
	}
	
	public static double computeF(List<Transition> wb_info){
		double f = 0;
		for(Transition t : wb_info){
			f += t.reward;
		}
		return f;
	}
	
}
