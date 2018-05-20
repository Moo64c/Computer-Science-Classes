/**
 * @file PrefixCalculator.
 * Calculate prefix expressions.
 *
 * @author Amir Arbel
 */
public class PrefixCalculator extends Calculator {

    /**
     * Evaluate an expression.
     *
     * @param exp
     *  Expression to evaluate.
     */
    public void evaluate(String exp) {
        StackAsArray stack = new StackAsArray();
        ExpTokenizer tokenizer = new ExpTokenizer(exp, false);

        while (tokenizer.hasNext()) {
            CalcToken currentToken = tokenizer.nextElement();
            if (currentToken instanceof BinaryOp) {
                // Pop the last two elements.
                ValueToken right = (ValueToken) stack.pop();
                ValueToken left = (ValueToken) stack.pop();
                // Perform operation.
                ValueToken result = new ValueToken(((BinaryOp) currentToken).operate(right.getValue(), left.getValue()));
                // Push back to stack.
                stack.push(result);
            }
            else {
                // Assuming currentToken is a ValueToken.
                stack.push(currentToken);
            }
        }

        // The stack should only have one ValueToken left.
        currentResult = ((ValueToken) stack.pop()).getValue();
    }
}
