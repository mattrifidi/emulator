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
 * This class is the model element for Items (boxes, forklifts, etc).
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemElement extends AbstractMapModelElement implements
		Container<ItemElement> {
	/** The serial version ID */
	static final long serialVersionUID = 1;
	/** The model object */
	private ItemModel model;
	/** The location of this model element */
	private Point location;
	/** The size of this model element */
	private Dimension dimension;
	/** A list of items contained in this model */
	private List<ItemElement> containedItems;

	/***
	 * Constructor
	 * 
	 * @param model
	 */
	public ItemElement(ItemModel model) {
		this.model = model;
		init();

	}

	/**
	 * A private method to initialize this object, which should be called from
	 * the constructor and when this object is being recreaed from a serialized
	 * byte array
	 */
	private void init() {
		// TODO: calculate
		dimension = new Dimension(getImage());
		if (containedItems == null)
			this.containedItems = new ArrayList<ItemElement>();

	}

	/**
	 * A method to serialize this object from a byte array
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	/**
	 * Return true if this item can contain other items
	 * 
	 * @return
	 */
	public boolean isContainer() {
		return model.getContainer();
	}

	/**
	 * Use this method to allow or disallow this item to hold items
	 * 
	 * @param container
	 */
	public void setContainer(boolean container) {
		model.setContainer(container);
	}

	/**
	 * Add a contained item to this model
	 * 
	 * @param item
	 */
	public void addContainedItem(ItemElement item) {
		if (model.getContainer()) {
			this.containedItems.add(item);
			super.fireChildAdded(item);
		}
	}

	/**
	 * Use this metohd to remove a contained item
	 * 
	 * @param item
	 * @return
	 */
	public ItemElement removeContainedItem(ItemElement item) {
		if (this.containedItems.remove(item)) {
			super.fireChildRemoved(item);
			return item;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.prototyper.mapeditor.model.Container#contains(org.rifidi.
	 * prototyper.mapeditor.model.AbstractMapModelElement)
	 */
	@Override
	public boolean contains(ItemElement element) {
		if (this.equals(element)) {
			return true;
		}
		for (ItemElement e : containedItems) {
			if (e.contains(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all the items this item contains
	 * 
	 * @return
	 */
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
