/**
 * Abstract "superclass" describing ALL tokens seen when evaluating
 * arithmetic expressions.
 */
public abstract class Calculator {
    protected double currentResult;

    /**
     * Evaluates expression.
     *
     * @param exp
     *   Expression to evaluate.
     */
    public abstract void evaluate(String exp);

    /**
     * Get current result of the last expression that was evaluated.
     *
     * @return
     *   Last expression evaluated; if none, returns null.
     */
    public double getCurrentResult() {
        return currentResult;
    }
}
