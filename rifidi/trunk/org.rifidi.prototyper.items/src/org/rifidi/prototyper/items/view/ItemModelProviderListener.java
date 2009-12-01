/**
 * 
 */
package org.rifidi.prototyper.items.view;

import org.rifidi.prototyper.items.model.TaggedItem;

/**
 * @author kyle
 * 
 */
public interface ItemModelProviderListener {

	void ItemAdded(TaggedItem item);

	void ItemRemoved(TaggedItem item);

}
