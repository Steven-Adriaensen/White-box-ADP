package wb.adp.bench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import wb.adp.BaseCOADP;
import wb.adp.DesignChoice;

public class Benchmark2 extends BaseCOADP{
	final Random rng;
	final List<DesignChoice<Integer>> dcs;
	final List<Integer> dc_order = Arrays.asList(0,1,2,3,4); 
	
	public Benchmark2(Random rng){
		this.rng = rng;
		dcs = new ArrayList<DesignChoice<Integer>>(5);
		for(int i = 0; i < 5; i++){
			dcs.add(DesignChoice.IntegerInRange(1, 5));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DesignChoice<?>> getDesignChoices() {
		return (List<DesignChoice<?>>)(List<?>) dcs;
	}

	@Override
	public void execute() {
		Collections.shuffle(dc_order, rng);
		for(int i = 1; i <= 5; i++){
			if(getDecision(dcs.get(dc_order.get(i-1))) == i){
				feedback(1);
			}
		}
	}

}
