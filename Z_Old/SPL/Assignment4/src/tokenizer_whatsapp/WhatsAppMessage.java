package tokenizer_whatsapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import encoder.HttpEncoder;
import tokenizer.Message;
import tokenizer_http.HttpMessage;

public class WhatsAppMessage extends HttpMessage implements Message<String> {
	public static final String PARAM_DELIMITER = "&";
	public static final String KEY_VALUE_DELIMITER = "=";
	
	Map<String, String> _bodyParameters;
	boolean _invalidMessageBodyFormat;
	
	/**
	 * Turns an HttpMessage to WhatsAppMessage (Should be used for request messages).
	 * @param msg
	 */
	public WhatsAppMessage(HttpMessage msg) {
		super(msg);
		_invalidMessageBodyFormat = false;
		parseBodyParameters(msg.getMessageBody());
	}
	
	/**
	 * request constructor
	 * @param headers
	 * @param messageBody
	 * @param requestType
	 * @param requestURI
	 */
	public WhatsAppMessage(Map<String, String> headers,
			String messageBody,
			tokenizer_http.HttpMessage.REQUEST_TYPE requestType,
			String requestURI) {
		super(headers, messageBody, requestType, requestURI);
		_invalidMessageBodyFormat = false;
		parseBodyParameters(messageBody);
	}
	
    /**
     * response constructor
     * @param headers
     * @param messageBody
     * @param status
     */
	public WhatsAppMessage(Map<String, String> headers, String messageBody, int status){
    	super(headers, messageBody, status);
	}

	/**
	 * parses message body to a key-value structure.
	 * @param body - the message body in a format of <key1>=<value1>&<key2>=<value2>&...
	 */
	private void parseBodyParameters(String body) {
		_bodyParameters = new TreeMap<String, String>();
		for (String item: body.split(PARAM_DELIMITER)){
			String[] keyValueArray = item.split(KEY_VALUE_DELIMITER);
			String value = "";
			if (keyValueArray.length != 2){
				_invalidMessageBodyFormat = true;
				break;
			}
			try {
				 value = URLDecoder.decode(keyValueArray[1], HttpEncoder.DEFAULT_CHARSET);
				 value = trim(value);
			} catch (UnsupportedEncodingException e) {
				_invalidMessageBodyFormat = true;
				break;				
			}
			_bodyParameters.put(keyValueArray[0], value);
		}
	}
	
	/**
	 * 
	 * @return true if parameters is message body were parsed correctly 
	 */
	public boolean isValidMessageBodyFormat(){
		return !_invalidMessageBodyFormat;
	}
	
    /**
     * retrieves a key from the body. if response message
     * @param key - the key to search.
     * @return a string representation of the body value. null if non-existent.
     */
    public String getMessageParameter(String key){
    	if (_invalidMessageBodyFormat || _bodyParameters.get(key) == null)
    		return null;
    	return new String(_bodyParameters.get(key));
    }
    
    
    public void setMessageBody(String toSet){
    	_messageBody = new String(toSet);
    }
    
    /**
     * Removes "$" and new lines and spaces from end of a string.
     * @param str
     * @return String
     *  Trimmed string.
     */
    public static String trim(String str) {
    	String ans = str;
    	
		if (ans.endsWith("$")) {
			ans = ans.substring(0, ans.length() - "$".length());
		}

		// Remove extra new line characters.
		while((ans.endsWith("\n") || ans.endsWith("\r"))) {
			ans = ans.substring(0, ans.length() - "\n".length());
		}
		
		return ans;
    }
}
