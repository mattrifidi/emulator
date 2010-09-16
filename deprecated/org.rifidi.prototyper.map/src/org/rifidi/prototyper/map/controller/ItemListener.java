/**
 * 
 */
package org.rifidi.prototyper.map.controller;

import org.rifidi.prototyper.model.ItemViewModel;

/**
 * An interface implemented by classes who want to be notified when an item
 * model is added or removed
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface ItemListener {

	/**
	 * Called when an item model is added.
	 * 
	 * @param item
	 *            the item that was added
	 */
	void itemAdded(ItemViewModel item);

	/**
	 * Called when an item model is removed.
	 * 
	 * @param item
	 *            the item that was removed.
	 */
	void itemDeleted(ItemViewModel item);

}
