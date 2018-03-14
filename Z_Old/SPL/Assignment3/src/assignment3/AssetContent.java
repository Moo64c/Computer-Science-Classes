package assignment3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Holds info about items inside an asset.
 */
public class AssetContent {
    private String name;
    private double health;
    // Time to repair: (100 - health) * repairCostMultiplier.
    private double repairCostMultiplier;

    private Map<String, RepairMaterialInformation> repairMaterialInformation;
    private Map<String, RepairToolInformation> repairToolInformation;

    public AssetContent(String _name, double multiplier) {
    	
    	name = _name;
    	health = 100.0;
    	repairCostMultiplier = multiplier;
    	repairMaterialInformation = new TreeMap<String, RepairMaterialInformation>();
    	repairToolInformation = new TreeMap<String, RepairToolInformation>();
    }
    
    
    /**
     * Repair the item.
     */
	public void repair() {
		health = 100.0;
	}
    
    /**
     * Damage the item.
     * @param percentage
     *   Percentage of the damage caused.
     */
	public void damage(double percentage) {
		health -= percentage;
		if (health < 0.0) {
			health = 0.0;
		}
	}

	/**
	 * Adds a repair info.
	 * @param info
	 *  Info to add.
	 */
	public void addRepairToolInfo(RepairToolInformation info) {
		repairToolInformation.put(info.getName(), info);
	}

	/**
	 * Adds a repair info.
	 * @param info
	 *  Info to add.
	 */
	public void addRepairMaterialInfo(RepairMaterialInformation info) {
		repairMaterialInformation.put(info.getName(), info);
	}
	
	/**
	 * Getter function.
	 */
	public Map<String, RepairMaterialInformation> getRepairMaterialInformation() {
		return repairMaterialInformation;
	} 

	/**
	 * Getter function.
	 */
	public Map<String, RepairToolInformation> getRepairToolInformation() {
		return repairToolInformation;
	}
	
	/**
	 * Get the damaged caused to the item. 
	 */
	public double getDamagePercentage() {
		return 100.0 - health;
	}

	/**
     * Print this as a string.
     */
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("\tAssetContent:" + name + "\t" + "health:" + health);
    	str.append("\t" + "multiplier:" + repairCostMultiplier);
    	for(Entry<String, RepairToolInformation> item : repairToolInformation.entrySet()) {
    		str.append( "\n\t\t" + item.getValue().toString());
    	}
    	for(Entry<String, RepairMaterialInformation> item : repairMaterialInformation.entrySet()) {
    		str.append("\n\t\t" + item.getValue().toString());
    	}
    	
        return str.toString();
    }


	public double getRepairTime() {
		return repairCostMultiplier * getDamagePercentage();
	}
}
