package assignment3;

import java.util.Comparator;

/**
 * Comparator to sort the assets by incrementing size.
 * @author amir
 */
public class AssetComparator implements Comparator<Asset>
{
	public int compare(Asset asset1, Asset asset2) {
		return asset2.getSize() - asset1.getSize();
	}
}
