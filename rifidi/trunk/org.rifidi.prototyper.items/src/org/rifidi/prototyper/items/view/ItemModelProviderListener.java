/**
 * 
 */
package org.rifidi.prototyper.items.view;

import org.rifidi.prototyper.items.model.ItemModel;

/**
 * @author kyle
 * 
 */
public interface ItemModelProviderListener {

	void ItemAdded(ItemModel item);

	void ItemRemoved(ItemModel item);

}
