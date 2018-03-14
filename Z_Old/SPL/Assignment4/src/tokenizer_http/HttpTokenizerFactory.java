package tokenizer_http;

import encoder.Encoder;
import encoder.EncoderFactory;
import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

public class HttpTokenizerFactory implements TokenizerFactory<HttpMessage> {
	public static final char DEFAULT_DELIMITER = '$';
	protected EncoderFactory<HttpMessage> _encoderFactory;

	public HttpTokenizerFactory(EncoderFactory<HttpMessage> encoderFactory){
		_encoderFactory = encoderFactory;
	}

	public Tokenizer<HttpMessage> create(char delimiter, Encoder<HttpMessage> encoder) {
		return new HttpTokenizer(delimiter, encoder);
	}

	public Tokenizer<HttpMessage> create() {
		return create(DEFAULT_DELIMITER, _encoderFactory.create());
	}

}
