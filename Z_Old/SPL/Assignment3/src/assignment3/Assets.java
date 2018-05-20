package assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Holds the Asset objects.
 */
public class Assets {
    private ArrayList<Asset> assets;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);    
    public Assets() {
    	assets = new ArrayList<Asset>();
    }
    
    /**
     * Add a new asset to the asset list.
     * @param newAsset
     * Asset to add.
     */
	public void addAsset(Asset newAsset) {
		this.assets.add(newAsset);
	}
	
	/**
	 * Sort the asset list by size.
	 */
	public void forceSort() {
		Collections.sort(this.assets, new AssetComparator());
	}

    /**
     * Find an asset according to a clerk's demands.
     * @param type
     *   Type of the asset.
     * @param size
     *   Size needed.
     * @return
     *   An appropriate asset if found, null otherwise.
     */
    public synchronized Asset findAsset(String type, int size) {
        Iterator<Asset> iterator = assets.iterator();

        while(iterator.hasNext()) {
        	Asset asset = iterator.next();
        	if (asset.getSize() < size) {
        		// Asset are sorted by size, so if we didn't find one big enough, move to the next. 
        		continue;
        	}
    		if (asset.available() && asset.getType().equals(type)) {
    			return asset;
    		}
    	} 
        
        return null;
    }

    /**
     * Print this as a string.
     */
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("Assets:\n");
        Iterator<Asset> iterator = assets.iterator();
        while(iterator.hasNext()) {
        	Asset asset =  iterator.next();
    		str.append("\t" + asset.toString());
    	}
    	
        return str.toString();
    }
}
