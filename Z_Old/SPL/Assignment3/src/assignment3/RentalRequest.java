package assignment3;

/**
 * Holds information about a rental request.
 */
public class RentalRequest {
    // Status options.
    public static enum RENTAL_STATUS{INCOMPLETE, FULFILLED, IN_PROGRESS, COMPLETE};

    private String requestId;
    private String type;
    private int size;
    private int duration;
    private RENTAL_STATUS status;

    // This is set after a RunnableClerk found a fitting asset.
    private Asset asset;
    
    public RentalRequest(String id, String _type, int _size, int _duration) {
    	asset = null;
    	status = RENTAL_STATUS.INCOMPLETE;
    	
    	requestId = id;
    	type = _type;
    	size = _size;
    	duration = _duration;
    }

    /**
     * Getter function.
     */
    public String getId() {
    	return requestId;
    }
    
    /**
     * changes status of rental request: STATUS_INCOMPLETE => STATUS_FULFILLED => STATUS_IN_PROGRESS => STATUS_COMPLETE
     * 	also - notifies all objects waiting for object.
     * 	note that if status is already complete, it is not changed.  
     */
    public synchronized void incrementStatus(){
    	if (status == RENTAL_STATUS.INCOMPLETE)
    		status = RENTAL_STATUS.FULFILLED;
    	else if (status == RENTAL_STATUS.FULFILLED){
    		status = RENTAL_STATUS.IN_PROGRESS;
    		asset.setStatusOccuipied();
    	}
    	else if (status == RENTAL_STATUS.IN_PROGRESS) {
    		status = RENTAL_STATUS.COMPLETE;
    	}
    	
    	notifyAll();
    }
    
    /**
     * Getter function.
     */
    public synchronized RENTAL_STATUS getStatus() {
    	return status;
    }
    
    
    public Asset findAppropriateAsset(Assets assets) {
    	return assets.findAsset(type, size);
	}
    

    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
    	String str = "\tRentalRequest ID:" + requestId;
    	str += "\t\tType:" + type + "\t\tSize:" + size + "\t\tDuration:" + duration + "\t\tStatus:" + status;
    	str += "\t\tAsset:" + (asset == null ? "NONE" : asset.getName());
        return str;
    }

	public Asset getAsset() {
		return this.asset;
	}

	public int getDurationOfStay() {
		return duration;
	}

	public void setAsset(Asset _asset) {
		this.asset = _asset;
	}
}
