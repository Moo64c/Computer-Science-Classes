package assignment3;

/**
 * Holds info about a repair material.
 */
public class RepairMaterial {
    String name;
    int amount;

	public RepairMaterial(String _name, int _amount) {
    	this.name = _name;
    	this.amount = _amount;
    }

	
	/**
	 * Getter function.
	 * @return
	 *  Amount of this material in the warehouse.
	 */
	public int getAmount() {
		return this.amount;
	}
	
	/**
	 * Use a specified amount of materials.
	 * Assumption: never asks more than what's available.
	 * @param _amount
	 *   Amount to consume.
	 */
	public synchronized void consume(int _amount) {
		this.amount -= _amount;
	}

    /**
     * Getter function.
     */
	public String getName() {
		return name;
	}

    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "Material Name:" + name + "\tAmount:" + amount;
    }

}
