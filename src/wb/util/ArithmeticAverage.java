package wb.util;

/**
 * A convenience class for representing the arithmetic average of a collection of numbers
 * 
 * @author Steven Adriaensen
 *
 */
public class ArithmeticAverage{
	private int n;
	public double avg; //the current arithmetic average
	
	/**
	 * Initialises an arithmetic average of a single number (= single number)
	 * 
	 * @param v value of a single number
	 */
	public ArithmeticAverage(double v){
		this.avg = v;
		this.n = 1;
	}
	
	/**
	 * updates an arithmetic average, adding a new number to the collection.
	 * 
	 * @param v new number to be added
	 */
	public void update(double v){
		n++;
		avg += (v-avg)/n;
	}
	
	/**
	 * @return the size of the underlying collection
	 */
	public int n(){
		return n;
	}
	
}