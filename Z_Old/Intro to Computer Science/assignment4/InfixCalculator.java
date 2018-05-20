/**
 * @file InfixCalculator.
 * Calculate Infix expressions.
 *
 * @author Amir Arbel
 */
public class InfixCalculator extends Calculator {

    /**
     * Evaluate an infix expression.
     *
     * @param exp
     *  Expression to evaluate.
     */
    public void evaluate(String exp) {
        PeekableStackAsArray stack = new PeekableStackAsArray();
        ExpTokenizer tokenizer = new ExpTokenizer(exp, true);
        StackAsArray parenthesisStack = new StackAsArray();

        while (tokenizer.hasNext()) {
            CalcToken currentToken = tokenizer.nextElement();
            collapse(stack, currentToken);
            stack.push(currentToken);

            // Handle parenthesis.
            if (currentToken instanceof OpenBracket) {
                parenthesisStack.push(currentToken);
            }
            else if (currentToken instanceof CloseBracket) {
                if (parenthesisStack.isEmpty()) {
                    this.currentResult = Double.NaN;
                    throw new ParseException("SYNTAX ERROR: invalid parentheses format");
                }
                parenthesisStack.pop();
            }
        }

        // Any parenthesis left?
        if (!parenthesisStack.isEmpty()) {
            this.currentResult = Double.NaN;
            throw new ParseException("SYNTAX ERROR: invalid parentheses format");
        }

        while (stack.size() > 1) {
            collapse(stack, null);
        }
        CalcToken lastItem = (CalcToken) stack.pop();
        this.currentResult = ((ValueToken) lastItem).getValue();
    }

    /**
     * Helper function to evaluate infix expressions.
     *
     * @param stack
     *  Peekable stack to use.
     * @param newToken
     *  Next token to enter the stack.
     */
    public void collapse(PeekableStackAsArray stack, CalcToken newToken) {
        boolean endCollapse = false;

        while (!endCollapse && stack.size() >= 3) {
            // Peek at the three last tokens.
            CalcToken firstToken = (CalcToken) stack.peek(0);
            CalcToken secondToken = (CalcToken) stack.peek(1);
            CalcToken thirdToken = (CalcToken) stack.peek(2);

            if (firstToken instanceof ValueToken && thirdToken instanceof ValueToken && secondToken instanceof BinaryOp &&
                ((newToken instanceof BinaryOp && ((BinaryOp) newToken).getPrecedence() <= ((BinaryOp) secondToken).getPrecedence()) ||
                !(newToken instanceof BinaryOp))) {
                    // Pop the first three values and push the result of their operation.
                    stack.pop();
                    stack.pop();
                    stack.pop();

                    double result = ((BinaryOp) secondToken).operate(((ValueToken) thirdToken).getValue(), ((ValueToken) firstToken).getValue());
                    stack.push(new ValueToken(result));
            }
            // Reversed since the ExpTokenizer deals with right-to-left by default.
            else if (thirdToken instanceof OpenBracket && firstToken instanceof CloseBracket) {
                // Remove brackets.
                stack.pop();
                stack.pop();
                stack.pop();

                stack.push(secondToken);
            }
            // Handle badly formatted expressions.
            else if (
                        // Two adjacent numbers with no operator.
                        (firstToken instanceof ValueToken && secondToken instanceof ValueToken) ||
                        (thirdToken instanceof ValueToken && secondToken instanceof ValueToken) ||
                        // Two adjacent operators with no numbers.
                        ((firstToken instanceof CloseBracket || firstToken instanceof BinaryOp) && (secondToken instanceof BinaryOp || secondToken instanceof OpenBracket)) ||
                        ((thirdToken instanceof OpenBracket || thirdToken instanceof BinaryOp) && (secondToken instanceof CloseBracket || secondToken instanceof BinaryOp))
                    ) {
                this.currentResult = Double.NaN;
                throw new ParseException("SYNTAX ERROR: invalid infix expression format");
            }
            else {
                endCollapse = true;
            }
        }
    }
}
