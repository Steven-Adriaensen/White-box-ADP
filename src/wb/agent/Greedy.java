package wb.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;
import wb.agent.StateSpace.State;
import wb.agent.StateSpace.StateAction;

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
