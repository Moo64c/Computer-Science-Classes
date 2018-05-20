/**
 * 
 */
package encoder;

import java.nio.charset.Charset;

/**
 * @author yotam
 *
 */
public interface Encoder<T> {
	
	/**
	 * convert the object to byte representation.
	 * @param obj
	 * @return byte array of representation.
	 */
	byte[] toBytes(T obj);
	
	/**
	 * convert bytes to object.
	 * @param bytes
	 * @return object
	 */
	T toObject(String stringRepresentation);
	
	/**
	 * 
	 * @return the encoding used to convert bytes to object.
	 */
	Charset getEncoding();
}
