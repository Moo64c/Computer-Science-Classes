package assignment3;

/**
 * Stores parsed info, and used by RunnableClerk.
 */
public class ClerkDetails {
    private String name;
    public Location location;

    
    public ClerkDetails(String _name, Location _location) {
    	name = _name;
    	location = _location;
    }
    
    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "ClerkDetails: Name:" + name + "\t" + location.toString();
    }

	public String getName() {
		return name;
	}
}
