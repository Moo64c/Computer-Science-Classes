package tokenizer_http;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import java.io.IOException;

import encoder.Encoder;
import encoder.HttpEncoder;
import tokenizer.Tokenizer;


public class HttpTokenizer implements Tokenizer<HttpMessage> {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public final static String HTTP_VERSION = "HTTP/1.1";

	private final char delimiter;
	private InputStreamReader inputStreamReader;
	private boolean closed;
	Encoder<HttpMessage> encoder;

	public HttpTokenizer(char _delimiter, Encoder<HttpMessage> _encoder) {
		
		encoder = _encoder;
		delimiter = _delimiter;
		closed = false;
		inputStreamReader = null;
	}
	/**
	 * Whether reading to StringBuffer failed.
	 */
	@Override
	public boolean isAlive() {
		return !this.closed;
	}

	/**
    * Adding a bufferedReader from which the tokenizer reads the input.
    */
	@Override
	public void addInputStream(InputStreamReader _inputStreamReader) {
		inputStreamReader = _inputStreamReader;
	}
	/**
	 * Analyze given input stream and returns a string.
	 */
	public HttpMessage nextMessage() {
		if (!isAlive()) {
			try {
				throw new IOException("Tokenizer is closed");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		int ch = 0;
		StringBuilder sb = new StringBuilder();
		try {
			while (true) {
				ch = inputStreamReader.read();
				if (ch == -1) {
					Thread.sleep(10);
					continue;
				}
				
				if ((char) ch == delimiter) {
					break;
				}
				sb.append((char) ch);
			}
		} catch (IOException | InterruptedException e) {
			logger.severe("Exception. " + e);
			closed = true;
			return null;
		}
		
		String decodedString = "";
		try {
			decodedString = URLDecoder.decode(sb.toString(), HttpEncoder.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (decodedString.equals("")) {
			return null;
		}
		
		return encoder.toObject(decodedString);
	}
	
}