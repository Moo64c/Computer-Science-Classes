package threadPerClient;

import java.io.IOException;
import java.net.ServerSocket;
import protocol.ServerProtocolFactory;
import tokenizer.TokenizerFactory;

public class MultipleClientProtocolServer<T> implements Runnable {
	private ServerSocket serverSocket;
	private int listenPort;
	private ServerProtocolFactory<T> _protocolFactory;
	private TokenizerFactory<T> _tokenizerFactory;


	public MultipleClientProtocolServer(int port, ServerProtocolFactory<T> protocolFactory, TokenizerFactory<T> tokenizerFactory)
	{
		serverSocket = null;
		listenPort = port;
		_protocolFactory = protocolFactory;
		_tokenizerFactory = tokenizerFactory;
	}

	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening...");
		}
		catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (true)
		{
			try {
				ConnectionHandler<T> newConnection = new ConnectionHandler<T>(
						serverSocket.accept(), _protocolFactory.create(),_tokenizerFactory.create());
				new Thread(newConnection).start();
			}
			catch (IOException e)
			{
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
	}


	// Closes the connection
	public void close() throws IOException
	{
		serverSocket.close();
	}
}
