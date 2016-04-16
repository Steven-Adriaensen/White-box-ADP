package wb.adp;
import java.util.List;

import wb.agent.Agent;

public interface AlgorithmDesignProblem {
	List<DesignChoice<?>> getDesignChoices();
	List<Transition> evaluate(Agent agent);
	
	public class Transition{
		public DesignChoice<?> dc;
		public int dd;
		public double reward;
		
		Transition(DesignChoice<?> dc, int dd){
			this.dc = dc;
			this.dd = dd;
		}
	}
	
}
