package wb.adp;

public interface DesignChoice<T> {
	int getNumberOfAlternatives();
	T getAlternative(int i);
	
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