/**
 * 
 */
package encoder;

import tokenizer_http.HttpMessage;

/**
 * @author yotam
 *
 */
public class HttpEncoderFactory implements EncoderFactory<HttpMessage> {

	public Encoder<HttpMessage> create(){
		return new HttpEncoder();
	}

}
