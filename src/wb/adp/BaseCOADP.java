package wb.adp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wb.agent.Agent;

//does most of the book-keeping, enabling high-level design space implementations
public abstract class BaseCOADP implements AlgorithmDesignProblem {
	private Map<DesignChoice<?>,Integer> decisions = new HashMap<DesignChoice<?>,Integer>();
	private LinkedList<Transition> transitions;
	private Agent agent;

	@Override
	public List<Transition> evaluate(Agent agent) {
		decisions.clear();
		transitions = new LinkedList<Transition>();
		transitions.add(new Transition(null,-1));
		this.agent = agent;
		execute();
		return transitions;
	}
	
	protected <T> T getDecision(DesignChoice<T> dc){
		int alt;
		if(decisions.containsKey(dc)){
			//we made design decision before
			alt = decisions.get(dc);
		}else{
			//design decision is still open
			//query the agent
			alt = agent.make(decisions, dc);
			decisions.put(dc, alt);
			transitions.add(new Transition(dc,alt));
		}
		return dc.getAlternative(alt);
	}
	
	protected void feedback(double reward){
		transitions.peekLast().reward += reward;
	}
	
	abstract public void execute();
	
	

}
