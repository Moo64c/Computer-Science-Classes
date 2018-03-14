package assignment3;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Finally some output.
 */
public class Statistics {
    // How much did we gain?
    private double moneyGained;
    // The rental requests and their statuses.
    private Vector<RentalRequest> rentalRequests;
    // What materials were used and how many tools are required.
    private Map<String, Integer> repairMaterials;
    private Map<String, Integer> repairTools;
    
    public Statistics() {
    	moneyGained = 0;
    	rentalRequests = new Vector<RentalRequest>();
    	repairMaterials = new TreeMap<String, Integer>();
    	repairTools = new TreeMap<String, Integer>();
    }
    
    /**
     * Report money made.
     * @param amount
     *   Amount of money made.
     */
    public void gainMoney(double amount) {
    	moneyGained += amount;
    }
    
    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
    	StringBuilder string = new StringBuilder();
    	string.append("Statistics \tMoney Gained: ");
    	string.append(moneyGained + "\n");
    	string.append("\tRental Requests: \n" + getRentalRequests());
    	string.append("Tools Used: \n" + getToolsUsed());
    	string.append("Materials Used: \n" + getMaterialsUsed());
    	
    	return  string.toString();
	}
    
    
    /**
     * get rental requests fulfilled.
     * @return rental request info as string.
     */
    private String getRentalRequests(){
    	StringBuilder string = new StringBuilder();
    	for (int i = 0; i < rentalRequests.size(); i++)
    	{
    		string.append("\t\t"+ rentalRequests.get(i)+"\n");
    	}
    	return string.toString();
    }
    
    /**
     * amount of tools used.
     * @return
     */
    private String getToolsUsed(){
    	return getStringFromMap(repairTools);
    }
    
    /**
     * amount of materials used.
     * @return
     */
    private String getMaterialsUsed(){
    	return getStringFromMap(repairMaterials);
    }
    
    /**
     * Auxiliary function - retrieves statistics from map in a nice format.
     * @param map - the map to extract statistics from.
     * @return a string with the statistics.
     */
    private String getStringFromMap(Map<String,Integer> map){
    	StringBuilder string = new StringBuilder();
    	Iterator< Entry<String,Integer>> it = map.entrySet().iterator();
    	while (it.hasNext()){
    		Entry<String, Integer> nextEntry = it.next();
    		string.append("\t\t" + (String) nextEntry.getKey() + " : " + nextEntry.getValue().toString() + "\n");
    	}
    	return string.toString();
    }
    
    /**
     * Adds a rental request to statistics.
     * @param toAdd - the request to add
     */
    public synchronized void addRentalRequest(RentalRequest toAdd){
    	rentalRequests.add(toAdd);
    }
    
    /**
     * Adds a material used to statistics.
     * @param material
     * @param amount
     */
    public synchronized void addMaterial(String material, int amount){
    	addToMap(material, amount, repairMaterials);
    }
    
    /**
     * Adds a tool used to statistics.
     * @param tool
     * @param amount
     */
    public synchronized void addTool(String tool, int amount){
    	addToMap(tool, amount, repairTools);
    }
    
    /**
     * Auxiliary function - inserts or increments a value in a map.
     * @param key
     * @param Value
     * @param map
     */
    private void addToMap(String key, int value, Map<String, Integer> map){
    	if (map.containsKey(key)){
    		Integer newValue = new Integer(map.get(key).intValue() + value);
    		map.remove(key);
    		map.put(key, newValue);
    	}
    	else {
    		map.put(key, new Integer(value));
    	}
    }

}
