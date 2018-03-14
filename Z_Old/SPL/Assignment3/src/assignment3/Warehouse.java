package assignment3;

/**
 * Manage repair tools and materials.
 */
public interface Warehouse {

    /**
     * Mark a tool as used by a repairman.
     * @param name
     *   Name of the tool.
     */
    public void acquireTool(RepairToolInformation info);

    /**
     * Mark a tool as available.
     * @param Info.
     *   Repair items info.
     */
    public void releaseTool(RepairToolInformation info);

    /**
     * Use a material to repair something.
     * @param Info.
     *   Repair items info.
     */
    public void consumeMaterial(RepairMaterialInformation info);

    /**
     * Print this as a string.
     * @return
     */
    public String toString();
    
    /**
     * Add tools.
     * @param tool
     *  Tool to add.
     */
	public void addTool(RepairTool tool);
	
	/**
	 * Add materials.
	 * @param material
	 *   Material to add.
	 */
	public void addMaterial(RepairMaterial material);
}
