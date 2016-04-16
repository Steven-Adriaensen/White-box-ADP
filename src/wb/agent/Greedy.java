package wb.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;
import wb.util.Experience;
import wb.util.StateSpace;
import wb.util.StateSpace.State;
import wb.util.StateSpace.StateAction;

/**
 * This agent will take the action it thinks is best (given its experience).
 * Concrete, the action maximizing the expected future reward.
 * 
 * Ties are broken randomly. If it has no information about taking an action in a certain state, 
 * it will assume it is worse than any other. If it ends up in a state it has no experience in, 
 * it will select an action uniformly at random.
 * 
 * This agent type is returned by the white box optimizer.
 * Note that a different agent can be used to gather experience in the training phase.
 * (e.g. URS, PURS)
 * 
 * @author Steven Adriaensen
 *
 */
public class Greedy implements Agent{
	final private Random rng;
	
	private StateSpace ss;
	private Map<StateAction,Double> q;

	public Greedy(Random rng){
		this.rng = rng;
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		if(q != null){
			//determine the current state
			State s = ss.new State(decisions,dc);
			//determine a list of best actions
			StateAction sa = ss.new StateAction(s,-1);
			List<Integer> best = new ArrayList<Integer>();
			double bestq = Double.NEGATIVE_INFINITY;
			for(sa.dd = 0; sa.dd < dc.getNumberOfAlternatives(); sa.dd++){
				Double qsa = q.get(sa);
				if(qsa != null){
					if(qsa >= bestq){
						if(qsa > bestq){
							best.clear();
							bestq = qsa;
						}
						best.add(sa.dd);
					}
				}
			}
			//randomly choose a best action
			int n = best.size();
			return n == 0? rng.nextInt(dc.getNumberOfAlternatives()) : best.get(rng.nextInt(n));
		}else{
			return rng.nextInt(dc.getNumberOfAlternatives());
		}
	}
	
	@Override
	public void learn(Experience exp){
		//derive the q-value
		q = exp.compute_q();
		ss = exp.getStateSpace();
	}
	
	

}
