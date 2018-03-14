
public class BadInputFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6266773571091056752L;
	public BadInputFormatException(String msg) {
		super(msg);
	}
	
	@Override
	public String getMessage() {
		return getClass().getName() + ":" + super.getMessage();
	}
}
