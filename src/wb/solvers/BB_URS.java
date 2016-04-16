package wb.solvers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import wb.adp.AlgorithmDesignProblem;
import wb.agent.Agent;
import wb.agent.Configuration;
import wb.util.ArithmeticAverage;
import wb.util.StateSpace;

/**
 * A simple black-box optimizer.
 * 
 * It will evaluate configurations drawn uniformly at random.
 * 
 * For each configuration it maintains the arithmetic average of f for each configuration.
 * It returns the configuration with the highest estimate after N evaluations.
 * 
 * @author Steven Adriaensen
 *
 */
public class BB_URS implements Solver{
	
	final int N;
	final Random rng;
	
	/**
	 * Constructs a BB_URS solver
	 * 
	 * @param N the total number of evaluations to perform
	 * @param rng the random number generator used to select configurations
	 */
	public BB_URS(int N, Random rng){
		this.N = N;
		this.rng = rng;
	}

	@Override
	public Agent solve(AlgorithmDesignProblem adp) {
		Map<Configuration, wb.util.ArithmeticAverage> results = new HashMap<Configuration, ArithmeticAverage>();
		StateSpace ss = new StateSpace(adp.getDesignChoices());
		Configuration c = Configuration.sample_uniform(ss, rng);
		Configuration c_best = c;
		for(int i = 0; i < N; i++){
			double f = AlgorithmDesignProblem.computeF(adp.evaluate(c)); //here white box info is abstracted into a single f value
			updateResults(c,f,results);
			if(getAvgF(c,results) > getAvgF(c_best,results)){
				c_best = c;
			}
			c = Configuration.sample_uniform(ss, rng);
		}
		return c_best;
	}
	
	private void updateResults(Configuration c, double f, Map<Configuration, ArithmeticAverage> results){
		if(results.containsKey(c)){
			results.get(c).update(f);
		}else{
			results.put(c, new ArithmeticAverage(f));
		}
	}
	
	private double getAvgF(Configuration c, Map<Configuration, ArithmeticAverage> results){
		if(results.containsKey(c)){
			return results.get(c).avg;
		}else{
			return 0;
		}
	}
	
	

}
