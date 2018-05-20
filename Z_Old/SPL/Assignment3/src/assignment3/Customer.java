package assignment3;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Holds customer information.
 */
public class Customer {
    // Vandalism types:
	enum DAMAGE_TYPES {NONE, FIXED, ARBITRARY};

    private String name;
    private DAMAGE_TYPES vandalismType;
    private double minimumDamage;
    private double maximumDamage;
    
    public Customer(String _name, DAMAGE_TYPES type, double minDamage, double maxDamage) {
    	name = _name;
    	minimumDamage = minDamage;
    	maximumDamage = maxDamage;
    	// Assume type is a vandlismType as defined above.
    	vandalismType = type;
    }
    
    /**
     * Gets how much damage the customer caused to the asset.
     */
    public double calculateDamage() {
        if (vandalismType == DAMAGE_TYPES.FIXED) {
            // Return the average of the two limits.
            return (maximumDamage + minimumDamage) / 2;
        }

        if (vandalismType == DAMAGE_TYPES.ARBITRARY) {
            // Return a random number within the limit.
            Random randomGen = new Random();
            return minimumDamage + (randomGen.nextDouble() * (maximumDamage - minimumDamage));
        }

        // DAMAGE_NONE:
        return 0.5;
    }

    /**
     * Getter function.
     */
    public String getName() {
    	return name;
    }

    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "Customer: Name:" + name +"\tVandlism:" + vandalismType + "\tDamage:[" + minimumDamage + ":" + maximumDamage + "]";
    }

    /**
     * 
     * @returns a new CallableSimulateStayInAsset object that simulates this customers stay
     */
	public Callable<Double> createStayTask(RentalRequest request) {
		return new CallableSimulateStayInAsset(this, request.getDurationOfStay());
	}
    
}
