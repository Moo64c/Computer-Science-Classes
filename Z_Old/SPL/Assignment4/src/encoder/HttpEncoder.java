/**
 * 
 */
package encoder;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import tokenizer_http.HttpMessage;
import tokenizer_http.HttpMessage.REQUEST_TYPE;
import tokenizer_whatsapp.WhatsAppMessage;

/**
 * @author Yotam
 *
 */
public class HttpEncoder implements Encoder<HttpMessage> {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected Charset _charset;
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String NEW_LINE_REGEX = "\r\n";
	public static final String SPACE_REGEX = " ";
	public static final String  HEADER_SPLIT_REGEX = ": ";
	
	public HttpEncoder() {
		_charset = Charset.forName(DEFAULT_CHARSET);
	}

	@Override
	public byte[] toBytes(HttpMessage obj) {
		return obj.toString().getBytes(_charset);
	}

	@Override
	public HttpMessage toObject(String stringRepresentation) {
		if (stringRepresentation.equals("")) {
			logger.severe("Empty string.");
			return null;
		}
		HttpMessage.REQUEST_TYPE requestType = getRequestType(stringRepresentation);
		String requestURI = getRequestURI(stringRepresentation);
		Map<String,String> headerMap = getHeaderMap(stringRepresentation);
		String messageBody = getMessageBody(stringRepresentation);
		
		return new HttpMessage(headerMap, messageBody, requestType, requestURI);
	}

	private String getRequestURI(String stringRepresentation) {
		String[] firstLine = getFirstLine(stringRepresentation);
		if (firstLine.length < 2)
			return null;
		else 
			return firstLine[1];

	}

	/**
	 * Get message body as text
	 * @param msg
	 * @return
	 */
	protected String getMessageBody(String msg) {
		String ans = null;

		msg = WhatsAppMessage.trim(msg);
		
		if (msg.indexOf("\n\n") > 0) {
			ans = msg.substring(msg.indexOf("\n\n") + "\n\n".length());
		}
		
		if (msg.indexOf(NEW_LINE_REGEX + NEW_LINE_REGEX) > 0) {
			ans = msg.substring(msg.indexOf(NEW_LINE_REGEX + NEW_LINE_REGEX) 
					+ (NEW_LINE_REGEX + NEW_LINE_REGEX).length());
		}
		
		if (ans == null) {
			return ans;
		}
		
		// Remove extra new line characters.
		while((ans.endsWith("\n") || ans.endsWith("\r"))) {
			ans = ans.substring(0, ans.length() - "\n".length());
		}
		
		return ans;
	}
	
	/**
	 * generate TreeMap of headers from http request.
	 * @param stringRepresentation
	 * @return
	 */
	protected Map<String, String> getHeaderMap(String stringRepresentation) {
		String[] lines = stringRepresentation.split("\n");
		Map<String, String> headerMap = new TreeMap<String, String>();
		int index = 1; // iterator index that begins from second line (if exists)

		while (index < lines.length && lines[index].indexOf(HEADER_SPLIT_REGEX) != -1){
			String key = lines[index].substring(0, lines[index].indexOf(HEADER_SPLIT_REGEX));
			String value = lines[index].substring(lines[index].indexOf(HEADER_SPLIT_REGEX) + HEADER_SPLIT_REGEX.length());
			headerMap.put(key, value);
			index++;
		}
		
		return headerMap;
	}
	/**
	 * parses request type from http message.
	 * @param stringRepresentation
	 * @return
	 */
	protected REQUEST_TYPE getRequestType(String stringRepresentation) {
		String[] firstLine = getFirstLine(stringRepresentation);
		
		if (firstLine.length == 0)
			return REQUEST_TYPE.INVALID;
		if (firstLine[0].toLowerCase().equals("get"))
			return REQUEST_TYPE.GET;
		else if (firstLine[0].toLowerCase().equals("post"))
			return REQUEST_TYPE.POST;
		else 
			return REQUEST_TYPE.INVALID;
	}
	
	/**
	 * Auxiliary method that returns first line of request split by spaces. if empty returns null.
	 * @param stringRepresentation - the full request.
	 * @return the first line of request split by spaces.
	 */
	private String[] getFirstLine(String stringRepresentation){
		String[] lines = stringRepresentation.split(NEW_LINE_REGEX);
		if (lines.length == 0)
			return null;
		String[] firstLine = lines[0].split(SPACE_REGEX);
		return firstLine;
	}
	
	/**
	 * finds the http version of the message by getting the third word from the first line.
	 * @param stringRepresentation
	 * @return
	 */
	protected String getHttpVersion(String stringRepresentation) {
		String[] firstLine = getFirstLine(stringRepresentation);
		if (firstLine.length < 3)
			return null;
		else 
			return firstLine[2];
	}

	@Override
	public Charset getEncoding() {
		return _charset;
	}

}
