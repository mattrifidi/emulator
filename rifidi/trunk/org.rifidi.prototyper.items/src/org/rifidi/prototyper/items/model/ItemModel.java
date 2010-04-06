/**
 * 
 */
package org.rifidi.prototyper.items.model;

import java.io.Serializable;

import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.items.Activator;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is a bean that is the business model object for Tagged Items. Two items
 * are equal if their itemIDs are equal.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemModel implements Serializable, Comparable<ItemModel> {

	/***/
	private static final long serialVersionUID = 1L;
	/** The Tag ID */
	private RifidiTag tag;
	/** The name of the item */
	private String name;
	/** The type of item */
	private ItemType type;
	/** An internal ID used to identify this item */
	private Integer itemID;
	private Boolean container=null;

	/**
	 * @return the tag
	 */
	public RifidiTag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(RifidiTag tag) {
		this.tag = tag;
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
	 * @return the type
	 */
	public ItemType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ItemType type) {
		this.type = type;
	}

	/**
	 * An ID used to identify this object. Not the RFID Tag ID!
	 * 
	 * @return the itemID
	 */
	public Integer getItemID() {
		return itemID;
	}

	/**
	 * An ID used to identify this object. Not the RFID Tag ID!
	 * 
	 * @param itemID
	 *            the itemID to set
	 */
	public void setItemID(Integer itemID) {
		this.itemID = itemID;
	}

	public Image getImage() {
		return Activator.getDefault().getImageRegistry().get(type.getType());
	}

	/**
	 * @return the container
	 */
	public Boolean getContainer() {
		if (container==null)
			return type.getIsContainer();
		else return type.getIsContainer();
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(Boolean container) {
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this.itemID != null) {
			if (obj != null) {
				if (obj instanceof ItemModel) {
					ItemModel that = (ItemModel) obj;
					if (this.itemID.equals(that.itemID)) {
						return true;
					}
				}
			}
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
		return "Item Model (" + getItemID() + ") : " + getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ItemModel o) {
		return this.getName().compareTo(o.getName());
	}

}
