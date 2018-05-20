package boundedStack;


/**
 * Stack is an objects container. All objects are of type T. Contents is
 * ordered in Last-In-First-Out order. You can add an object T to the Stack and
 * take a contained Object T out.
 * 
 * @author Amir A
 * 
 */
interface BoundedStack<T> {
    /**
     * add the object at the top of the stack. (This is a command.)
     * 
     * @param obj
     *            any non null T object to be added to the stack
     
     */
    public void push(T obj);
    

    /**
     * remove the top object from the stack and returns it. 
     * 
     * @return the topmost object on the stack 
     * @throws Exception in case the stack is empty
     */
    public T pop() throws Exception;
    
    /**
     * @return True if the Stack is empty, or False if the Stack contains at
     *         least one {@link Object}. 
     */
    public boolean isEmpty();
    
    
    public void remove() {
    	
    }
    
    public T top() {
    	return 
    }
}