/**
 * 
 */
package org.rifidi.prototyper.map.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.rifidi.prototyper.model.FloorplanViewModel;
import org.rifidi.prototyper.model.HotspotViewModel;
import org.rifidi.prototyper.model.ItemViewModel;

/**
 * This class is a singleton that keeps track of all objects that are currently
 * a part of the model (e.g. item models, hotspot models, etc).
 * 
 * Controllers that want to modify the state of the model (for example, handlers
 * that load items either from persistance or from a DND operation) should use
 * this singleton to add them.
 * 
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ViewModelSingleton {

	/** The reference to this singleton */
	private static ViewModelSingleton instance = null;
	/** The Floorplan for the modelF */
	private FloorplanViewModel floorplan;
	/** All the hotspots in the model */
	private HashMap<String, HotspotViewModel> hotspots;
	/** All the items in the model */
	private HashMap<String, ItemViewModel> items;
	/** Listeners for items to be added and removed */
	private Set<ItemListener> itemListeners;
	/** Listeners for hotspots to be added and removed */
	private Set<HotspotListener> hotspotListeners;
	/** Listeners for the edit mode to change */
	private Set<EditModeListener> editModeListeners;

	/**
	 * Private constructor for this singleton
	 */
	private ViewModelSingleton() {
		instance = this;
		hotspots = new HashMap<String, HotspotViewModel>();
		items = new HashMap<String, ItemViewModel>();
		itemListeners = new HashSet<ItemListener>();
		editModeListeners = new HashSet<EditModeListener>();
		hotspotListeners = new HashSet<HotspotListener>();
		init();

	}

	/**
	 * Private method to initialize any of the layers.
	 */
	private void init() {
		floorplan = new FloorplanViewModel();
		HotspotViewModel hs_dd = new HotspotViewModel();
		// hs_dd.setX(700);
		// hs_dd.setY(200);
		// hs_dd.setMinimumHeight(50);
		// hs_dd.setMinimumWidth(50);
		// hs_dd.setName("dock door");
		// hotspots.put(hs_dd.getName(), hs_dd);
		//
		// HotspotViewModel hs_ws = new HotspotViewModel();
		// hs_ws.setX(420);
		// hs_ws.setY(150);
		// hs_ws.setMinimumHeight(100);
		// hs_ws.setMinimumWidth(10);
		// hs_ws.setName("weigh station");
		// hotspots.put(hs_ws.getName(), hs_ws);
	}

	/**
	 * Get an instance of this singleton
	 * 
	 * @return
	 */
	public static ViewModelSingleton getInstance() {
		if (instance == null) {
			new ViewModelSingleton();
		}
		return instance;
	}

	/**
	 * Get the floorplan for the model
	 * 
	 * @return
	 */
	public FloorplanViewModel getFloorplan() {
		return floorplan;
	}

	/**
	 * Get all the hotspots
	 * 
	 * @return
	 */
	public Set<HotspotViewModel> getHotspots() {
		return new HashSet<HotspotViewModel>(hotspots.values());
	}

	/**
	 * Get all the item in the model
	 * 
	 * @return
	 */
	public Set<ItemViewModel> getItems() {
		return new HashSet<ItemViewModel>(items.values());
	}

	/**
	 * Add a listener to this object
	 * 
	 * @param o
	 */
	public void addListener(Object o) {
		if (o instanceof ItemListener) {
			itemListeners.add((ItemListener) o);
		}
		if (o instanceof EditModeListener) {
			editModeListeners.add((EditModeListener) o);
		}
		if (o instanceof HotspotListener) {
			hotspotListeners.add((HotspotListener) o);
		}
	}

	/**
	 * Remove a listener from this class
	 * 
	 * @param o
	 */
	public void removeListener(Object o) {
		if (o instanceof ItemListener) {
			itemListeners.remove((ItemListener) o);
		}
		if (o instanceof EditModeListener) {
			editModeListeners.remove((EditModeListener) o);
		}
		if (o instanceof HotspotListener) {
			hotspotListeners.remove((HotspotListener) o);
		}
	}

	/**
	 * Add an item to the model.
	 * 
	 * @param item
	 */
	public void addItem(ItemViewModel item) {
		if (items.containsKey(item.getItemID())) {
			return;
		}
		items.put(item.getItemID(), item);
		for (ItemListener il : itemListeners) {
			il.itemAdded(item);
		}
	}

	/**
	 * remove an item from the model
	 * 
	 * @param itemID
	 */
	public void removeItem(String itemID) {
		if (!items.containsKey(itemID)) {
			return;
		}
		ItemViewModel model = items.remove(itemID);
		for (ItemListener il : itemListeners) {
			il.itemDeleted(model);
		}
	}

	/**
	 * Add a hotspot to the model.
	 * 
	 * @param hotspot
	 */
	public void addHotspot(HotspotViewModel hotspot) {
		if (hotspots.containsKey(hotspot.hashString())) {
			return;
		}
		hotspots.put(hotspot.hashString(), hotspot);
		for (HotspotListener l : hotspotListeners) {
			l.hotspotAdded(hotspot);
		}
	}

	/**
	 * Remove a hotspot from the model.
	 * 
	 * @param hashstring
	 */
	public void removeHotspot(String hashstring) {
		if (!hotspots.containsKey(hashstring)) {
			return;
		}
		HotspotViewModel model = hotspots.remove(hashstring);
		for (HotspotListener l : hotspotListeners) {
			l.hotspotDeleted(model);
		}
	}

	/**
	 * Toggle the edit mode.
	 * 
	 * @param toggle
	 */
	public void setEditMode(boolean toggle) {
		for (EditModeListener listener : editModeListeners) {
			listener.setEditMode(toggle);
		}
	}

}
