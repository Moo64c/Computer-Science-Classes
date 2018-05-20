/**
 * @file Tester.java
 * This is a testing framework. Use it extensively to verify that your code is working
 * properly.
 *
 * @author Amir Arbel
 */
public class Tester {

	private static boolean testPassed = true;
	private static int testNum = 0;
	
	/**
	 * This entry function will test all classes created in this assignment.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// Each function here should test a different class.
        testBinaryOps();
        testInfixCalculator();
		testValueToken();
        testPrefixCalculator();
        testStackAsArray();
        testPeekableStackAsArray();

		// Notifying the user that the code have passed all tests.
		if (testPassed) {
			System.out.println("All " + testNum + " tests passed!");
		}
	}

    /**
     * Test StackAsArray class.
     */
    private static void testStackAsArray() {
        StackAsArray stack = new StackAsArray();

        test(stack.isEmpty(), "Stack should be empty");
        // Create stack to test.
        stack.push(new OpenBracket());
        stack.push(new ValueToken(5.1));
        stack.push(new CloseBracket());

        test(!stack.isEmpty(), "Stack should not be empty");
        test(stack.pop().toString().equals(")"), "Wrong object received.");
        test(stack.pop().toString().equals("5.1"), "Wrong object received.");
        test(stack.pop().toString().equals("("), "Wrong object received.");

        test(stack.isEmpty(), "Stack should be empty");
    }

    /**
     * Test PeekableStackAsArray class functions.
     *
     * Assuming pop(), push() and isEmpty() valid from testStackAsArray().
     */
    private static void testPeekableStackAsArray() {
        PeekableStackAsArray stack = new PeekableStackAsArray();

        // Create stack to test.
        stack.push(new OpenBracket());
        stack.push(new ValueToken(5.1));
        stack.push(new CloseBracket());

        // Peek tests.
        test(((ValueToken) stack.peek(1)).getValue() == 5.1, "failed: Testing peek 1/3");
        test(stack.peek(2).toString().equals("("), "failed: Testing peek 2/3");
        test(stack.peek(0).toString().equals(")"), "failed: Testing peek 3/3");

        // stackContent/clear/size tests.
        test(stack.stackContents().equals(")\n5.1\n(\n"), "Failed: testing stackContents()");
        test(stack.size() == 3, "Failed testing size()");
        stack.clear();
        test(stack.size() == 0, "Failed clear() and/or size()");
        test(stack.stackContents().equals(""), "Failed: testing stackContents() and/or clear().");
    }

    /**
     * Basic testing to binary operations.
     */
    private static void testBinaryOps() {
        // Test addition.
        test (new AddOp().operate(50, 100) == 150.0, "Addition binary operation failed.");
        test (new AddOp().operate(50, -100) == -50.0, "Addition binary operation failed.");

        // Test subtraction.
        test (new SubtractOp().operate(50, 100) == -50.0, "Subtract binary operation failed.");
        test (new SubtractOp().operate(50, -100) == 150.0, "Subtract binary operation failed.");

        // Test multiply.
        test (new MultiplyOp().operate(5, 100) == 500.0, "Multiply binary operation failed.");
        test (new MultiplyOp().operate(-5, 100) == -500.0, "Multiply binary operation failed.");

        // Test divide.
        test (new DivideOp().operate(50, 100) == 1 / 2.0, "Divide binary operation failed.");
        test (new DivideOp().operate(50, -100) == -1 / 2.0, "Divide binary operation failed.");

        // Test power of
        test (new PowOp().operate(2, 3) == 2 * 2 * 2.0, "Power binary operation failed.");
        test (new PowOp().operate(2, -3) == 1/ (2 * 2 * 2.0), "Power binary operation failed.");
    }

