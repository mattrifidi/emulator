/**
 * 
 */
package org.rifidi.prototyper.items.model;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author kyle
 * 
 */
@XmlRootElement
public class ItemType implements Serializable, Comparable<ItemType> {

	/***/
	private static final long serialVersionUID = 1L;
	private String type;
	private Set<String> categories;
	private String imagePath;
	private Boolean isContainer;
	private Boolean containedItemsVisible;
	private Integer defaultHeight;
	private Integer defaultWidth;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the categories
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath
	 *            the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the isContainer
	 */
	public Boolean getIsContainer() {
		return isContainer;
	}

	/**
	 * @param isContainer
	 *            the isContainer to set
	 */
	public void setIsContainer(Boolean isContainer) {
		this.isContainer = isContainer;
	}

	/**
	 * @return the containedItemsVisible
	 */
	public Boolean getContainedItemsVisible() {
		return containedItemsVisible;
	}

	/**
	 * @param containedItemsVisible
	 *            the containedItemsVisible to set
	 */
	public void setContainedItemsVisible(Boolean containedItemsVisible) {
		this.containedItemsVisible = containedItemsVisible;
	}

	/**
	 * @return the defaultHeight
	 */
	public Integer getDefaultHeight() {
		return defaultHeight;
	}

	/**
	 * @param defaultHeight
	 *            the defaultHeight to set
	 */
	public void setDefaultHeight(Integer defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	/**
	 * @return the defaultWidth
	 */
	public Integer getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * @param defaultWidth
	 *            the defaultWidth to set
	 */
	public void setDefaultWidth(Integer defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof ItemType) {
				ItemType that = (ItemType) obj;
				return this.type.equalsIgnoreCase(that.type);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return type.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TYPE: " + type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ItemType o) {
		return this.getType().compareTo(o.getType());
	}

}
