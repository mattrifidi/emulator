/**
 * 
 */
package org.rifidi.prototyper.items.view;

import org.rifidi.prototyper.items.model.ItemModel;

/**
 * Clients that are interested in when items are added or removed should
 * implement this interface and register themselves with the ItemService.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface ItemModelProviderListener {

	/**
	 * Called when an Item is added
	 * 
	 * @param item
	 */
	void ItemAdded(ItemModel item);

	/**
	 * Called when an Item is removed.
	 * 
	 * @param item
	 */
	void ItemRemoved(ItemModel item);

}
