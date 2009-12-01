/**
 * 
 */
package org.rifidi.prototyper.map.view.mousehandler;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.rifidi.prototyper.map.controller.EditModeListener;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.figures.HotspotFigure;
import org.rifidi.prototyper.map.view.layers.HotspotLayer;

/**
 * @author kyle
 * 
 */
public class EditModeMapViewMouseHandler extends
		AbstractMapViewMouseHandlerState implements EditModeListener {

	private HotspotLayer hsLayer;
	private HotspotFigure selectedhotspot;
	private Dimension selectedHotspotOffset;

	public EditModeMapViewMouseHandler(HotspotLayer hsLayer) {
		super();
		this.hsLayer = hsLayer;
		ViewModelSingleton.getInstance().addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mousePressed(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		IFigure figure = hsLayer.findFigureAt(me.getLocation());
		HotspotFigure hotspot = null;
		while (figure != null && hotspot == null) {
			if (figure instanceof HotspotFigure) {
				hotspot = (HotspotFigure) figure;
			} else {
				figure = figure.getParent();
			}
		}

		if (hotspot != null) {
			if (selectedhotspot != null && hotspot != selectedhotspot) {
				selectedhotspot.unselect();
				selectedhotspot = null;
				selectedHotspotOffset = null;
			}
			this.selectedhotspot = hotspot;
			selectedHotspotOffset = hotspot.getLocation().getDifference(
					me.getLocation());
			hotspot.select();
		} else {
			if (selectedhotspot != null) {
				selectedhotspot.unselect();
				selectedhotspot = null;
				selectedHotspotOffset = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		if (selectedhotspot != null) {
			selectedhotspot.move(me.getLocation().getTranslated(
					selectedHotspotOffset));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #keyPressed(org.eclipse.draw2d.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.character == SWT.DEL) {
			if (selectedhotspot != null) {
				selectedhotspot.unselect();
				ViewModelSingleton.getInstance().removeHotspot(
						selectedhotspot.getHashstring());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.EditModeListener#setEditMode(boolean
	 * )
	 */
	@Override
	public void setEditMode(boolean toggle) {
		if (!toggle) {
			if (selectedhotspot != null) {
				selectedhotspot.unselect();
			}
		}

	}

}
