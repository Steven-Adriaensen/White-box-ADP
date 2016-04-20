package wb.test;

import java.util.Random;

import wb.adp.AlgorithmDesignProblem;
import wb.adp.bench.Benchmark1;
import wb.adp.bench.Benchmark2;
import wb.agent.Agent;
import wb.agent.EpsilonGreedy;
import wb.agent.PURS;
import wb.agent.URS;
import wb.solvers.BB_URS;
import wb.solvers.Solver;
import wb.solvers.WB;

/**
 * This class gives an example of how to solve a given adp, using one of the solvers and 
 * test the resulting agent.
 * 
 * @author Steven Adriaensen
 *
 */
public class Example {

	public static void main(String[] args) {
		//decide how many evaluations to perform
		final int N = 1000;
		//initialise the random generator used with a fixed seed (execution will be deterministic)
		final Random rng = new Random(0); 
		
		//create an instance of the algorithm design problem you wish to solve
		AlgorithmDesignProblem adp;
		adp = new Benchmark1(rng);
		//adp = new Benchmark2(rng);
		
		//select a solver to solve it
		Solver solver;
		solver = new BB_URS(N,rng); //BB-URS
		/*
		solver = new WB(
				new URS(rng), //WB-URS
				//new PURS(rng), //WB-PURS
				//new EpsilonGreedy(0.1,rng), //WB-EG
				N,rng);
		*/
		
		//solve the adp using the given solver
		Agent solution = solver.solve(adp);
		
		//evaluate the test performance of the resulting agent 
		//(based on N evaluations here)
		double f_avg_test = 0;
		for(int i = 0; i < N; i++){
			double f = AlgorithmDesignProblem.computeF(adp.evaluate(solution));
			f_avg_test += (f-f_avg_test)/(i+1);
		}
		System.out.println("test performance: "+f_avg_test);
	}

}
