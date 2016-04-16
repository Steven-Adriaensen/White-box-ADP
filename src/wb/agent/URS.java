package wb.agent;

import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;

/**
 * This agent selects an alternative uniformly at random.
 * 
 * @author Steven Adriaensen
 *
 */
public class URS implements Agent{
	private Random rng;
	
	/**
	 * Constructs a URS agent
	 * 
	 * @param rng the random generator used
	 */
	public URS(Random rng){
		this.rng = rng;
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		return rng.nextInt(dc.getNumberOfAlternatives());
	}

}
