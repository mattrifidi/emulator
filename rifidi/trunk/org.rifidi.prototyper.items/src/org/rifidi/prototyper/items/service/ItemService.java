package org.rifidi.prototyper.items.service;

import java.util.List;

import org.rifidi.prototyper.items.model.ItemModel;

public interface ItemService {

	/**
	 * The item ID is the item's name.
	 * 
	 * @param id
	 * @return
	 */
	public ItemModel getItem(Integer id);
	
	public List<ItemModel> getItems();
	
	public void clear();
	
	public void addItem(ItemModel item) throws DuplicateItemException;

}
