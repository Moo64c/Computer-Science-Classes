package protocol_whatsapp;

import WhatsApp.WhatsAppServer;
import protocol.AsyncServerProtocol;
import protocol.ServerProtocolFactory;
import tokenizer_http.HttpMessage;


public class WhatsAppProtocolFactory implements ServerProtocolFactory<HttpMessage> {
    WhatsAppServer server = new WhatsAppServer();
    
	public AsyncServerProtocol<HttpMessage> create() {
		return new WhatsAppProtocol(server);
	}
}
