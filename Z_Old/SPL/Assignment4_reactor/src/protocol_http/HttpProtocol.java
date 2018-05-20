package protocol_http;

import java.util.Map;
import java.util.TreeMap;

import protocol.AsyncServerProtocol;
import tokenizer_http.HttpMessage;

public class HttpProtocol implements AsyncServerProtocol<HttpMessage> {
    public final static String END_CONNECTION_URI = "/EndConnection/";

	private boolean _shouldClose = false;
	@SuppressWarnings("unused")
	private boolean _connectionTerminated = false;
	
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
	

	/**
	 * Is the protocol in a closing state?.
	 * When a protocol is in a closing state, it's handler should write out all pending data, 
	 * and close the connection.
	 * @return true if the protocol is in closing state.
	 */
	@Override
	public boolean shouldClose() {
		return this._shouldClose;
	}

	/**
	 * Indicate to the protocol that the client disconnected.
	 */
	@Override
	public void connectionTerminated() {
		this._connectionTerminated = true;
	}
}
