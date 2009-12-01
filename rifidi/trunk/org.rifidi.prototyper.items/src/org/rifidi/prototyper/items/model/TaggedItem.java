/**
 * 
 */
package org.rifidi.prototyper.items.model;

import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.items.Activator;

/**
 * This is a bean that is the business model object for Tagged Items
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class TaggedItem {

	/** The Tag ID */
	private String tag;
	/** The name of the item */
	private String name;
	/** The type of item */
	private ItemType type;

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
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

	public Image getImage() {
		switch (type) {
		case CARGO:
			return Activator.getDefault().getImageRegistry().get(
					ItemType.CARGO.name());
		case FORKLIFT:
			return Activator.getDefault().getImageRegistry().get(
					ItemType.FORKLIFT.name());
		default:
			return null;
		}
	}

}
