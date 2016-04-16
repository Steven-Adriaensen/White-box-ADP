package wb.adp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wb.agent.Agent;

/**
 * 
 * This is an abstract implementation of the context-oblivious ADP.
 * 
 * It does most of the book-keeping associated with evaluating an agent,
 * enabling higher level algorithm design problems implementations.
 * 
 * In particular it provides the following functions:
 * - T getDecision(dc<T>) to get a the decision for a given design choice when it is needed.
 * - feedback(double) to indicate the desirability of a given execution path.
 * 
 * In most COADP cases you should extend this class, 
 * rather than implementing the AlgorithmDesignProblem interface directly.
 * 
 * @author Steven Adriaensen
 *
 */
public abstract class BaseCOADP implements AlgorithmDesignProblem {
	private Map<DesignChoice<?>,Integer> decisions = new HashMap<DesignChoice<?>,Integer>();
	private LinkedList<Transition> transitions;
	private Agent agent;

	@Override
	final public List<Transition> evaluate(Agent agent) {
		decisions.clear();
		transitions = new LinkedList<Transition>();
		transitions.add(new Transition(null,-1));
		this.agent = agent;
		execute();
		return transitions;
	}
	
	/**
	 * Returns the decision for a given design choice.
	 * If the design choice is still open it will query the agent for a decision.
	 * Otherwise it will simply return the decision.
	 * 
	 * @param dc the design choice for which we need a decision
	 * @return the decision the agent made for that design choice
	 */
	final protected <T> T getDecision(DesignChoice<T> dc){
		int alt;
		if(decisions.containsKey(dc)){
			//we made design decision before
			alt = decisions.get(dc);
		}else{
			//design decision is still open
			//query the agent
			alt = agent.make(decisions, dc);
			decisions.put(dc, alt);
			transitions.add(new Transition(dc,alt));
		}
		return dc.getAlternative(alt);
	}
	
	/**
	 * 
	 * rewards/penalises the agent.
	 * Solvers will attempt to train an agent to maximise the reward it accumulates
	 * and as such it specifies the objective.
	 * 
	 * @param reward the reward
	 */
	final protected void feedback(double reward){
		transitions.peekLast().reward += reward;
	}
	
	/**
	 * This method executes the algorithm with open design choices.
	 * 
	 * During execution it can use the getDecision and feedback methods.
	 * 
	 */
	abstract public void execute();
	
	

}
