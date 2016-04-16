package wb.agent;

import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;
import wb.util.Experience;

/**
 * 
 * Implements the epsilon greedy agent.
 * 
 * It alternates between choosing the action it thinks is best (given its experience)
 * and choosing an action uniformly at random (with frequency 1-epsilon and epsilon resp.)
 * 
 * It therefore balances exploration and exploitation.
 * 
 * @author Steven Adriaensen
 *
 */
public class EpsilonGreedy implements Agent{
	final private Random rng;
	final private Greedy greedy;
	final double epsilon;

	/**
	 * Constructs an epsilon greedy agent
	 * 
	 * @param epsilon the likelihood of selecting a random action (0 never, 1 always)
	 * @param rng the random generator used
	 */
	public EpsilonGreedy(double epsilon, Random rng){
		this.rng = rng;
		this.epsilon = epsilon;
		greedy = new Greedy(rng);
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		if(rng.nextDouble() < epsilon){
			return rng.nextInt(dc.getNumberOfAlternatives());
		}else{
			return greedy.make(decisions, dc);
		}
	}
	
	@Override
	public void learn(Experience exp){
		greedy.learn(exp);
	}
	
	

}
