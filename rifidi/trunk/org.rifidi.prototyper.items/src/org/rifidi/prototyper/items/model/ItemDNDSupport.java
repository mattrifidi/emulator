/**
 * 
 */
package org.rifidi.prototyper.items.model;

/**
 * This is a helper class to parse drag and drop TextTransfers for items
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemDNDSupport {

	/**
	 * Returns true if the given text tranfer is for an Item
	 * 
	 * @param textTransfer
	 * @return
	 */
	public static boolean isItem(String textTransfer) {
		return textTransfer.startsWith("item");
	}

	/**
	 * Returns the itemID given the text tranfer string
	 * 
	 * @param textTranfer
	 * @return
	 */
	public static String getItemID(String textTransfer) {
		try {
			return textTransfer.split("\\|")[1].split(":")[1];
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid text tranfer string: "
					+ textTransfer);
		}
	}

	/**
	 * Return a new text transfer string given an ItemID
	 * 
	 * @param itemID
	 * @return
	 */
	public static String getTextTranfer(String itemID) {
		if (itemID != null && !itemID.trim().equals("")) {
			return "item|id:" + itemID;
		} else {
			throw new IllegalArgumentException("itemID cannot be null");
		}
	}

}
