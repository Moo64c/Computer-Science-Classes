package assignment3;

/**
 * Written when a customer group leaves an asset.
 * Sent to management so they will decide what to do on.
 */
public class DamageReport {
    private Asset asset;
    private double damagePercent;

    /**
     * Constructor; calculates total damage.
     * @param _asset 
     * 	Asset to check.
     * @param customers
     *  The customers leaving the asset.
     */
    public DamageReport(Asset _asset, double _damagePercent) {
    	asset = _asset;
    	damagePercent = _damagePercent;
    	asset.damageContent(damagePercent);
    }
    
    /**
     * Gets the total amount of damage.
     * @return
     *   Sum of the damage percentages.
     */
    public double getTotalDamagePercent() {
		return damagePercent;
    }
    
    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "DamageReport for asset:" + asset.getName() + "\t" + "damage caused:" + damagePercent;
    }
    
    public Asset getAsset(){
    	return asset;
    }
}
