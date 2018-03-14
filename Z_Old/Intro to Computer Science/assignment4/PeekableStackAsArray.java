/**
 * @file PeekableStackAsArray
 * Defines a peek-able stack.
 *
 * @author Amir Arbel
 */
import java.util.EmptyStackException;

public class PeekableStackAsArray extends StackAsArray implements PeekableStack  {

    /**
	 * Return the number of items in the stack.
	 * @return the number of items in the stack.
	 */
	public int size() {
        return this.size;
    }

	/** 
	 * Return the i-th item from the top of the stack, where the top
	 *  is the 0-th item.
	 * Precondition: i is strictly less than the stack's size AND i >= 0.
	 * @return the i-th stack item.
 	 * @throws EmptyStackException if trying to peek at an element which is not in the stack.
	 */
    public Object peek(int i) throws EmptyStackException {
        // If there are no elements, an exception is thrown
        if (size == 0) {
            throw new EmptyStackException();
        }

        return this.elements[size - i - 1];
    }

	/**
	 * Returns a newline(\n)-delimited (i.e. between adjacent elements)
	 *  String of the stack's contents, in order from top downwards.  
	 *  The String for each element is the result of that element's
	 *  toString() method. If the stack is empty, return the empty string.
	 * @return
     *  String representation of the stack contents
	 */
    public String stackContents() {
        String contents = "";
        for (int i = 0; i < size; i++) {
            contents += peek(i) + "\n";
        }
        return contents;
    }


	/**
	 * Empty the stack of all its contents.
	 */
	public void clear() {
        this.size = 0;
        this.elements = null;
    }
}
