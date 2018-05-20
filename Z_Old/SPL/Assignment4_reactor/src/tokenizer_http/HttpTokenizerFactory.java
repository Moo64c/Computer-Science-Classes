package tokenizer_http;

import java.nio.charset.Charset;

import tokenizer.HttpTokenizer;
import tokenizer.MessageTokenizer;
import tokenizer.TokenizerFactory;

public class HttpTokenizerFactory implements TokenizerFactory<HttpMessage> {
	public static final String DEFAULT_DELIMITER = "$";
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public MessageTokenizer<HttpMessage> create(String delimiter, Charset charset) {
		return new HttpTokenizer(delimiter, charset);
	}

	public MessageTokenizer<HttpMessage> create() {
		return create(DEFAULT_DELIMITER, DEFAULT_CHARSET);
	}

}
