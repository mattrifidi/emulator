/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
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
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotFigure extends RoundedRectangle {

	private HotspotElement model;
	private Float pixelsPerFoot;
	private ImageFigure imageFigure;

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

	private Dimension calculateSize() {
		//TODO: Calculate using antenna strength
		//Integer pixels = Math.round(model.getAntennaRage() * pixelsPerFoot);
		//pixels = Math.max(pixels, 30);
		//return new Dimension(pixels, pixels);
		return model.getSize();
	}

	public void refreshImage() {
		if(imageFigure.getImage()!=null){
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
