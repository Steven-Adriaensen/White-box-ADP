package wb.test;

import java.util.Random;

import wb.Util;
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

public class Example {

	public static void main(String[] args) {
		final int N = 100000;
		final Random rng = new Random(0x12345678);
		
		AlgorithmDesignProblem adp;
		//adp = new Benchmark1(rng);
		adp = new Benchmark2(rng);
		
		Solver solver;
		//solver = new BB_URS(N,rng); //BB-URS
		
		solver = new WB(
				//new URS(rng), //WB-URS
				new PURS(rng), //WB-PURS
				//new EpsilonGreedy(0.1,rng), //WB-EG
				N,rng);
		
		
		Agent solution = solver.solve(adp);
		
		//evaluate test performance based on N evaluations
		double f_avg_test = 0;
		for(int i = 0; i < N; i++){
			double f = Util.computeF(adp.evaluate(solution));
			f_avg_test += (f-f_avg_test)/(i+1);
		}
		System.out.println("test performance: "+f_avg_test);
	}

}
