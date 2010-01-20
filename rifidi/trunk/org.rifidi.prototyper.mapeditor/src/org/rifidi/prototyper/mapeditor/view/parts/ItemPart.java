/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.prototyper.mapeditor.view.figures.ItemFigure;
import org.rifidi.prototyper.mapeditor.view.parts.policies.ItemComponentEditPolicy;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemPart extends AbstractMapPart<ItemElement> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new ItemFigure(getModelElement());
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ItemComponentEditPolicy());
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#propertyChange
	 * (java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(AbstractMapModelElement.PROP_MOVED)) {
			refreshVisuals();
			((MapPart) getParent().getParent()).getHotspotLayerPart()
					.manageCollisions(this);
		}
		super.propertyChange(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		ItemElement model = getModelElement();
		Rectangle bounds = new Rectangle(model.getLocation(), model
				.getDimension());
		((ItemFigure)getFigure()).refreshImage();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	@Override
	public String toString() {
		return "Item Part " + getModelElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#getHoverText()
	 */
	@Override
	public String getHoverText() {
		return getModelElement().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		((MapPart) getParent().getParent()).getHotspotLayerPart()
				.manageCollisions(this);
	}

}
