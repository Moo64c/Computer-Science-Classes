/**
 * Interface describing an extended Stack, with random access through "peeking".
 */
public interface PeekableStack extends Stack {

	/** 
	 * Return the number of items in the stack.
	 * @return the number of items in the stack.
	 */
	int size();

	/** 
	 * Return the i-th item from the top of the stack, where the top
	 *  is the 0-th item.
	 * Precondition: i is strictly less than the stack's size AND i >= 0.
	 * @return the i-th stack item.
 	 * @throws EmptyStackException if trying to peek at an element which is not in the stack.
	 */
	Object peek(int i);

	/**
	 * Returns a newline(\n)-delimited (i.e. between adjacent elements)
	 *  String of the stack's contents, in order from top downwards.  
	 *  The String for each element is the result of that element's
	 *  toString() method. If the stack is empty, return the empty string.
	 * @return String representation of the stack contents
	 */
	String stackContents();

	/**
	 * Empty the stack of all its contents.
	 */
	void clear();
  
}
