package wb.agent;

import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;

public class URS implements Agent{
	private Random rng;
	
	public URS(Random rng){
		this.rng = rng;
	}

	@Override
	public int make(Map<DesignChoice<?>, Integer> decisions, DesignChoice<?> dc) {
		return rng.nextInt(dc.getNumberOfAlternatives());
	}

}
