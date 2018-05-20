package assignment3;

import java.util.logging.Logger;

/**
 * Holds info about a repair tool.
 */
public class RepairTool {
    String name;
    int amount;
    int inUse;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public RepairTool(String _name, int _amount) {
        name = _name;
        amount = _amount;
        inUse = 0;
    }


    /**
     * How many tools are available.
     */
    public int amountAvailable() {
    	return this.amount - this.inUse;
    }
    
    
    /**
     * Whether there's an available tool to acquire.
     */
    public synchronized boolean available(int amount) {
    	return (this.amountAvailable() - amount >= 0);
    }
    
    
    /**
     * Mark a tool as used.
     */
	public synchronized void acquire(int _amount) {
		while (!this.available(_amount)) {
			// None available, wait to be released.
			try {
				LOGGER.info("waiting for tools to become available" + this);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.inUse += _amount;
		notifyAll();
	}

    /**
     * Mark a tool as available.
     */
	public synchronized void release(int _amount) {

		this.inUse -= _amount;
		notifyAll();
	}

    /**
     * Getter function.
     */
	public String getName() {
		return name;
	}
	
    /**
     * Print this as a string.
     */
    public String toString() {
        return "Tool Name:" + name + "\tAmount:" + amount + "\tIn Use:" + inUse;
    }
}
