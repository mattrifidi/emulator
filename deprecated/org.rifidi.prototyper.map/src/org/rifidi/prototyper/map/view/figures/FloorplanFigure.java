/**
 * 
 */
package org.rifidi.prototyper.map.view.figures;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.map.Activator;
import org.rifidi.prototyper.model.FloorplanViewModel;

/**
 * An object that extends an ImageFigure and wraps a FloorplanViewModel. It
 * displays the model
 * 
 * TODO: add dispose method. Make sure image is disposed.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class FloorplanFigure extends ImageFigure {

	/** The model */
	private FloorplanViewModel model;
	/** The map image. */
	private Image image;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            The model to show
	 */
	public FloorplanFigure(FloorplanViewModel model) {
		this.model = model;
		image = Activator.getImageDescriptor(model.getImageFigurePath())
				.createImage();
		this.setImage(image);
		this.setBounds(new Rectangle(0, 0, image.getBounds().width, image
				.getBounds().height));
	}
}
