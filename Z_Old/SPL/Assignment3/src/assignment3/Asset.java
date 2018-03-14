package assignment3;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Holds information about an asset.
 */
public class Asset {
    // Status options.
	public static enum ASSET_STATUS{AVAILABLE, BOOKED, OCCUPIED, UNAVAILABLE};

    private String name;
    // Types are defined in the input list.
    private String type;
    private Location location;
    
    // Items in the asset.
    private Map<String, AssetContent> content;
    // Status can only be one of the 4 defined above statuses.
    private ASSET_STATUS status;
    private double costPerNight;
    // Number of people who can live in the asset.
    protected int size;
    
    public Asset(String _name, String _type, Location _location,
    		Map<String, AssetContent> _content, double _cost, int _size) {
    	name = _name;
    	type = _type;
    	location = _location;
    	content = _content;
    	costPerNight = _cost; 
    	size = _size;
    	
    	// Assume it's a new place, set as available.
    	status = ASSET_STATUS.AVAILABLE;
    }
    
    /**
     * Damage every AssetContent in the Asset.
     * @see http://www.cs.bgu.ac.il/~spl151/course-forum/viewtopic.php?f=6&t=173#p448
     * @param percentage
     *  Damage to cause (in percentages)
     */
    public synchronized void damageContent(double percentage) {
    	for(Map.Entry<String, AssetContent> item : content.entrySet()) {
    		item.getValue().damage(percentage);
    	}
    	
    	if (getDamagePercentage() > 35.0) {
    		// The asset is too damaged to be used.
    		status = ASSET_STATUS.UNAVAILABLE;
    	}
    	else {
    		status = ASSET_STATUS.AVAILABLE;
    	}
    }
    
    public double getCost(){
    	return costPerNight;
    }
	
	/**
	 * Get the damage caused to the asset (all AssetContent have the same amount).
	 */
	public double getDamagePercentage() {
		if (content.isEmpty()) {
			return 0.0;
		}
		
		// Get an asset content; they should all have the same damage percentage.
		Map.Entry<String, AssetContent> entry = content.entrySet().iterator().next();
		return entry.getValue().getDamagePercentage();
	}
	
    /**
     * Print this as a string.
     */
    public String toString() {
    	String str = "\nAsset Name: " + name + "\tType:" + type + "\t" + location.toString();
    	str += "\nStatus:" + status + "\tCost:" + costPerNight + "\tSize:" + size;
    	
        return str;
    }
    
    /**
     * Print this as a string.
     */
    public String toStringFull() {
    	StringBuilder str = new StringBuilder(); 
		str.append("\nAsset Name: " + name + "\tType:" + type + "\t" + location.toString());
		str.append("\nStatus:" + status + "\tCost:" + costPerNight + "\tSize:" + size);
    	for(Entry<String, AssetContent> item : content.entrySet()) {
    		str.append("\n" + item.getValue().toString());
    	}
    	
        return str.toString();
    }
    
    /**
     * Gets all the asset content in the asset that has 35% damage or more.
     * @return damaged asset content.
     */
	public Map<String, AssetContent> getDamagedContent() {
		if (getStatus() == ASSET_STATUS.UNAVAILABLE) {
			// Asset is damaged.
			return content;
		}
		return null;
	}
	
	/**
	 * Fix all the items in the asset.
	 */
	public synchronized void heal() {
		for(Entry<String, AssetContent> item : getDamagedContent().entrySet()) {
			item.getValue().repair();
		}
		status = ASSET_STATUS.AVAILABLE;
		notifyAll();
	}
	
	/**
	 * Sets the status for the asset.
	 * @param status
	 * @return 
	 *  Whether the status was successfully changed.
	 */
	public synchronized boolean setStatusBooked() {
		if (status == ASSET_STATUS.BOOKED) {
			return false;
		}
		status = ASSET_STATUS.BOOKED;
		return true;
	}

    /**************************************************************/
    // Getter functions.
    /**************************************************************/
	
	
	
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}

	public boolean available() {
		return status == ASSET_STATUS.AVAILABLE;
	}

	public String getType() {
		return type;
	}

	public ASSET_STATUS getStatus() {
		return status;
	}

	public Location getLocation() {
		return location;
	}

	public void setStatusOccuipied() {
		status = ASSET_STATUS.OCCUPIED;
	}
}
