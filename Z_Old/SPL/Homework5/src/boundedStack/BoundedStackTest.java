package boundedStack;

import static org.junit.Assert.*;
 
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
 
public class BoundedStackTest {
 
    private BoundedStack<Integer> stack;
    
    @Before
    public void setUp() throws Exception {
        this.stack = new BoundedStackImpl<Integer>();
    }
 
    @After
    public void tearDown() throws Exception {
    }
 
    @Test
    public void testCount()
    {
        final int cnt = 23;
        Integer i = 17;
        for (int idx=0; idx < cnt; idx++)
        {
            this.stack.push(i);
        }
        assertEquals("Wrong number of elements",cnt,this.stack.count());
    }
    
    @Test
    public void testPush() throws Exception {
        Integer i1 = 1;
        Integer i2 = 2;
        Integer i3 = 3;
        stack.push(i1);
        stack.push(i2);
        stack.push(i3);
        assertTrue(stack.count()==3);
        assertEquals("wrong object is pushed into the stack", i1, this.stack.itemAt(0));
        assertEquals("wrong object is pushed into the stack", i2, this.stack.itemAt(1));
        assertEquals("wrong object is pushed into the stack", i3, this.stack.itemAt(2));
    }
 
    @Test
    public void testRemove() throws Exception {
        Integer i1 = 1;
        stack.push(i1);
        stack.remove();
        assertTrue(this.stack.isEmpty());
    }
 
    /**
     * Test method for {@link spl.util.Stack#pop()}. Negative test - throw an
     * exception.
     * @throws Exception 
     */
 
    @Test (expected = Exception.class)
    public void testRemoveException() throws Exception {
        stack.remove();
    }
    
    @Test
    public void testIsEmpty() {
        Integer i1 = 1;
        assertEquals(true, stack.isEmpty());
        stack.push(i1);
        assertEquals(false, stack.isEmpty());
    }
 
    /**
     * Test the construction/set-up of the Stack
     */
    @Test 
    public void testStackConstructor() {
        assertNotNull("new Stack is not null", this.stack);
        assertTrue("new Stack is not empty", this.stack.isEmpty());
    }
    
    /**
     * Push a null
     */
    @Test public void testPushNull() {
        this.stack.push(null);
        assertTrue("Should still be empty, after push(null).", stack.isEmpty());
    }
    
    /**
     * Test {@link spl.util.Stack} for Last-In-First-Out. Will push three
     * objects and then pop them and will make sure they are ordered from last
     * to first.
     * @throws Exception 
     */
    @Test 
    public void testStackLIFO() throws Exception {
        Integer i1 = 1;
        Integer i2 = 2;
        Integer i3 = 3;
        Integer p;
 
        assertTrue("Stack s is not empty", this.stack.isEmpty());
        stack.push(i1);
        stack.push(i2);
        stack.push(i3);
        
        p = stack.top();
        assertEquals("wrong object is popped out of the stack", i3, p);
        stack.remove();
        assertEquals("wrong number of elements in stack", stack.count(),2);
        p = stack.top();
        assertEquals("wrong object is popped out of the stack", i2, p);
        stack.remove();
        assertEquals("wrong number of elements in stack", stack.count(),1);
        p = stack.top();
        assertEquals("wrong object is popped out of the stack", i1, p);
        stack.remove();
        assertTrue("stack is not empty after last pop()", stack.isEmpty());
    }
}