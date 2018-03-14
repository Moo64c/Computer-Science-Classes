/**
 * @file ValueToken
 * Token handling for numbers.
 *
 * @author Amir Arbel
 */
public class ValueToken extends CalcToken {
    private double value;

    /**
     * Constructor; sets value.
     */
    ValueToken(double value) {
        this.value = value;
    }

    /**
     * Get the value of the token.
     *
     * @return
     *  Value of the token.
     */
    public double getValue() {
        return value;
    }

	/**
     * Basic toString method.
     *
	 * @return
     * The string representation of this token.
	 */
	public String toString() {
        return "" + getValue();
    }

}
