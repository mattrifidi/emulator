/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemElement extends AbstractMapModelElement implements
		Container<ItemElement> {
	static final long serialVersionUID = 1;
	private ItemModel model;
	private Point location;
	private Dimension dimension;
	private List<ItemElement> containedItems;

	public ItemElement(ItemModel model) {
		this.model = model;
		init();

	}

	private void init() {
		// TODO: calculate
		dimension = new Dimension(getImage());
		if (containedItems == null)
			this.containedItems = new ArrayList<ItemElement>();

	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
		super.firePropertyMoved(this);
	}

	/**
	 * @return the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * @param dimension
	 *            the dimension to set
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		super.firePropertyMoved(this);
	}

	public Image getImage() {
		return model.getImage();
	}

	public String getName() {
		return model.getName();
	}

	public RifidiTag getTag() {
		return model.getTag();
	}

	/**
	 * Gets the Set of tags that can be seen when this item enters a hotspot.
	 * This includes the tag on this item and any tags on items that this item
	 * contains.
	 * 
	 * @return
	 */
	public Set<RifidiTag> getVisibleTags() {
		Set<RifidiTag> tags = new HashSet<RifidiTag>();
		tags.add(getTag());
		for (ItemElement item : containedItems) {
			tags.addAll(item.getVisibleTags());
		}
		return tags;
	}

	public boolean isContainer() {
		return model.getContainer();
	}

	public void setContainer(boolean container) {
		model.setContainer(container);
	}

	public void addContainedItem(ItemElement item) {
		this.containedItems.add(item);
		super.fireChildAdded(item);
	}

	public ItemElement removeContainedItem(ItemElement item) {
		if (this.containedItems.remove(item)) {
			super.fireChildRemoved(item);
			return item;
		}
		return null;
	}

	@Override
	public boolean contains(ItemElement item) {
		if (this.equals(item)) {
			return true;
		}
		for (ItemElement e : containedItems) {
			if (e.contains(item)) {
				return true;
			}
		}
		return false;

	}

	public List getContainedItems() {
		return new ArrayList<ItemElement>(this.containedItems);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof ItemElement)) {
			ItemElement that = (ItemElement) obj;
			return this.model.equals(that.model);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item Element: ( " + model + " )";
	}

}
