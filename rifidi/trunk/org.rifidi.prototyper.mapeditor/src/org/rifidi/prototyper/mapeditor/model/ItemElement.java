/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemElement extends AbstractMapModelElement {
	static final long serialVersionUID = 1;
	private ItemModel model;
	private Point location;
	private Dimension dimension;

	public ItemElement(ItemModel model) {
		this.model = model;
		// TODO: calculate
		dimension = new Dimension(getImage());
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
		return "Item Element: ( " +model+" )";
	}

}
