package wb.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import wb.adp.DesignChoice;
import wb.util.StateSpace.FullTransition;
import wb.util.StateSpace.State;
import wb.util.StateSpace.StateAction;
import wb.adp.AlgorithmDesignProblem.Transition;

/**
 * This class represents experience in the white box adp.
 * 
 * It is build from multiple evaluations on the same adp 
 * (potentially using different agents)
 * 
 * This implementation builds a maximum likelihood estimation of the underlying MDP
 * 
 * @author Steven Adriaensen
 *
 */
public class Experience{ //maximum likelihood estimate of MDP
	private final StateSpace ss;
	
	private Map<FullTransition,ArithmeticAverage> data = new TreeMap<FullTransition,ArithmeticAverage>();
	
	/**
	 * Construct a new experience instance
	 * @param dcs the list of design choices in the adp
	 */
	public Experience(List<DesignChoice<?>> dcs){
		ss = new StateSpace(dcs);
	}
	
	/**
	 * Update experience adding the result of a white box evaluation
	 * 
	 * @param wb_info the list of transitions observed during an evaluation
	 */
	public void add(List<Transition> wb_info) {
		Map<DesignChoice<?>,Integer> decisions = new HashMap<DesignChoice<?>,Integer>();
		
		Transition t = null;
		
		for(Transition t_next : wb_info){
			if(t != null){
				updateData(ss.new FullTransition(decisions,t,t_next),t.reward);
				if(t.dc != null){
					decisions.put(t.dc,t.dd);
				}
			}
			t = t_next;
		}
		
		updateData(ss.new FullTransition(decisions,t,null),t.reward);	
	}
	
	/**
	 * Computes n, a function returning the expected number of different execution paths
	 * for each observed state (design choice + past decisions) action (decision) pair.
	 *  
	 * @return n
	 */
	public Map<StateAction,Integer> compute_n(){
		Map<StateAction,Integer> n_values = new HashMap<StateAction,Integer>();
		
		Iterator<Entry<FullTransition,ArithmeticAverage>> it = data.entrySet().iterator();
		int n = 0;
		int nval = 0;
		
		Entry<FullTransition,ArithmeticAverage> entry = it.next();
		FullTransition t = entry.getKey();
		ArithmeticAverage avg = entry.getValue();
		
		StateAction sa = entry.getKey().sa;
		int[] dds = Arrays.copyOf(t.sa.s.dds,t.sa.s.dds.length);
		dds[t.sa.s.dc] = t.sa.dd;
		
		while(true){
			//- update n
			n += avg.n();
			//- update q
			nval += avg.n()*getN(ss.new State(dds,t.dc2),n_values);
			
			if(it.hasNext()){
				entry = it.next();
				t = entry.getKey();
				avg = entry.getValue();
				if(!sa.equals(t.sa)){
					nval = nval/n;
					n_values.put(sa, nval);
					//reset
					n = 0;
					nval = 0;
					sa = t.sa;
					dds = Arrays.copyOf(t.sa.s.dds,t.sa.s.dds.length);
					if(t.sa.s.dc != -1)
						dds[t.sa.s.dc] = t.sa.dd;
				}
			}else{
				nval = nval/n;
				n_values.put(sa, nval);
				break;
			}

		}
		return n_values;
	}
	
	private int getN(State s, Map<StateAction,Integer> n_values){
		if(s.dc == -1){
			return 1; //terminal state
		}else{
			int n = 0;
			StateAction sa = ss.new StateAction(s,-1);
			for(sa.dd = 0; sa.dd < ss.index2dc(s.dc).getNumberOfAlternatives(); sa.dd++){
				n += getN(sa,n_values);
			}
			return n;
		}
	}
	
	private int getN(StateAction sa, Map<StateAction,Integer> n_values){
		Integer v = n_values.get(sa); // fetch n value
		if(v == null){
			return 1; //n-value for any state action pair that didn't occur is 1
			/*
			//alternatively, assume each combination of decisions for open design choices leads
			//to a different execution path (works better in many cases)
			int n = 1;
			for(int i = 0; i < ss.getNumberOfDesignChoices(); i++){
				if(sa.s.dds[i] == -1){
					n *= ss.index2dc(i).getNumberOfAlternatives();
				}
			}
			return n;
			*/
		}
		return v;
	}
	
	/**
	 * Computes q, a function returning the expected future reward
	 * for each observed state (design choice + past decisions) action (decision) pair.
	 * 
	 * @return n
	 */
	public Map<StateAction,Double> compute_q(){
		Map<StateAction,Double> q = new HashMap<StateAction,Double>();
		
		Iterator<Entry<FullTransition,ArithmeticAverage>> it = data.entrySet().iterator();
		int n = 0;
		double qval = 0;
		
		Entry<FullTransition,ArithmeticAverage> entry = it.next();
		FullTransition t = entry.getKey();
		ArithmeticAverage avg = entry.getValue();
		
		StateAction sa = entry.getKey().sa;
		int[] dds = Arrays.copyOf(t.sa.s.dds,t.sa.s.dds.length);
		dds[t.sa.s.dc] = t.sa.dd;
		
		while(true){
			//- update n
			n += avg.n();
			//- update q
			qval += avg.n() * (avg.avg + getV(ss.new State(dds,t.dc2),q));
			
			if(it.hasNext()){
				entry = it.next();
				t = entry.getKey();
				avg = entry.getValue();
				if(!sa.equals(t.sa)){
					qval = qval/n;
					q.put(sa, qval);
					//reset
					n = 0;
					qval = 0;
					sa = t.sa;
					dds = Arrays.copyOf(t.sa.s.dds,t.sa.s.dds.length);
					if(t.sa.s.dc != -1)
						dds[t.sa.s.dc] = t.sa.dd;
				}
			}else{
				qval = qval/n;
				q.put(sa, qval);
				break;
			}

		}
		return q;
	}
	
	/**
	 * @return the state-space of this experience
	 */
	public StateSpace getStateSpace(){
		return ss;
	}
	
	private double getV(State s, Map<StateAction,Double> q){
		if(s.dc == -1){
			return 0; //terminal state
		}else{
			int n = ss.index2dc(s.dc).getNumberOfAlternatives();
			double v = Double.NEGATIVE_INFINITY;
			StateAction sa = ss.new StateAction(s,-1);
			for(sa.dd = 0; sa.dd < n; sa.dd++){
				v = Math.max(v,getQ(sa,q));
			}
			return v;
		}
	}
	
	private double getQ(StateAction sa, Map<StateAction,Double> q){
		Double v = q.get(sa); // fetch q value
		if(v == null){
			return Double.NEGATIVE_INFINITY; //q-value for any state action pair that didn't occur is -inf
		}
		return v;
	}
	
	private void updateData(FullTransition ft, double reward){
		if(data.containsKey(ft)){
			data.get(ft).update(reward);
		}else{
			data.put(ft,new ArithmeticAverage(reward));
		}
	}
}
