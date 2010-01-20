/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public abstract class AbstractMapPart<T extends AbstractMapModelElement>
		extends AbstractGraphicalEditPart implements PropertyChangeListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName().equals(AbstractMapModelElement.PROP_CHILD)) {
			if (arg0.getNewValue() == null) {
				AbstractMapModelElement model = (AbstractMapModelElement) arg0
						.getOldValue();
				EditPart p = (EditPart) getRoot().getViewer()
						.getEditPartRegistry().get(model);
				removeChild(p);
				refreshVisuals();
			} else if (arg0.getOldValue() == null) {
				addChild(createChild(arg0.getNewValue()), -1);
				refreshVisuals();
			}
		}
	}

	public T getModelElement() {
		return (T) getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive()) {
			return;
		}
		super.activate();
		getModelElement().addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (!isActive()) {
			return;
		}
		super.deactivate();
		getModelElement().removePropertyChangeListener(this);
	}

	public String getHoverText() {
		return null;
	}

}
