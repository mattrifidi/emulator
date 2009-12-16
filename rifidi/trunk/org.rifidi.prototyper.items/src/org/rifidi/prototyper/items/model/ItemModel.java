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
public class ItemModel {

	/** The Tag ID */
	private String tag;
	/** The name of the item */
	private String name;
	/** The type of item */
	private String type;
	private String imagePath;

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
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Image getImage() {
		return Activator.getDefault().getImageRegistry().get(type);
	}

}
