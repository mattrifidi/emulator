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
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapModel extends AbstractMapModelElement {

	private static final long serialVersionUID = 1L;
	private String name;
	private FloorplanElement floorplan;
	private final ElementSet<ItemElement> items;
	private final ElementSet<HotspotElement> hotspots;
	private final List<GeneralReaderPropertyHolder> allReaders;
	private final List<ItemModel> allItems;

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

	public void setAllReaders(Collection<GeneralReaderPropertyHolder> readers) {
		allReaders.clear();
		allReaders.addAll(readers);
	}

	public List<GeneralReaderPropertyHolder> getAllReaders() {
		return new LinkedList<GeneralReaderPropertyHolder>(allReaders);
	}

	public void setAllItems(Collection<ItemModel> items) {
		allItems.clear();
		allItems.addAll(items);
	}

	public List<ItemModel> getAllItems() {
		return new LinkedList<ItemModel>(allItems);
	}
}
