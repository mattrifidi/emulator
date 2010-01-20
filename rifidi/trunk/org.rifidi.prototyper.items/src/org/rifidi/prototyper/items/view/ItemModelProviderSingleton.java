/**
 * 
 */
package org.rifidi.prototyper.items.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.model.ItemType;
import org.rifidi.prototyper.items.service.DuplicateItemException;
import org.rifidi.prototyper.items.service.ItemService;

/**
 * A singleton that manages the List of items that have been created.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemModelProviderSingleton implements ItemService {

	/** The static singleton instance */
	private static ItemModelProviderSingleton provider;
	/** The list of items that this model contains */
	private Map<ItemType, List<ItemModel>> itemsByType;
	/** The listeners that need to know about changes to the model */
	private Set<ItemModelProviderListener> listeners;
	private Integer itemIDs = 0;;

	/**
	 * A private constructor for the singleton
	 */
	private ItemModelProviderSingleton() {
		provider = this;
		itemsByType = new HashMap<ItemType, List<ItemModel>>();
		listeners = new HashSet<ItemModelProviderListener>();
	}

	/**
	 * Gets the instance of this singleton
	 * 
	 * @return
	 */
	public static ItemModelProviderSingleton getModelProvider() {
		if (provider == null) {
			new ItemModelProviderSingleton();
		}
		return provider;
	}

	/**
	 * Gets a list of all the items in this model
	 * 
	 * @return
	 */
	public List<ItemModel> getItems() {
		List<ItemModel> retVal = new LinkedList<ItemModel>();
		for (ItemType type : itemsByType.keySet()) {
			retVal.addAll(itemsByType.get(type));
		}
		return retVal;
	}

	public Map<ItemType, List<ItemModel>> getViewerInput() {
		return new HashMap<ItemType, List<ItemModel>>(itemsByType);
	}

	/**
	 * Add an item to the model
	 * 
	 * @param item
	 */
	public void createItem(ItemModel item) throws DuplicateItemException {
		item.setItemID(itemIDs);
		item.getTag().setTagEntitiyID(new Long(this.itemIDs));
		addItem(item);
	}

	/**
	 * Remove an item from the model
	 * 
	 * @param item
	 */
	public void removeItem(ItemModel item) {
		ItemType type = item.getType();
		itemsByType.get(type).remove(item);
		if(itemsByType.get(type).isEmpty()){
			itemsByType.remove(type);
		}
		
		for (ItemModelProviderListener l : listeners) {
			l.ItemRemoved(item);
		}
	}

	/**
	 * Add a new listener to the model
	 * 
	 * @param listener
	 */
	public void registerListener(ItemModelProviderListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Remove a listener from the model
	 * 
	 * @param listener
	 */
	public void unregisterListener(ItemModelProviderListener listener) {
		this.listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.items.service.ItemService#getItem(java.lang.String)
	 */
	@Override
	public ItemModel getItem(Integer id) {
		for (ItemModel item : getItems()) {
			if (item.getItemID().equals(id)) {
				return item;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.items.service.ItemService#addItem(org.rifidi.prototyper
	 * .items.model.ItemModel)
	 */
	@Override
	public void addItem(ItemModel item) throws DuplicateItemException {
		if (getItem(item.getItemID())!=null) {
			throw new DuplicateItemException(
					"There is already an item with ID " + item.getItemID());
		}
		if (itemIDs <= item.getItemID()) {
			itemIDs = item.getItemID() + 1;
		}
		
		if(!itemsByType.containsKey(item.getType())){
			itemsByType.put(item.getType(), new LinkedList<ItemModel>());
		}
		itemsByType.get(item.getType()).add(item);
		
		for (ItemModelProviderListener l : listeners) {
			l.ItemAdded(item);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.prototyper.items.service.ItemService#clear()
	 */
	@Override
	public void clear() {
		Collection<ItemModel> items = getItems();
		for (ItemModel item : items) {
			removeItem(item);
		}

	}
}
