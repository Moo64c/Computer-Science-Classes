package assignment3;

import java.util.Iterator;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;




public class RunnableCustomerGroupManager implements Runnable{
	private CustomerGroupDetails details;
	private Management management;
	private double accumulatedDamage;
	private Semaphore groupManagersAtWork;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public RunnableCustomerGroupManager(CustomerGroupDetails _details, Management _management, Semaphore _groupManagersSem) {
		details = _details;
		management = _management;
		cleanDamageReport();
		groupManagersAtWork = _groupManagersSem;
		try {
			groupManagersAtWork.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void run() {
		while(details.hasRequests()) {
			cleanDamageReport();
			LOGGER.info(details.getManagerName() + " - getting new request");
			RentalRequest nextRequest = details.getNextRequest();
			
			if (nextRequest == null) {
				LOGGER.info(details.getManagerName() + " - next request is null");
				continue;
			}
			
			LOGGER.info(details.getManagerName() + " - adding request to queue: " + nextRequest.toString());
			management.addRequestToQueue(nextRequest);
			synchronized(nextRequest){
				while (nextRequest.getStatus() != RentalRequest.RENTAL_STATUS.FULFILLED){
					try {
						LOGGER.info(details.getManagerName() + " - waiting for request fullfilement " + nextRequest.toString());
						nextRequest.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// request in progress:
				}
				simulateStay(nextRequest);
			}
		}
		LOGGER.info(details.getManagerName() + " - finished cleanly.");
		groupManagersAtWork.release();
	}

	/**
	 * simulates the stay of customers in asset.
	 * @param nextRequest the request to simulate stay of.
	 */
	private void simulateStay(RentalRequest nextRequest) {
		LOGGER.info(details.getManagerName() + " - simulating stay");
		nextRequest.incrementStatus();
		Iterator<Customer> customerIterator = details.getCustomers();
		ExecutorService executeStay = Executors.newCachedThreadPool();
		CompletionService<Double> cs = new ExecutorCompletionService<Double>(executeStay);
		int numOfCustomers = 0;
		while (customerIterator.hasNext()){
			Customer nextCustomer = customerIterator.next();
			LOGGER.info(details.getManagerName() + " - created customer: " + nextCustomer);
			cs.submit(nextCustomer.createStayTask(nextRequest));
			numOfCustomers++;
		}
		for (int i = 0; i < numOfCustomers; i++){
			try {
				calculateDamageReport(cs.take().get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executeStay.shutdownNow();
		LOGGER.info(details.getManagerName() + " - generating damaged report for damage: " + accumulatedDamage);
		management.addDamageReport(new DamageReport(nextRequest.getAsset(), accumulatedDamage));
		nextRequest.incrementStatus(); 
		management.addRentalRequestToStats(nextRequest);
		management.gainMoneyToStats(getMoneyGainedFromRequest(nextRequest, numOfCustomers));
		// request completed.
		cleanDamageReport();
	}


	/**
	 * adds given damage percentage to accumulated damage.
	 * @param damage - the damage to add.
	 */
	private void calculateDamageReport(Double damage) {
		accumulatedDamage += damage.doubleValue();
	}
	
	/**
	 * calculates how much money was paid per request, by the formula: numOfCustomer*costpernight*duration.
	 * @param req
	 * @param numOfCustomers
	 * @return
	 */
	private double getMoneyGainedFromRequest(RentalRequest req, int numOfCustomers){
		double ans = numOfCustomers;
		ans *= req.getDurationOfStay();
		ans *= req.getAsset().getCost();
		return ans;
	}

	
	/**
	 * clears the accumulated damage to asset.
	 */
	private void cleanDamageReport() {
		accumulatedDamage = 0;
	}


	@Override
	public String toString() {
		return "RunnableCustomerGroupManager [details=" + details
				+ ", accumulatedDamage="
				+ accumulatedDamage + "]";
	}
}
