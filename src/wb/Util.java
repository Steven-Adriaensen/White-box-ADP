package wb;
import java.util.List;

import wb.adp.AlgorithmDesignProblem.Transition;

public class Util {
	
	public static int[] copyArray(int[] a){
		int[] ca = new int[a.length];
		System.arraycopy(a, 0, ca, 0, a.length);
		return ca;
	}

	public static double computeF(List<Transition> wb_info){
		double f = 0;
		for(Transition t : wb_info){
			f += t.reward;
		}
		return f;
	}
	
	public static class ArithmeticAverage{
		private int n;
		public double val;
		
		public ArithmeticAverage(double v){
			this.val = v;
			this.n = 1;
		}
		
		public void update(double v){
			n++;
			val += (v-val)/n;
		}
		
		public int n(){
			return n;
		}
		
	}
	
}
