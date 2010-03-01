/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.prototyper.mapeditor.view.figures.ItemFigure;
import org.rifidi.prototyper.mapeditor.view.parts.policies.ItemComponentEditPolicy;
import org.rifidi.prototyper.mapeditor.view.parts.policies.ItemContainerEditPolicy;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#createEditPolicies
	 * ()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ItemComponentEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ItemContainerEditPolicy());
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
		if (arg0.getPropertyName().equals(AbstractMapModelElement.PROP_CHILD)) {
			if (arg0.getNewValue() != null) {
				((ItemFigure) getFigure()).showBorder();
			}
			if (arg0.getOldValue() != null) {

			}
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
		((ItemFigure) getFigure()).refreshImage();
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
		return "Item Part " + getModelElement().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart#getHoverText()
	 */
	@Override
	public String getHoverText() {
		StringBuilder builder = new StringBuilder();
		builder.append(getModelElement().getName());
		if (getModelElement().isContainer()) {
			builder.append("\n-------\n");
			for (Object o : getModelElement().getContainedItems()) {
				if (o instanceof ItemElement)
					builder.append("   " + ((ItemElement) o).getName() + "\n");
			}
		}
		return builder.toString();
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
		// ((MapPart) getParent().getParent()).getHotspotLayerPart()
		// .manageCollisions(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new ItemDragTracker(this);
	}

}
