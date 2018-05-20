/**
*	This class handles a string expression and splits it into tokens.
*/

public class ExpTokenizer {
	
	private boolean leftToRight; 
	private int start;
	private int finish;
	private int curr;
	private int acc;
	private String[] splitedExp;

	/**
	* Constructor:
	* @param exp The expression.
	* @param leftToRight The Tokenizing orientation.
	*		  if this parameter is true then this tokenizer will read the tokens from left to right,
	*		  otherwise, it will read the tokens from right to left.
	*/
	public ExpTokenizer(String exp, boolean leftToRight) {
		this.leftToRight = leftToRight;
		this.splitedExp = exp.split(" ");
		if (this.leftToRight) {
			this.start = 0;
			this.finish = this.splitedExp.length;
			this.acc = 1;
		}
		else {
			this.start = this.splitedExp.length-1;
			this.finish = -1;
			this.acc = -1;
		}
		this.curr = this.start;
	}
	
	/**
	* @return true if the current state contains more tokens.
	*/
	public boolean hasNext() {
		return (this.curr != this.finish);
	}
	
	/**
	* @return the next element (token) if one exist or null otherwise.
	*/
	public CalcToken nextElement() {
		CalcToken resultToken = null;
		String token = nextString();
		if (token != null) {
			if (token.equals("+")) {
				resultToken =  new AddOp();
			}
            else if (token.equals("*")) {
				resultToken =  new MultiplyOp();
			}
            else if (token.equals("/")) {
                resultToken = new DivideOp();
            }
            else if (token.equals("^")) {
                resultToken = new PowOp();
            }
            else if (token.equals("-")) {
                resultToken = new SubtractOp();
            }
            else if (token.equals("(")) {
                resultToken = new OpenBracket();
            }
            else if (token.equals(")")) {
                resultToken = new CloseBracket();
            }
			else {
                try {
				    resultToken = new ValueToken(Double.parseDouble(token));
                }
                catch (NumberFormatException nfe) {
                    throw new ParseException("SYNTAX ERROR: \"" + token.toString() +"\" is not a number.");
                }
			}
		}			
		return resultToken;	
	}


	private String nextString() {
		String next = null;
		if (hasNext()) {
			next = splitedExp[curr];
			curr = curr + acc;
		}
		return next;
	}

	/**
	* @return the number of remaining tokens in the current state.
	*/
	public int countTokens() {
		return Math.abs(this.finish - this.curr);
	}
	
}
