package assignment3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.TreeMap;

public class WarehouseImpl implements Warehouse {
    protected Map<String, RepairTool> tools;
    protected Map<String, RepairMaterial> materials;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public WarehouseImpl() {
		this.materials = new TreeMap<String, RepairMaterial>();
		this.tools = new TreeMap<String, RepairTool>();
	}
	
	@Override
	public void acquireTool(RepairToolInformation info) {
		RepairTool tool = tools.get(info.getName()); 
		
		if (tool == null) {
			// No such tool.
			return;
		}
		
		tool.acquire(info.getAmount());
	}

	@Override
	public void releaseTool(RepairToolInformation info) {
		RepairTool tool = tools.get(info.getName()); 
		
		if (tool == null) {
			// No such tool.
			LOGGER.info("Failed releasing tools");
			return;
		}
		
		tool.release(info.getAmount());
	}

	@Override
	public void consumeMaterial(RepairMaterialInformation info) {
		RepairMaterial material = materials.get(info.getName());
		if (material == null) {
			// No such material.
			return;
		}
		
		material.consume(info.getAmount());
	}

	/**
	 * Get the current amount of tools with a specified name.
	 * @param name
	 *   Name of the tool.
	 * @return
	 *   Amount of the tool in the tools map; 0 if not found.
	 */
	public int toolAmount(String name) {
		RepairTool tool = this.tools.get(name);
		if (tool == null) {
			return 0;
		}
		return tool.amountAvailable();
	}

	/**
	 * Get the current amount of materials with a specified name.
	 * @param name
	 *   Name of the material.
	 * @return
	 *   Amount of the material in the materials map; 0 if not found.
	 */
	public int materialAmount(String name) {
		RepairMaterial material = this.materials.get(name);
		if (material == null) {
			return 0;
		}
		return material.getAmount();
	}
	
	/**
	 * Add a few materials and tools.
	 */
	public void addTestItems() {
		RepairMaterial material1 = new RepairMaterial("Nails", 100);
		RepairMaterial material2 = new RepairMaterial("Shotgun Shells", 30);
		RepairTool tool1 = new RepairTool("Hammer", 3);
		RepairTool tool2 = new RepairTool("Shotgun", 1);
		
		this.materials.put("Nails", material1);
		this.materials.put("Shotgun Shells", material2);
		this.tools.put("Hammer", tool1);
		this.tools.put("Shotgun", tool2);
	}
	
	/**
	 * Removes the test items from the maps.
	 */
	public void removeTestItems() {
		this.materials.clear();
		this.tools.clear();
	}

	@Override
	public void addTool(RepairTool tool) {
		tools.put(tool.getName(), tool);
	}

	@Override
	public void addMaterial(RepairMaterial material) {
		materials.put(material.getName(), material);		
	}


    /**
     * Print this as a string.
     */
    public String toString() {
    	StringBuilder str = new StringBuilder(); 
		str.append("Warehouse content:\nTools:");
    	for (Entry<String, RepairTool> item : tools.entrySet()) {
    		str.append("\n\t" + item.getValue().toString());
    	}
    	str.append("\nMaterials:");
    	for (Entry<String, RepairMaterial> item : materials.entrySet()) {
    		str.append("\n\t" + item.getValue().toString());
    	}
    	
        return str.toString();
    }
    
}
