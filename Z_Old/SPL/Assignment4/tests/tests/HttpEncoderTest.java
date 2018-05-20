/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tokenizer_http.HttpMessage;
import encoder.HttpEncoder;

/**
 * @author yotam
 *
 */
public class HttpEncoderTest {

	HttpEncoder encoder;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		encoder = new HttpEncoder();
	}


	/**
	 * Test method for {@link encoder.HttpEncoder#toObject(java.lang.String)}.
	 */
	@Test
	public final void testToObject() {
		String stringMessage = "POST /login.jps HTTP/1.1\r\n" +
								"Cookie: yay!1\r\n" +
								"User: me me me\r\n" + 
								"\r\n" + 
								"UserName=AldoRaine&Phone=495558298";
		
		HttpMessage httpMessageObject = (HttpMessage) encoder.toObject(stringMessage);
		
		if (!"UserName=AldoRaine&Phone=495558298".equals(httpMessageObject.getMessageBody()))
			fail("Wrong message body: " + httpMessageObject.getMessageBody());
		
		if (!"/login.jps".equals(httpMessageObject.getRequestURI()))
			fail("Wrong http URI: " + httpMessageObject.getRequestURI());
		
		if (!"me me me".equals(httpMessageObject.getHeader("User")))
			fail("Wrong http User Header: " + httpMessageObject.getHeader("User"));
		if (!"yay!1".equals(httpMessageObject.getHeader("Cookie")))
			fail("Wrong http Cookie Header: " + httpMessageObject.getHeader("Cookie"));
		if (httpMessageObject.getHeader("Nothing") != null)
			fail("Wrong http \"Nothing\" Header - supposed to be null: " + httpMessageObject.getHeader("Nothing"));
		
		assertTrue(true);
	}

	/**
	 * Test method for {@link encoder.HttpEncoder#toObject(java.lang.String)}.
	 */
	@Test
	public final void testToObjectNoValues() {
		String stringMessage = "POST /login.jps HTTP/1.1\r\n" +
								"Cookie: yay!1\r\n" +
								"User: \r\n\r\n";
		
		HttpMessage httpMessageObject = (HttpMessage) encoder.toObject(stringMessage);
		
		if (httpMessageObject.getMessageBody() == "")
			fail("Wrong message body: " + httpMessageObject.getMessageBody());
		
		
		if (!"".equals(httpMessageObject.getHeader("User")))
			fail("Wrong http User Header: " + httpMessageObject.getHeader("User"));
		
		assertTrue(true);
	}
	
}
