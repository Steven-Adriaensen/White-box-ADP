package wb.agent;
import java.util.Map;

import wb.adp.DesignChoice;

public interface Agent {
	int make(Map<DesignChoice<?>,Integer> decisions, DesignChoice<?> dc);
	default void learn(Experience exp){};
}
