package protocol_http;

import protocol.AsyncServerProtocol;
import protocol.ServerProtocolFactory;
import tokenizer_http.HttpMessage;


public class HttpProtocolFactory implements ServerProtocolFactory<HttpMessage> {
    
	public AsyncServerProtocol<HttpMessage> create() {
		return new HttpProtocol();
	}
}
