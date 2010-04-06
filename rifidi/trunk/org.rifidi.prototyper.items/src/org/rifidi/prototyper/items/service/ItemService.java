package org.rifidi.prototyper.items.service;

import java.util.List;

import org.rifidi.prototyper.items.model.ItemModel;

/**
 * This is the interface for the ItemService, which stores currently created
 * models
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface ItemService {

	/**
	 * Return a model given the model's ID
	 * 
	 * @param id
	 * @return
	 */
	public ItemModel getItem(Integer id);

	/**
	 * 
	 * @return All models
	 */
	public List<ItemModel> getItems();

	/**
	 * Clear all the models
	 */
	public void clear();

	/**
	 * Add a new Model to the service
	 * 
	 * @param item
	 *            The item to add
	 * @throws DuplicateItemException
	 */
	public void addItem(ItemModel item) throws DuplicateItemException;

}
