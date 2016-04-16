package wb.adp.bench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wb.adp.BaseCOADP;
import wb.adp.DesignChoice;

public class Benchmark1 extends BaseCOADP{
	final Random rng;
	final List<DesignChoice<Boolean>> dcs;
	
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
