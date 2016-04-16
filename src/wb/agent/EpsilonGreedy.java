package wb.agent;

import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;


public class EpsilonGreedy implements Agent{
	final private Random rng;
	final private Greedy greedy;
	final double epsilon;

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
