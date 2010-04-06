/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;

/**
 * This is a "view" object for displaying the Hotspots on the map
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotFigure extends RoundedRectangle {

	/** The Model object */
	private HotspotElement model;
	/** The scale used to calculate the size */
	private Float pixelsPerFoot;
	/** The figure to display */
	private ImageFigure imageFigure;

	/**
	 * Constructor
	 * 
	 * @param element
	 *            The view model element
	 * @param pixelsPerFoot
	 *            The scale of the map
	 */
	public HotspotFigure(HotspotElement element, Float pixelsPerFoot) {
		model = element;
		this.pixelsPerFoot = pixelsPerFoot;
		GridLayout layout = new GridLayout(2, false);
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setAlpha(175);
		setSize(calculateSize());
		imageFigure = new ImageFigure();
		add(imageFigure);
		refreshImage();
	}

	/**
	 * This is a private helper method to calculate the size eventually hotpsots
	 * should scale themselves according to the map scale using the antenna
	 * range, but this is not happening right now.
	 * 
	 * @return
	 */
	private Dimension calculateSize() {
		// TODO: Calculate using antenna strength
		// Integer pixels = Math.round(model.getAntennaRage() * pixelsPerFoot);
		// pixels = Math.max(pixels, 30);
		// return new Dimension(pixels, pixels);
		return model.getSize();
	}

	/**
	 * Called to refresh the image when something changes.
	 */
	public void refreshImage() {
		if (imageFigure.getImage() != null) {
			imageFigure.getImage().dispose();
		}
		Display display = PlatformUI.getWorkbench().getDisplay();
		Image orig = model.getImage();
		int width = calculateSize().width;
		int height = calculateSize().height;
		imageFigure.setImage(new Image(display, orig.getImageData().scaledTo(
				width, height)));
	}
}
