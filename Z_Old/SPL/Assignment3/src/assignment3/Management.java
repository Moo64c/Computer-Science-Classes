package assignment3;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


/**
 * Main information holding object.
 */
public class Management {
    private Assets assets;
    private Warehouse warehouse;
    private Statistics statistics;
    private int numberOfRepairmen;
    protected LinkedBlockingQueue<RentalRequest> rentalRequestQueue;
    protected BlockingQueue<DamageReport> DamageReportQueue;
    private ExecutorService maintenanceExecutor;
    private boolean finished;
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private Map<String, ClerkDetails> 				clerkDetails;
    private Map<String, CustomerGroupDetails> 		customerGroupDetails;
    private Map<String, RepairMaterialInformation> 	repairMaterialInformation;
    private Map<String, RepairToolInformation> 		repairToolInformation;
    private Statistics stats;
    private Semaphore groupManagersAtWork;
    protected CyclicBarrier clerkShift;
    private AtomicInteger requestsNotHandled;
    protected AtomicInteger activeClerksLeft;
    
    public Management() {
    	assets = 	new Assets();
    	warehouse = new WarehouseImpl();
    	clerkDetails =  			new TreeMap<String, ClerkDetails>();
    	customerGroupDetails =		new TreeMap<String, CustomerGroupDetails>();
    	repairMaterialInformation = new TreeMap<String, RepairMaterialInformation>();
    	repairToolInformation = 	new TreeMap<String, RepairToolInformation>();
    	rentalRequestQueue = 		new LinkedBlockingQueue<RentalRequest>();
    	numberOfRepairmen = 0;
    	requestsNotHandled = new AtomicInteger(0);
    	stats = new Statistics();
    	finished = false;
    	activeClerksLeft = new AtomicInteger(0);
    }
    
    /**
     * Add a new asset.
     * @param _name
     *   Asset constructor parameter.
     * @param _type
     * 	 Asset constructor parameter.
     * @param _location
     *   Asset constructor parameter.
     * @param _content
     *   Asset constructor parameter.
     * @param _cost
     * 	 Asset constructor parameter.
     * @param _size
     *   Asset constructor parameter.
     */
    public void addAsset(String _name, String _type, Location _location,
		Map<String, AssetContent> _content, double _cost, int _size) {
    	
    	// Create asset and add to Asset list.
    	Asset newAsset = new Asset(_name, _type, _location, _content, _cost, _size);
    	assets.addAsset(newAsset);
    	
    	// Add the repair tool and material info to the relevant management map.
    	for (Map.Entry<String, AssetContent> item : _content.entrySet()) {
    		Map<String, RepairToolInformation> toolInfo = item.getValue().getRepairToolInformation();
    		Map<String, RepairMaterialInformation> materialInfo = item.getValue().getRepairMaterialInformation();
    		
    		for(Entry<String, RepairToolInformation> info : toolInfo.entrySet()) {
    			if (this.repairToolInformation.get(info.getKey()) != null) {
    				this.repairToolInformation.put(info.getKey(), info.getValue());
    			}
    		}

    		for(Entry<String, RepairMaterialInformation> info : materialInfo.entrySet()) {
    			if (this.repairMaterialInformation.get(info.getKey()) != null) {
    				this.repairMaterialInformation.put(info.getKey(), info.getValue());
    			}
    		}
    	}
    }
    
    /**
     * increment money gained.
     * @param amount
     */
    protected void gainMoneyToStats(double amount){
    	stats.gainMoney(amount);
    }
    
    /**
     * add a fulfilled request to stats.
     * @param toAdd
     */
    protected void addRentalRequestToStats(RentalRequest toAdd){
    	stats.addRentalRequest(toAdd);
    }
    
    /**
     * adds a tool and the number of times it was used to stats.
     * @param tool
     * @param amount
     */
    protected void addToolToStats(String tool, int amount){
    	stats.addTool(tool, amount);
    }
    
    /**
     * add a material and the number of times it was used to stats.
     * @param material
     * @param amount
     */
    protected void addMaterialToStats(String material, int amount){
    	stats.addMaterial(material, amount);
    }
    
    /**
     * Adds a clerk to the management.
     * @param _name
     *   Name of the clerk.
     * @param _location
     *   Clerk's location.
     */
    public void addClerk(String _name, Location _location) {
    	ClerkDetails details = new ClerkDetails(_name, _location);
    	clerkDetails.put(_name, details);
    	activeClerksLeft.incrementAndGet();
    }

    /**
     * Adds a new customer group to the collection.
     * @param _managerName
     *  Customer group manager.
     */
    public void addCustomerGroup(String _managerName) {
    	CustomerGroupDetails group = new CustomerGroupDetails(_managerName);
    	customerGroupDetails.put(_managerName, group);
    }
    
    
    /**
     * Adds a new customer group to the collection.
     * @param _group
     *  Customer group.
     */
    public void addCustomerGroup(CustomerGroupDetails _group) {
    	customerGroupDetails.put(_group.getManagerName(), _group);
    }

    /**
     * Adds a new repair tool to the warehouse.
     * @param _name
     *   Name of the repair tool.
     * @param amount
     *   Amount to add.
     */
    public void addItemRepairTool(String _name, int amount) {
    	RepairTool tool = new RepairTool(_name, amount);
    	warehouse.addTool(tool);
    }

