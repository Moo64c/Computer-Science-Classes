package WhatsApp;
import java.io.IOException;

import protocol_whatsapp.WhatsAppProtocolFactory;
import threadPerClient.MultipleClientProtocolServer;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpTokenizerFactory;
import encoder.HttpEncoderFactory;


public class Main {

	public static void main(String[] args) throws IOException
	{
		// Get port
		int port = Integer.decode(args[0]).intValue();

		MultipleClientProtocolServer<HttpMessage> server = null;
		server = new MultipleClientProtocolServer<HttpMessage>(port, 
				new WhatsAppProtocolFactory(),
				new HttpTokenizerFactory(new HttpEncoderFactory())
				);
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		}
		catch (InterruptedException e)
		{
			System.out.println("Server stopped");
		}
	}
}
