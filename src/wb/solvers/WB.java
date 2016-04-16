package wb.solvers;

import java.util.List;
import java.util.Random;

import wb.adp.AlgorithmDesignProblem;
import wb.adp.AlgorithmDesignProblem.Transition;
import wb.agent.Agent;
import wb.agent.Experience;
import wb.agent.Greedy;

public class WB implements Solver{
	final int N;
	final Agent agent;
	final Random rng;
	
	public WB(Agent agent, int N, Random rng){
		this.agent = agent;
		this.N = N;
		this.rng = rng;
	}

	@Override
	public Agent solve(AlgorithmDesignProblem adp) {
		Experience experience = new Experience(adp.getDesignChoices());
		
		//gather experience using the learning policy
		for(int i = 0; i < N; i++){
			List<Transition> wb_info = adp.evaluate(agent);
			experience.add(wb_info);
			agent.learn(experience);
		}
		
		//use this experience to train a greedy agent (used for testing)
		Greedy greedy = new Greedy(rng);
		greedy.learn(experience);
		return greedy;
	}
	
}
