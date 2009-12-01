/**
 * 
 */
package org.rifidi.prototyper.map.controller;

import org.rifidi.prototyper.model.ItemViewModel;

/**
 * @author kyle
 * 
 */
public interface ItemListener {

	void itemAdded(ItemViewModel item);

	void itemDeleted(ItemViewModel item);

}
