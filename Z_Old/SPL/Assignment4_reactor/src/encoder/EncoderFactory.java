package encoder;

public interface EncoderFactory<T> {
	/**
	 * 
	 * @return a new encoder.
	 */
	Encoder<T> create();
}
