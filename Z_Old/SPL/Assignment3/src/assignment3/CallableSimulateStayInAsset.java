package assignment3;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class CallableSimulateStayInAsset implements Callable<Double>{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
	public String toString() {
		return "CallableSimulateStayInAsset [customer=" + customer
				+ ", durationOfStay=" + durationOfStay + "]";
	}

	private Customer customer;
	private int durationOfStay;


	private final int REAL_TO_SIM_TIME = 24000; 

	
	
	public CallableSimulateStayInAsset(Customer _customer, int _durationOfStay) {
		customer = _customer;
		durationOfStay = _durationOfStay;
	}

	@Override
	public Double call() throws Exception {
		LOGGER.info("Simulating " + customer.getName() + " stay in asset: " + ((REAL_TO_SIM_TIME * durationOfStay) / 1000) + " seconds") ;
		Thread.sleep(REAL_TO_SIM_TIME * durationOfStay);
		LOGGER.info(customer.toString() + "finished stay");
		return new Double(customer.calculateDamage());
	}

}