    private static void testInfixCalculator() {
        InfixCalculator infixCalculator = new InfixCalculator();

        // Basic, single operations.
        infixCalculator.evaluate("5 + 2");
        test(infixCalculator.getCurrentResult() == 7.0, "Should be 7.0");
        infixCalculator.evaluate("5 - 2");
        test(infixCalculator.getCurrentResult() == 3.0, "Should be 3.0");
        infixCalculator.evaluate("5 * 2");
        test(infixCalculator.getCurrentResult() == 10.0, "Should be 10.0");
        infixCalculator.evaluate("5 / 2");
        test(infixCalculator.getCurrentResult() == 2.5, "Should be 2.5");
        infixCalculator.evaluate("5 ^ 2");
        test(infixCalculator.getCurrentResult() == 25.0, "Should be 25.0");
        infixCalculator.evaluate("( 4 )");
        test(infixCalculator.getCurrentResult() == 4.0, "Should be 4.0");

        // More complex checks.
        infixCalculator.evaluate("4 ^ 2 * 3 / 8 + 2 - 10");
        test(infixCalculator.getCurrentResult() == (4 * 4) * 3 / 8 + 2 - 10, "Should be -2.0");
        infixCalculator.evaluate("0.5 + 2 * 3 ^ 2");
        test(infixCalculator.getCurrentResult() == 18.5, "Should be 18.5");
        infixCalculator.evaluate("( ( 10 ^ 2 ) * ( 10 - 10.02 ) )");
        test(infixCalculator.getCurrentResult() == ((10 * 10) * (10 - 10.02)), "Should be -2.0");
        infixCalculator.evaluate("( ( 10 / ( 2 ^ 2 ) / 3.75 ) * ( 6 ) ) ^ ( 0.5 * ( ( ( 10.5 - 6.5 ) ) ) )");
        test(infixCalculator.getCurrentResult() == 16.0, "Should be 16.0");

        // Test exception handling.
        int startTestNum = testNum;
        try {
            infixCalculator.evaluate("( ( 10 / ( 2 ^ 2 ) / 3.75 ) * ( 6 ) ) ^ ( 0.5 * ( ( ( 10.5 - 6.5 ) ) )");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid parentheses format"), "Wrong exception.");
        }
        try {
            infixCalculator.evaluate("( ( 10 / ( 2 ^ 2 ) / 3.75 ) * ( ) ) ^ ( 0.5 * ( ( ( 10.5 - 6.5 ) ) ) )");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid infix expression format"), "Wrong exception.");
        }
        try {
            infixCalculator.evaluate("( ( 10 / ( 2 ^ 2 ) / * 3.75 ) * ( 6 ) ) ^ ( 0.5 * ( ( ( 10.5 - 6.5 ) ) ) )");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid infix expression format"), "Wrong exception.");
        }
        try {
            infixCalculator.evaluate("( ( ( ( ( ( ( ( ( ( ( ( ( ( ( ( 5 ) ) ) ) ) ( ) ) ) ) ) ) ) ) ) ) )");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid infix expression format"), "Wrong exception.");
        }
        try {
            infixCalculator.evaluate("2 + + 2");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid infix expression format"), "Wrong exception.");
        }
        try {
            infixCalculator.evaluate("2 + 2 / 5 + * 2");
        }
        catch (ParseException pe) {
            test(pe.toString().equals("ParseException: SYNTAX ERROR: invalid infix expression format"), "Wrong exception.");
        }

        // Test all exceptions were thrown.
        test(testNum == startTestNum + 6, "Some exceptions were not thrown.");
    }

    /**
	 * This utility function will count the number of times it was invoked. 
	 * In addition, if a test fails the function will print the error message.  
	 * @param exp The actual test condition
	 * @param msg An error message, will be printed to the screen in case the test fails.
	 */
	private static void test(boolean exp, String msg) {
		testNum++;
		
		if (!exp) {
			testPassed = false;
			System.out.println("Test " + testNum + " failed: "  + msg);
		}
	}

	/**
	 * Checks the ValueToken class.
	 */
	private static void testValueToken() {
		ValueToken valueToken = new ValueToken(5.1);

		test(valueToken.getValue() == 5.1, "Value should be 5.1.");
		test(valueToken.toString().equals("5.1"), "Value toString should be 5.1.");
        valueToken = new ValueToken(5);
        test(valueToken.getValue() == 5.0, "Value should be 5.0");
        test(valueToken.toString().equals("5.0"), "Value toString should be 5.0");
	}

	/**
	 * Checks the PrefixCalculator class.
	 */
	private static void testPrefixCalculator() {
        PrefixCalculator prefixCalculator = new PrefixCalculator();

        // Basic, single operation checks.
        prefixCalculator.evaluate("+ 2 3");
        test(prefixCalculator.getCurrentResult() == 5.0, "Should be 5.0");
        prefixCalculator.evaluate("- 3 5");
        test(prefixCalculator.getCurrentResult() == 3.0 - 5.0, "Should be -2.0");
        prefixCalculator.evaluate("* 6 2");
        test(prefixCalculator.getCurrentResult() == 12.0, "Should be 12.0");
        prefixCalculator.evaluate("/ 10 4");
        test(prefixCalculator.getCurrentResult() == 10 / 4.0, "Should be 2.5");
        prefixCalculator.evaluate("^ 2 4");
        test(prefixCalculator.getCurrentResult() == 16.0, "Should be 16.0");

        // More complex checks.
        prefixCalculator.evaluate("* + 2 3 - 4 2");
        test(prefixCalculator.getCurrentResult() == 10.0, "Should be 10.0");
        prefixCalculator.evaluate("- / ^ 2 3 * 4 2 7");
        test(prefixCalculator.getCurrentResult() == -6.0, "Should be -6.0");
        prefixCalculator.evaluate("- / ^ 2 3 * 4 2 -7");
        test(prefixCalculator.getCurrentResult() == 8.0, "Should be 8.0");
	}
}

