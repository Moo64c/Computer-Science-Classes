package protocol_http;

import java.util.Map;
import java.util.TreeMap;

import protocol.ServerProtocol;
import tokenizer_http.HttpMessage;

public class HttpProtocol implements ServerProtocol<HttpMessage> {
    public final static String END_CONNECTION_URI = "/EndConnection/";
    
    public HttpProtocol() {
    	
    }
    
	@Override
	public HttpMessage processMessage(HttpMessage msg) {
		HttpMessage response;
		Map<String, String> headers = new TreeMap<String,String>();
		int status = 200;
		
		if (!isValid(msg)) {
			// Illegal request.
			status = 403;
		}
		
		response = new HttpMessage(headers, "", status);
		return response;
	}
	
    @Override
	public boolean isEnd(HttpMessage msg) {
    	if (msg.getRequestURI().equals(END_CONNECTION_URI)) {
    		return true;
    	}
    	
    	return false; 
	}
    
	/**
	 * Checks if message is valid
	 * @return true if message is valid.
	 */
	public boolean isValid(HttpMessage msg){
		if (msg == null) {
			return false;
		}
		
		if (msg.getRequestURI().equals("")) {
			return false;
		}
		
		if (msg.getRequestType() == null) {
			return false;
		}
		
		return true; 
	}
}
