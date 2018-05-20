package protocol_http;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;
import tokenizer_http.HttpMessage;


public class HttpProtocolFactory implements ServerProtocolFactory<HttpMessage> {
    
	public ServerProtocol<HttpMessage> create() {
		return new HttpProtocol();
	}
}
