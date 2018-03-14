/**
 * @file ParseException
 * Exception handling for parse errors.
 *
 * @author Amir Arbel
 */
public class ParseException extends RuntimeException {

    /**
     * Exception handling.
     *
     * @param message
     *  Message to send.
     */
    public ParseException(String message) {
        super(message);
    }
}
