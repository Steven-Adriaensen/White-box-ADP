package wb.agent;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import wb.adp.DesignChoice;

public class Configuration implements Agent{
	final private int[] config;
	final private StateSpace ss;
	
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
	
	public static Configuration sample_uniform(StateSpace ss, Random rng){
		int[] config = new int[ss.getNumberOfDesignChoices()];
		for(int i = 0; i < ss.getNumberOfDesignChoices(); i++){
			config[i] = rng.nextInt(ss.index2dc(i).getNumberOfAlternatives());
		}
		return new Configuration(config,ss);
	}
	
}
