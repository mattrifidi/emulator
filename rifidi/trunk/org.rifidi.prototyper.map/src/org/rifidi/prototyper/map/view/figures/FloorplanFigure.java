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
 * @author kyle
 * 
 */
public class FloorplanFigure extends ImageFigure {

	private FloorplanViewModel model;
	private Image image;

	public FloorplanFigure(FloorplanViewModel model) {
		this.model = model;
		image = Activator.getImageDescriptor(model.getImageFigurePath())
				.createImage();
		this.setImage(image);
		this.setBounds(new Rectangle(0, 0, image.getBounds().width, image
				.getBounds().height));
	}
	
	//TODO: add method to destroy image

}
