package assignment3;

/**
 * Created by Amir on 08/12/14.
 */
public class RepairMaterialInformation {
    private String name;
    private int quantity;
    
    
    public RepairMaterialInformation(String _name, int amount) {
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
     * @return
     */
    public String toString() {
        return "RepairMaterialInformation Name:" + name + "\tQuantity:" + quantity;
    }

	public int getAmount() {
		return quantity;
	}
}
