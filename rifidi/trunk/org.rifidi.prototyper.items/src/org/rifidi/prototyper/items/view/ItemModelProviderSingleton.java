/**
 * 
 */
package org.rifidi.prototyper.items.view;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rifidi.prototyper.items.model.TaggedItem;
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
	private List<TaggedItem> items;
	/** The listeners that need to know about changes to the model */
	private Set<ItemModelProviderListener> listeners;
	private ItemService itemService;

	/**
	 * A private constructor for the singleton
	 */
	private ItemModelProviderSingleton() {
		provider = this;
		items = new LinkedList<TaggedItem>();
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
	public List<TaggedItem> getItems() {
		return new LinkedList<TaggedItem>(items);
	}

	/**
	 * Add an item to the model
	 * 
	 * @param item
	 */
	public void addItem(TaggedItem item) {
		items.add(item);
		for (ItemModelProviderListener l : listeners) {
			l.ItemAdded(item);
		}
	}

	/**
	 * Remove an item from the model
	 * 
	 * @param item
	 */
	public void removeItem(TaggedItem item) {
		items.remove(item);
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
	public TaggedItem getItem(String id) {
		for(TaggedItem item : this.items){
			if(item.getTag().equals(id)){
				return item;
			}
		}
		return null;
	}

}
