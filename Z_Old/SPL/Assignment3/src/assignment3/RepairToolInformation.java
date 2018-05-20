package assignment3;

/**
 * Created by amir on 08/12/14.
 */
public class RepairToolInformation {
    private String name;
    private int quantity;

    public RepairToolInformation(String _name, int amount) {
    	name = _name;
    	quantity = amount;
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
        return "RepairToolInformation Name:" + name + "\tQuantity:" + quantity;
    }

	public int getAmount() {
		return quantity;
	}

}
