package wb.agent;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;
import wb.util.StateSpace;

/**
 * 
 * An agent always making the same decision for each design choice
 * 
 * This class of 'agent' is returned by the black-box optimizer
 * 
 * @author Steven Adriaensen
 *
 */
public class Configuration implements Agent{
	final private int[] config;
	final private StateSpace ss;
	
	/**
	 * Constructs a Configuration agent
	 * 
	 * @param config an integer array representation of the configuration, 
	 * where config[i] = j corresponds to making the j'th decision for the i'th design choice
	 * 
	 * @param ss specifies which index of the array corresponds to which design choice
	 */
	Configuration(int[] config, StateSpace ss){
		this.config = config;
		this.ss = ss;
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		return config[ss.dc2index(dc)];
	}
	
	@Override
	public int hashCode(){
		return Arrays.hashCode(config);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Configuration){
			Configuration other_c = (Configuration) other;
			return Arrays.equals(other_c.config, config);
		}else{
			return false;
		}
	}
	
	/**
	 * @param ss the state space
	 * @param rng the random number generator used
	 * @return returns a random configuration from given state space (each as likely)
	 */
	public static Configuration sample_uniform(StateSpace ss, Random rng){
		int[] config = new int[ss.getNumberOfDesignChoices()];
		for(int i = 0; i < ss.getNumberOfDesignChoices(); i++){
			config[i] = rng.nextInt(ss.index2dc(i).getNumberOfAlternatives());
		}
		return new Configuration(config,ss);
	}
	
}
