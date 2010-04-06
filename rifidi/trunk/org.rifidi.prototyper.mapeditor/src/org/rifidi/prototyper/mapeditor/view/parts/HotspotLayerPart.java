/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;
import org.rifidi.prototyper.mapeditor.view.figures.MapLayer;
import org.rifidi.prototyper.mapeditor.view.parts.policies.HotspotLayerLayoutPolicy;

/**
 * This is the EditPart for the Layer that contains all the HotSpots
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotLayerPart extends
		AbstractMapPart<ElementSet<HotspotElement>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Layer hotspotLayer = new MapLayer();
		hotspotLayer.setLayoutManager(new XYLayout());
		return hotspotLayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new HotspotLayerLayoutPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getChildren()
	 */
	@Override
	public List getModelChildren() {
		return getModelElement().getElements();
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

	/**
	 * Get a list of all hotspots contained in this edit part
	 * 
	 * @return
	 */
	private List<HotspotPart> getHotspots() {
		LinkedList<HotspotPart> retVal = new LinkedList<HotspotPart>();
		for (Object o : getChildren()) {
			if (o instanceof HotspotPart) {
				retVal.add((HotspotPart) o);
			}
		}
		return retVal;
	}

	/**
	 * This method is called when an ItemPart moves so that HotspotParts can
	 * decide if the ItemPart interceted with the Hotspot.
	 * 
	 * @param item
	 */
	public void manageCollisions(ItemPart item) {
		for (HotspotPart hs : getHotspots()) {
			hs.manageCollision(item);
		}
	}

}
