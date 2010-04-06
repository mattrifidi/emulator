/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.prototyper.items.model.ItemModel;

/**
 * This class is the model element that contains all other model elements. It's
 * what ties the floorplan, hotspots, and items together.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapModel extends AbstractMapModelElement {

	/** The serial version ID */
	private static final long serialVersionUID = 1L;
	/** The name of this map */
	private String name;
	/** The floorplan Element */
	private FloorplanElement floorplan;
	/** The items */
	private final ElementSet<ItemElement> items;
	/** The hotspot elements */
	private final ElementSet<HotspotElement> hotspots;
	/** The readers available */
	private final List<GeneralReaderPropertyHolder> allReaders;
	/** All the items available */
	private final List<ItemModel> allItems;

	/**
	 * Constructor
	 */
	public MapModel() {
		hotspots = new ElementSet<HotspotElement>(HotspotElement.class);
		items = new ElementSet<ItemElement>(ItemElement.class);
		allReaders = new LinkedList<GeneralReaderPropertyHolder>();
		allItems = new LinkedList<ItemModel>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param floorplan
	 *            the floorplan to set
	 */
	public void setFloorplan(FloorplanElement floorplan) {
		this.floorplan = floorplan;
	}

	/**
	 * @return the floorplan
	 */
	public FloorplanElement getFloorplan() {
		return floorplan;
	}

	/**
	 * @return the hotspots
	 */
	public ElementSet<HotspotElement> getHotspots() {
		return hotspots;
	}

	/**
	 * @return the items
	 */
	public ElementSet<ItemElement> getItems() {
		return items;
	}

	/**
	 * Provide a set of all the available readers.
	 * 
	 * @param readers
	 */
	public void setAllReaders(Collection<GeneralReaderPropertyHolder> readers) {
		allReaders.clear();
		allReaders.addAll(readers);
	}

	/**
	 * Get a list of all readers in the map
	 * 
	 * @return
	 */
	public List<GeneralReaderPropertyHolder> getAllReaders() {
		return new LinkedList<GeneralReaderPropertyHolder>(allReaders);
	}

	/**
	 * Provide a set of all the available Items
	 * 
	 * @param items
	 */
	public void setAllItems(Collection<ItemModel> items) {
		allItems.clear();
		allItems.addAll(items);
	}

	/**
	 * Get all the available items
	 * 
	 * @return
	 */
	public List<ItemModel> getAllItems() {
		return new LinkedList<ItemModel>(allItems);
	}
}
