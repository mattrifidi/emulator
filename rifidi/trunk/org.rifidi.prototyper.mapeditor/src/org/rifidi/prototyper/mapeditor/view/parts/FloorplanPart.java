/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.rifidi.prototyper.mapeditor.model.FloorplanElement;

/**
 * This is an EditPart for Floorplans.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class FloorplanPart extends AbstractMapPart<FloorplanElement> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		FloorplanElement model = getModelElement();
		Layer floorplanLayer = new Layer();
		if (model != null) {
			ImageFigure imageFig = new ImageFigure();
			imageFig.setLayoutManager(new GridLayout());
			imageFig.setImage(model.getFloorplanImage());
			imageFig.setBounds(new Rectangle(model.getFloorplanImage()
					.getBounds()));
			int width = model.getFloorplanImage().getBounds().width;
			int height = model.getFloorplanImage().getBounds().height;
			floorplanLayer.setSize(new Dimension(width, height));
			floorplanLayer.add(imageFig);
		}
		return floorplanLayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return false;
	}

}
