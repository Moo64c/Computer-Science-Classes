package assignment3;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Holds info about a customer group.
 * Used in RunnableCustomerGroupManager.
 */
public class CustomerGroupDetails {
	// RequestId -> request.
    private LinkedList<RentalRequest> requests;
    // CustomerName -> customer.
    private Vector<Customer> customers;
    private String managerName;
    
    public CustomerGroupDetails(String _managerName) {
    	managerName = _managerName;
    	requests  = new LinkedList<RentalRequest>();
    	customers = new Vector<Customer>();
    }
    
    /**
     * Adds a customer to the customer group.
     * @param customer
     *   Customer to add.
     */
    public void addCustomer(Customer customer) {
    	customers.add(customer);
    }
    
    /**
     * 
     * @return an iterator with every customer.
     */
    public Iterator<Customer> getCustomers(){
    	return customers.iterator();
    }

    /**
     * Adds a rental request to the customer group.
     * @param request
     *   The rental request to add.
     */
    public synchronized void addRentalRequest(RentalRequest request) {
    	requests.add(request);
    }
    
    /**
     * Getter function.
     * @return Manager's name.
     */
    public String getManagerName(){
    	return new String(managerName); 
    }

    /**
     * Getter function.
     * @return true if there are waiting requests.
     */
	public boolean hasRequests() {
		return (!requests.isEmpty());
	}
	
    /**
     * Print this as a string.
     */
    public String toString() {
    	StringBuilder str = new StringBuilder(); 
    	str.append("CustomerGroupDetails: Manager Name:" + managerName);
    	str.append("\n\tRentalRequests:");
    	Iterator<RentalRequest> it = requests.iterator();
    	while (it.hasNext()) {
    		str.append("\n\t" + it.next().toString());
    	}
    	str.append("\n\tCustomers:");
    	Iterator<Customer> customer_it = customers.iterator();
    	while(customer_it.hasNext()) {
    		str.append("\n\t\t" + customer_it.next().toString());
    	}
    	
        return str.toString();
    }
    
    /**
     * 
     * @return the next request to be processed by group manager.
     * 	if there are no more request to process - returns null.
     */
    public synchronized RentalRequest getNextRequest(){
    	if (requests.isEmpty())
    		return null;
    	else {
    		return requests.removeFirst();
    	}
    		
    }
    
}
