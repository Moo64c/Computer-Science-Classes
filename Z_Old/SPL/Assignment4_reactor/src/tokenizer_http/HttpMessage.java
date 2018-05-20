package tokenizer_http;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.TreeMap;

import tokenizer.HttpTokenizer;
import tokenizer.Message;



public class HttpMessage implements Message<String> {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static enum REQUEST_TYPE { GET, POST, INVALID };
    public static enum HTTP_MESSAGE_TYPE { REQUEST, RESPONSE };
    
    protected String _messageBody;
    protected Map<String, String> _headers;
	protected REQUEST_TYPE _requestType;
	protected String _requestURI;
	protected int _status;
    protected HTTP_MESSAGE_TYPE _type;
	
    /**
     * response constructor
     * @param httpVersion
     * @param headers
     * @param messageBody
     * @param status
     */
    public HttpMessage(Map<String, String> headers, String messageBody, int status) {
    	_headers = headers;
    	_messageBody = messageBody;
		_status = status;
		
		_type = HTTP_MESSAGE_TYPE.RESPONSE;
		_requestType = REQUEST_TYPE.INVALID;
		_requestURI = "";
    }
    
    /**
     * request constructor.
     * @param httpVersion
     * @param headers
     * @param messageBody
     * @param requestType
     * @param requestURI
     */
    public HttpMessage(Map<String, String> headers, String messageBody, REQUEST_TYPE requestType, String requestURI) {
    	_headers = headers;
    	_messageBody = messageBody;
		_requestType = requestType;
		_requestURI = requestURI;
		
		_type = HTTP_MESSAGE_TYPE.REQUEST;
		_status = 0;
    }
    
    /**
     * copy constructor
     * @param httpVersion
     * @param headers
     * @param messageBody
     * @param requestType
     * @param requestURI
     */
    public HttpMessage(HttpMessage other) {
    	_headers = new TreeMap<String, String>(other._headers);
		_requestType = other._requestType;
		_requestURI = new String(other._requestURI == null ? "" : other._requestURI);
		_status = other._status;
		_type = other._type;
		_messageBody = new String(other._messageBody == null ? "" : other._messageBody);
    }
    
    
    /**
     * retrieves a key from the headers.
     * @param key - the key to search.
     * @return a string representation of the header value
     */
    public String getHeader(String key){
    	if (_headers.get(key) == null) {
    		logger.warning("Header not found: " + key);
    		return null;
    	}
    	return new String(_headers.get(key));
    }

    public String getMessageBody() {
		return new String(_messageBody == null ? "" : _messageBody);
	}
	
	public String getRequestURI() {
		return new String(_requestURI == null ? "" : _requestURI);
	}
	
	public String toString(){
		return (_type == HTTP_MESSAGE_TYPE.REQUEST) ? toStringRequest() : toStringResponse(); 
	}
	
	private String toStringRequest() {
		StringBuilder ans = new StringBuilder();

		ans.append(HttpTokenizer.HTTP_VERSION + " " + _requestType + " " + _requestURI);
		
		for (Entry<String, String> item: _headers.entrySet()){
			ans.append("\r\n" + item.getKey() + ": " + item.getValue());
		}
		
		ans.append("\r\n\r\n" + _messageBody);
		
		return ans.toString();
	}
	
	@Override
	public boolean equals(Object other) { 
		return (other != null && this.toString().equals(other.toString()));
	}
    
	public int getStatus(){
		return _status;
	}
	
	public HTTP_MESSAGE_TYPE getType(){
		return _type;
	}

	public REQUEST_TYPE getRequestType() {
		return _requestType;
	}

	private String toStringResponse() {
		StringBuilder ans = new StringBuilder();
		ans.append(HttpTokenizer.HTTP_VERSION + " " + _status);
		
		for (Entry<String, String> item: _headers.entrySet()) {
			ans.append("\r\n" + item.getKey() + ": " + item.getValue());
		}
		
		ans.append("\r\n\r\n" + _messageBody);
		return ans.toString();
	}

	protected Map<String, String> getHeaderMap() {
		return _headers;
	}
}
