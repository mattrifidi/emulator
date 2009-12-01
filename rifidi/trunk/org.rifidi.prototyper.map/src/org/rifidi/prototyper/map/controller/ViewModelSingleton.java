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
 * @author kyle
 * 
 */
public class ViewModelSingleton {

	private static ViewModelSingleton instance = null;
	private FloorplanViewModel floorplan;
	private HashMap<String, HotspotViewModel> hotspots;
	private HashMap<String, ItemViewModel> items;
	private Set<ItemListener> itemListeners;
	private Set<EditModeListener> editModeListeners;
	private Set<HotspotListener> hotspotListeners;

	public ViewModelSingleton() {
		instance = this;
		hotspots = new HashMap<String, HotspotViewModel>();
		items = new HashMap<String, ItemViewModel>();
		init();
		itemListeners = new HashSet<ItemListener>();
		editModeListeners = new HashSet<EditModeListener>();
		hotspotListeners = new HashSet<HotspotListener>();

	}

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

	public static ViewModelSingleton getInstance() {
		if (instance == null) {
			new ViewModelSingleton();
		}
		return instance;
	}

	public FloorplanViewModel getFloorplan() {
		return floorplan;
	}

	public Set<HotspotViewModel> getHotspots() {
		return new HashSet<HotspotViewModel>(hotspots.values());
	}

	public Set<ItemViewModel> getItems() {
		return new HashSet<ItemViewModel>(items.values());
	}

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

	public void addItem(ItemViewModel item) {
		if (items.containsKey(item.getItemID())) {
			return;
		}
		items.put(item.getItemID(), item);
		for (ItemListener il : itemListeners) {
			il.itemAdded(item);
		}
	}

	public void removeItem(String itemID) {
		if (!items.containsKey(itemID)) {
			return;
		}
		ItemViewModel model = items.remove(itemID);
		for (ItemListener il : itemListeners) {
			il.itemDeleted(model);
		}
	}

	public void addHotspot(HotspotViewModel hotspot) {
		if (hotspots.containsKey(hotspot.hashString())) {
			return;
		}
		hotspots.put(hotspot.hashString(), hotspot);
		for (HotspotListener l : hotspotListeners) {
			l.hotspotAdded(hotspot);
		}
	}

	public void removeHotspot(String hashstring) {
		if (!hotspots.containsKey(hashstring)) {
			return;
		}
		HotspotViewModel model = hotspots.remove(hashstring);
		for (HotspotListener l : hotspotListeners) {
			l.hotspotDeleted(model);
		}
	}

	public void setEditMode(boolean toggle) {
		for (EditModeListener listener : editModeListeners) {
			listener.setEditMode(toggle);
		}
	}

}
