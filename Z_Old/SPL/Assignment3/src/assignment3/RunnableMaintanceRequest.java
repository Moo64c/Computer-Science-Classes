package assignment3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.TreeMap;

public class RunnableMaintanceRequest implements Runnable {
	private Asset asset;
	private Warehouse warehouse;
	private Map<String, RepairToolInformation> toolInfo;
	private Management management;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public RunnableMaintanceRequest(Asset _asset, Warehouse _warehouse, Management _management) {
		asset = _asset;
		warehouse = _warehouse;
		management = _management;
	}
	
	
	@Override
	public void run() {
		Map<String, AssetContent> damaged = asset.getDamagedContent();
		if (damaged == null) {
			// Not fixing this.
			return;
		}
		RepairToolInformation foundItem;
		double timeToFix = 0.0;
		// Hold name and amount of tools.
		Map<String, RepairToolInformation> toolsUsed = new TreeMap<String, RepairToolInformation>();
		for (Entry<String, AssetContent> item : damaged.entrySet()) {
			// Acquire tools and materials needed.
			for (Entry<String, RepairMaterialInformation> material:  item.getValue().getRepairMaterialInformation().entrySet()) {
				LOGGER.info("Acquiring materials." + material.getValue());
				warehouse.consumeMaterial(material.getValue());
				LOGGER.info("Materials acquired." + material.getValue());
				
				management.addMaterialToStats(material.getValue().getName(), material.getValue().getAmount());
			}
			for (Entry<String, RepairToolInformation> tool:  item.getValue().getRepairToolInformation().entrySet()) {
				if ((foundItem = toolsUsed.get(tool.getKey())) != null) {
					// Already has this item, see if amount is fine.
					if (foundItem.getAmount() < tool.getValue().getAmount()) {
						toolsUsed.put(tool.getKey(), tool.getValue());
					}
				}
				else {
					LOGGER.info("Acquiring tools." + tool.getValue());
					toolsUsed.put(tool.getKey(), tool.getValue());
					warehouse.acquireTool(tool.getValue());
				}
				
				LOGGER.info("Tools acquired." + tool.getValue());
			}

			timeToFix += item.getValue().getRepairTime();
		}

		// Fixing things take time.
		try {
			LOGGER.info("Fixing an asset " + asset + " takes " + (timeToFix / 1000)  + " seconds");
			Thread.sleep((long)  (timeToFix));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Release all the tools that were acquired.
		for(Entry<String, RepairToolInformation> item:  toolsUsed.entrySet()) {
			LOGGER.info("Releasing " + item.getValue() + " " + item.getKey());
			warehouse.releaseTool(item.getValue());
			LOGGER.info("Released " + item.getValue() + " " + item.getKey());
			
			management.addToolToStats(item.getValue().getName(), item.getValue().getAmount());
		}
		
		// Return asset content to full health.
		LOGGER.info("Returning asset to full health");
		asset.heal();
	}


	@Override
	public String toString() {
		return "RunnableMaintanceRequest [asset=" + asset + ", warehouse="
				+ warehouse + ", toolInfo=" + toolInfo + "]";
	}

}
