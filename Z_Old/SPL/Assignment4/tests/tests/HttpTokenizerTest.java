package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import encoder.HttpEncoder;
import encoder.HttpEncoderFactory;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpTokenizer;
import tokenizer_http.HttpTokenizerFactory;

public class HttpTokenizerTest {

	HttpTokenizer tokenizer;
	HttpTokenizerFactory tokenizerFactory;
	HttpEncoder encoder;
	InputStreamReader reader;
	PipedOutputStream out;
	PipedInputStream in;
	
	@Before
	public void setUp() throws Exception {
		tokenizerFactory = new HttpTokenizerFactory(new HttpEncoderFactory());
		tokenizer = (HttpTokenizer) tokenizerFactory.create();
		encoder = (HttpEncoder) (new HttpEncoderFactory()).create();
		out = new PipedOutputStream();
		in = new PipedInputStream(out);
		reader = new InputStreamReader(in, Charset.forName("UTF-8"));
		tokenizer.addInputStream(reader);
		
	}

	@Test
	public final void testNextMessage() throws IOException {
		String messageStart = "POST /login.jps HTTP/1.1\r\n\r\n";
		String messageEnd = "UserName=AldoRaine&Phone=495558298";
		String delimiter = "$";
		out.write(messageStart.getBytes());
		assertTrue(tokenizer.nextMessage() == null);
		
		out.write(messageEnd.getBytes());
		out.write(delimiter.getBytes());
		HttpMessage msg = (HttpMessage) tokenizer.nextMessage();
		
		if (!msg.equals((HttpMessage) encoder.toObject(messageStart + messageEnd))){
			fail(msg.toString() + "\r\n not equal to expected: \r\n" + encoder.toObject(messageStart + messageEnd).toString());
		}
		
	}

	@Test
	public final void testIsAlive() throws IOException {
		boolean shouldBeOpenBefore = tokenizer.isAlive();
		tokenizer.nextMessage();
		boolean shouldBeOpenAfter = tokenizer.isAlive();
		reader.close();
		tokenizer.nextMessage();
		boolean shouldBeClosed = !tokenizer.isAlive();
		assertTrue(shouldBeOpenBefore && shouldBeOpenAfter && shouldBeClosed);
	}

}
