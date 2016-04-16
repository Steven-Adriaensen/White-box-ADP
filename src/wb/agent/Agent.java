package wb.agent;
import java.util.Map;

import wb.adp.DesignChoice;
import wb.util.Experience;

/**
 * This interface represents an agent (in a COADP setting).
 * 
 * @author Steven Adriaensen
 *
 */
public interface Agent {
	/**
	 * Queries the agent to make a decision for a given design choice,
	 * given previously made decisions.
	 * 
	 * @param decisions decisions made thus far
	 * @param dc the design choice faced
	 * @return the alternative chosen by the agent
	 */
	int make(Map<DesignChoice<?>,Integer> decisions, DesignChoice<?> dc);
	
	/**
	 * Learn from experience.
	 * 
	 * After learn is called, the agent will base its actions on this experience.
	 * 
	 * Note that this operation overrides previous calls to learn.
	 * (learn from scratch, each time forgetting what it learned in the past.
	 * 
	 * @param exp the experience used for learning
	 */
	default void learn(Experience exp){};
}
