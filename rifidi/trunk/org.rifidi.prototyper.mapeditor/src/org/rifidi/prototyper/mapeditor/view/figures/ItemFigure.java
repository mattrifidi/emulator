/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.rifidi.prototyper.mapeditor.model.ItemElement;

/**
 * This is a "view" object for displaying the Items on the map
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemFigure extends ImageFigure {

	/** The view model of the Item */
	private ItemElement model;

	/***
	 * Constructor
	 * 
	 * @param element
	 *            The view model of the item
	 */
	public ItemFigure(ItemElement element) {
		this.model = element;
		setLocation(element.getLocation());
		setSize(element.getDimension());
		refreshImage();
	}

	/**
	 * display a border
	 */
	public void showBorder() {
		this.setBorder(new LineBorder(ColorConstants.black));
	}

	/**
	 * Called to refresh an Image when something in the model changes.
	 */
	public void refreshImage() {
		if (getImage() != null) {
			getImage().dispose();
		}
		Display display = PlatformUI.getWorkbench().getDisplay();
		Image orig = model.getImage();
		int width = model.getDimension().width;
		int height = model.getDimension().height;
		setImage(new Image(display, orig.getImageData().scaledTo(width, height)));
	}

}
