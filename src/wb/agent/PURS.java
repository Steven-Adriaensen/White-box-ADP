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
 * Implements an agent that selects action proportionally to the expected number of
 * different future execution paths (based on its current experience)
 * 
 * This is useful in settings where multiple configurations correspond to the same execution path.
 * 
 * @author Steven Adriaensen
 *
 */
public class PURS implements Agent{
	final private Random rng;
	
	private StateSpace ss;
	private Map<StateAction,Integer> n;

	/**
	 * Constructs a PURS agent
	 * 
	 * @param rng the random generator used
	 */
	public PURS(Random rng){
		this.rng = rng;
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		if(n != null){
			//determine the current state
			State s = ss.new State(decisions,dc);
			//determine n for each action
			int[] na = new int[dc.getNumberOfAlternatives()];
			StateAction sa = ss.new StateAction(s,-1);
			List<Integer> unexplored = null;
			int ns = 0;
			for(sa.dd = 0; sa.dd < dc.getNumberOfAlternatives(); sa.dd++){
				Integer n_val = n.get(sa);
				if(n_val == null){
					if(unexplored == null){
						unexplored = new ArrayList<>(dc.getNumberOfAlternatives()-sa.dd);
					}
					unexplored.add(sa.dd);
				}else if(unexplored == null){
					na[sa.dd] = n_val;
					ns += n_val;
				}
			}
			if(unexplored == null){
				//all alternatives were tried, select proportional to n
				double pivot = rng.nextDouble()*ns;
				int acc = 0;
				for(int i = 0; i < na.length; i++){
					acc += na[i];
					if(acc > pivot){
						return i;
					}
				}
				return na.length-1; //dead code in general (might be executed due to rounding errors)
			}else{
				//select uniformly at random from unexplored
				return unexplored.get(rng.nextInt(unexplored.size()));
			}
		}else{
			return rng.nextInt(dc.getNumberOfAlternatives());
		}
	}
	
	@Override
	public void learn(Experience exp){
		//derive the q-value
		n = exp.compute_n();
		ss = exp.getStateSpace();
	}
	
	

}
