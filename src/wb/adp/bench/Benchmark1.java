package wb.adp.bench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wb.adp.BaseCOADP;
import wb.adp.DesignChoice;

/**
 * 
 * The implementation of benchmark 1:
 * In this benchmark the agent is to make 20 design decisions between true or false
 * in a fixed order. As long as it chooses true, it will receive a reward drawn uniformly at
 * random from a normal distribution with mean 1 and standard deviation 2. Otherwise, execution
 * terminates.
 * 
 * @author Steven Adriaensen
 *
 */
public class Benchmark1 extends BaseCOADP{
	final Random rng;
	final List<DesignChoice<Boolean>> dcs;
	
	/**
	 * Constructs an instance of benchmark 1
	 * 
	 * @param rng the random number generator used to generate the random feedback signal
	 */
	public Benchmark1(Random rng){
		this.rng = rng;
		dcs = new ArrayList<DesignChoice<Boolean>>(20);
		for(int i = 0; i < 20; i++){
			dcs.add(DesignChoice.TrueOrFalse());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DesignChoice<?>> getDesignChoices() {
		return (List<DesignChoice<?>>)(List<?>) dcs;
	}

	@Override
	public void execute() {
		int i = 1;
		while(i <= 20 && getDecision(dcs.get(i-1))){
			feedback(1+2*rng.nextGaussian());
			i++;
		}
	}

}
