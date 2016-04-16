package wb.adp;

/**
 * This interface specifies a Design Choice of type T.
 * 
 * A design choice defines a finite number of alternative decisions.
 * T is a common super-type of the alternatives of this design choice.
 * 
 * This class also provides some factory methods for common design choices.
 * 
 * @author Steven Adriaensen
 *
 * @param <T> the type of this design choice.
 */
public interface DesignChoice<T> {
	/**
	 * @return the number of alternative decisions
	 */
	int getNumberOfAlternatives();
	
	/**
	 * @param i an index of the alternative
	 * @return the ith alternative
	 */
	T getAlternative(int i);
	
	/**
	 * @return A design choice between true and false.
	 */
	static DesignChoice<Boolean> TrueOrFalse(){
		return new DesignChoice<Boolean>(){

			@Override
			public int getNumberOfAlternatives() {
				return 2;
			}

			@Override
			public Boolean getAlternative(int i) {
				return i == 1;
			}
			
		};
	}
	
	/**
	 * @param min the lower bound of the range
	 * @param max the upper bound of the range (inclusive)
	 * @return A design choice between integers in [min,max]
	 */
	static DesignChoice<Integer> IntegerInRange(int min, int max){
		return new DesignChoice<Integer>(){

			@Override
			public int getNumberOfAlternatives() {
				return max-min+1;
			}

			@Override
			public Integer getAlternative(int i) {
				return min+i;
			}
			
		};
	}
	
	/**
	 * @param alternatives 
	 * @return A design choice between a given array of alternative instances of type T
	 */
	@SafeVarargs
	static <T> DesignChoice<T> OneOf(T... alternatives){
		return new DesignChoice<T>(){

			@Override
			public int getNumberOfAlternatives() {
				return alternatives.length;
			}

			@Override
			public T getAlternative(int i) {
				return alternatives[i];
			}
			
		};
	}
}