    /**
     * Adds a new repair material to the warehouse.
     * @param _name
     *   Name of the repair tool.
     * @param amount
     *   Amount to add.
     */
    public void addItemRepairMaterial(String _name, int amount) {
    	RepairMaterial material = new RepairMaterial(_name, amount);
    	warehouse.addMaterial(material);
    }

    /**
     * Sets the number of repair men.
     * @param amount
     *  Amount to set. 
     */
    public void setNumberOfRepairmen(int amount) {
    	this.numberOfRepairmen = amount;
    	maintenanceExecutor = Executors.newFixedThreadPool(numberOfRepairmen);
    }

    /**
     * Print this as a string.
     */
    public String toString() {
    	StringBuilder str = new StringBuilder(); 
		str.append("Management:\n");
    	str.append("Repairmen:" + numberOfRepairmen + "\tRequests Handled:" + requestsNotHandled + "\n");
    	str.append(assets.toString() + "\n");
    	str.append(warehouse.toString() + "\n");
    	
    	for(Entry<String, ClerkDetails> item : clerkDetails.entrySet()) {
    		str.append( "\n" + item.getValue().toString());
    	}

    	for(Entry<String, CustomerGroupDetails> item : customerGroupDetails.entrySet()) {
    		str.append("\n" + item.getValue().toString());
    	}
    		
    	str.append(statistics.toString());
    	
    	
    	// Repair tool / material info should be printed with the assets.
    	return str.toString();
    }
    
    /**
     * Adds a new request to queue and notifies all objects that are waiting on management due to no-requests on queue.
     * @param addedRequest
     */
    public void addRequestToQueue(RentalRequest addedRequest){
    	try {
			rentalRequestQueue.put(addedRequest);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    /**
     * Adds a new request to queue and notifies all objects that are waiting on management due to no-requests on queue.
     * @param addedRequest
     */
    protected RentalRequest takeRequestFromQueue(long timeLeft){
		try {
			return rentalRequestQueue.poll(8000 - timeLeft, TimeUnit.MILLISECONDS);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * Adds a new report to queue and notifies all objects that are waiting on management due to no-reports on queue.
     * @param damageReport
     */
    protected synchronized void addDamageReport(DamageReport damageReport) {
		if (damageReport.getAsset().getStatus() == Asset.ASSET_STATUS.UNAVAILABLE && !finished()){
				maintenanceExecutor.execute(new RunnableMaintanceRequest(damageReport.getAsset(), this.warehouse, this));
			LOGGER.info("management - created damage report: " + "total damage: " + damageReport.getAsset().getDamagePercentage()  + damageReport.toString());
		}
		LOGGER.info("requests to be fulfilled: " + requestsNotHandled.get());
    }
	
	/**
	 * launches all the customer group managers.
	 * @return true if all the group customers were successfully launched.
	 */
	private boolean launchRunnableCustomerGroupManagers() {
		int counter = 0;
		groupManagersAtWork = new Semaphore(customerGroupDetails.size());
    	for(Entry<String, CustomerGroupDetails> item : customerGroupDetails.entrySet()) {
    		Thread groupManager = new Thread(new RunnableCustomerGroupManager(item.getValue(), this, groupManagersAtWork));
    		groupManager.start();
    		LOGGER.info("created group manager");
    		counter++;
    	} 
    	
    	return counter == customerGroupDetails.size();
	}
	
	/**
	 * launches all the clerks.
	 * @return true if all the clerks were successfully launched.
	 */
	private boolean launchRunnableClerks() {
		int counter = 0;
		clerkShift = new CyclicBarrier(clerkDetails.size());
    	for(Entry<String, ClerkDetails> item : clerkDetails.entrySet()) {
    		RunnableClerk newClerk = new RunnableClerk(item.getValue(), this, requestsNotHandled,
    				activeClerksLeft, assets, clerkShift);
    		Thread clerk = new Thread(newClerk);
    		clerk.start();
    		LOGGER.info("created clerk");
    		counter++;
    	} 
    	
    	return counter == clerkDetails.size();
	}
	
	
	/**
	 * 
	 * @param num - number of requests to be handled by management.
	 */
	public void setNumberOfRequestsNotHandled(int num){
		LOGGER.info("Total number of requests in this run: " + num);
		requestsNotHandled.set(num);
	}
	/**
	 * Sort the assets.
	 */
	public void sortAssets() {
		assets.forceSort();
	}

	/**
	 * check if all clerks finished working
	 * @return
	 */
	protected boolean finished() {
		return (groupManagersAtWork.availablePermits() == customerGroupDetails.size());
	}

	/**
	 * print the statistics.
	 */
	private void printStatistics() {
		LOGGER.info("printing stats..." + stats.toString());
		
	}

	/**
	 * makes a thread wait for all clerks to finish running.
	 */
	private void waitForGroupManagersToFinish() {
		try {
			groupManagersAtWork.acquire(customerGroupDetails.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		groupManagersAtWork.release(customerGroupDetails.size());
		maintenanceExecutor.shutdownNow();
	}

	protected boolean hasFinished() {
		return finished;
	}
	
	/**
	 * starts simulating management.
	 */
	public void startSimulation() {
		this.launchRunnableCustomerGroupManagers();
		this.launchRunnableClerks();
		this.waitForGroupManagersToFinish();
		this.printStatistics();

		
	}

}
