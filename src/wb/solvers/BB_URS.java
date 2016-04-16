package wb.solvers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import wb.Util;
import wb.Util.ArithmeticAverage;
import wb.adp.AlgorithmDesignProblem;
import wb.agent.Agent;
import wb.agent.Configuration;
import wb.agent.StateSpace;

public class BB_URS implements Solver{
	
	final int N;
	final Random rng;
	
	public BB_URS(int N, Random rng){
		this.N = N;
		this.rng = rng;
	}

	@Override
	public Agent solve(AlgorithmDesignProblem adp) {
		Map<Configuration, ArithmeticAverage> results = new HashMap<Configuration, ArithmeticAverage>();
		StateSpace ss = new StateSpace(adp.getDesignChoices());
		Configuration c = Configuration.sample_uniform(ss, rng);
		Configuration c_best = c;
		for(int i = 0; i < N; i++){
			double f = Util.computeF(adp.evaluate(c)); //here white box info is abstracted into a single f value
			updateResults(c,f,results);
			if(getAvgF(c,results) > getAvgF(c_best,results)){
				c_best = c;
			}
			c = Configuration.sample_uniform(ss, rng);
		}
		System.out.println(results.get(c_best).val);
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
			return results.get(c).val;
		}else{
			return 0;
		}
	}
	
	

}
