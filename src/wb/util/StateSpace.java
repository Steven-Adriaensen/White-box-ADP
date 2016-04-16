package wb.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wb.adp.DesignChoice;
import wb.adp.AlgorithmDesignProblem.Transition;

/**
 * This is an auxiliary class, mapping Design Choices to integer indices.
 * 
 * It also provides 
 * - convenience methods converting between both representations.
 * - classes (compactly) representing states(actions) and transitions in the MDP_COADP.
 * 
 * the latter are suitable to be used as keys for hashmaps.
 * FullTransitions are furthermore fully ordered.
 * 
 * @author Steven Adriaensen
 *
 */
public class StateSpace {
	private final List<DesignChoice<?>> dcs; //index -> dc
	private final Map<DesignChoice<?>,Integer> index; //dc -> index
	private final int[] acc_a;
	
	/**
	 * Creates a state space object from a given list of design choices.
	 * The index in this list is the index of this design choice.
	 * 
	 * @param dcs the list of design choices
	 */
	public StateSpace(List<DesignChoice<?>> dcs){
		this.dcs = dcs;
		index = new HashMap<DesignChoice<?>,Integer>();
		int i = 0;
		for(DesignChoice<?> dc : dcs){
			index.put(dc, i);
			i++;
		}
		acc_a = new int[dcs.size()];
		acc_a[0] = dcs.get(0).getNumberOfAlternatives();
		for(int j = 1; j < acc_a.length; j++){
			acc_a[j] = acc_a[j-1] + dcs.get(j).getNumberOfAlternatives();
		}
	}
	
	/**
	 * @return the number of design choices in this state space
	 */
	public int getNumberOfDesignChoices(){
		return dcs.size();
	}
	
	/**
	 * @param index an index
	 * @return the design choice corresponding to given index
	 */
	public DesignChoice<?> index2dc(int index){
		if(index >= 0){
			return dcs.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * @param dc a design choice
	 * @return the index corresponding to given design choice
	 */
	public int dc2index(DesignChoice<?> dc){
		if(dc != null){
			return index.get(dc);
		}else{
			return -1;
		}
	}
	
	/**
	 * 
	 * @param decisions a map, mapping a subset of design choices to a decision.
	 * @return an array representation where -1 represents an open design choice.
	 */
	public int[] dds2array(Map<DesignChoice<?>,Integer> decisions){
		int[] dds = new int[dcs.size()];
		for(int i = 0; i < dds.length; i++){
			DesignChoice<?> dc = index2dc(i);
			if(decisions.containsKey(dc)){
				dds[i] = decisions.get(dc);
			}else{
				dds[i] = -1;
			}
		}
		return dds;
	}
	
	/**
	 * A class representing a state in the MDP_coadp
	 * - the design decisions made thus far
	 * - the design choice currently faced
	 * 
	 * This class is suitable to be used as key for a hashmap
	 * 
	 * @author Steven Adriaensen
	 *
	 */
	public class State{
		public int[] dds;
		public int dc;
		
		public State(int[] dds, int dc) {
			this.dds = dds;
			this.dc = dc;
		}
		
		public State(State s) {
			this.dds = Arrays.copyOf(s.dds,s.dds.length);
			this.dc = s.dc;
		}
		
		public State(Map<DesignChoice<?>,Integer> decisions, DesignChoice<?> dc) {
			this.dds = dds2array(decisions);
			this.dc = dc2index(dc);
		}
		
		@Override
	    public int hashCode() {
			long hashcode = 0;
			for(int i = 0; i < dcs.size(); i++){
				hashcode *= dcs.get(i).getNumberOfAlternatives()+1;
				hashcode += 1+dds[i];
			}
			hashcode *= acc_a[acc_a.length-1]+1;
	        return new Long(hashcode).hashCode();
		}
		
		@Override
	    public boolean equals(Object obj) {
	        if(obj instanceof State){
	        	State os = (State) obj;
	        	if(os.dc == dc){
	        		for(int i = 0; i < dds.length; i++){
	        			if(os.dds[i] != dds[i]){
	        				return false;
	        			}
	        		}
	        		return true;
	        	}
	        	return false;
	        }
	        return false;
	    }
		
	}
	
	/**
	 * A class representing a state action pair in the MDP_coadp
	 * 
	 * This class is suitable to be used as key for a hashmap
	 * 
	 * @author Steven Adriaensen
	 *
	 */
	public class StateAction{
		public State s;
		public int dd;
		
		public StateAction(State s, int dd){
			this.s = s;
			this.dd = dd;
		}
		
		StateAction(int[] dds, int dc, int dd){
			this(new State(dds,dc),dd);
		}
		
		StateAction(StateAction sa){
			this(new State(sa.s),sa.dd);
		}
		
		@Override
	    public int hashCode() {
			long hashcode = s.hashCode();
			hashcode += s.dc > 0? acc_a[s.dc-1]+dd+1 : (s.dc > -1? dd+1 : 0);
	        return new Long(hashcode).hashCode();
		}
		
		@Override
	    public boolean equals(Object obj) {
	        if(obj instanceof StateAction){
	        	StateAction osa = (StateAction) obj;
	        	if(osa.dd == dd){
	        		return osa.s.equals(s);
	        	}
	        	return false;
	        }
	        return false;
	    }
	}
	
	/**
	 * A class representing a transition in the MDP_coadp:
	 * - state (design choice face and decisions made thus far)
	 * - action (decision for design choice)
	 * the next design choice faced
	 * 
	 * This class is suitable to be used as key for a hashmap.
	 * In addition it is fully ordered, furthermore this order is topological.
	 * 
	 * This is a property which is exploited to compute q/n in Experience, using dynamic programming.
	 * 
	 * @author Steven Adriaensen
	 *
	 */
	public class FullTransition implements Comparable<FullTransition>{
		public StateAction sa;
		public int dc2;
		public int open;
		
		public FullTransition(Map<DesignChoice<?>,Integer> decisions, Transition t, Transition t_next){
			this.sa = new StateAction(dds2array(decisions),dc2index(t.dc),t.dd);
			if(t_next == null){
				this.dc2 = -1;
			}else{
				this.dc2 = dc2index(t_next.dc);
			}
			open = dcs.size()-decisions.size();
		}
		
		@Override
	    public int hashCode() {
			long hashcode = sa.hashCode();
			hashcode += dc2+1;
	        return new Long(hashcode).hashCode();
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if(obj instanceof FullTransition){
	        	FullTransition ot = (FullTransition) obj;
	        	if(ot.open == open && ot.dc2 == dc2){
	        		return ot.sa.equals(sa);
	        	}
	        	return false;
	        }
	        return false;
	    }

		@Override
		public int compareTo(FullTransition o) {
			if(o.open == open){
				if(o.sa.s.dc == sa.s.dc){
					if(o.sa.dd == sa.dd){
						if(o.dc2 == dc2){
							//sort based on first different
							for(int i = 0; i < o.sa.s.dds.length; i++){
								if(sa.s.dds[i] != o.sa.s.dds[i]){
									if(sa.s.dds[i] > o.sa.s.dds[i]){
										return 1;
									}else{
										return -1;
									}
								}
							}
							return 0;
						}else if(o.dc2 < dc2){
							return 1;
						}else{
							return -1;
						}
					}else if(o.sa.dd < sa.dd){
						return 1;
					}else{
						return -1;
					}
				}else if(o.sa.s.dc < sa.s.dc){
					return -1;
				}else{
					return 1;
				}
			}else if (o.open < open){
				return 1;
			}else{
				return -1;
			}
		}
		
	}
}
