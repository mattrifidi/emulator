/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class serves as a base class for all model elements. A model element is
 * essentially the "view model". It contains the business information about the
 * object as well as the information needed to display it (such as size and
 * location). It implements IPropertySouce so that interested objects (e.g.
 * controllers) can register themselves as listeners
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public abstract class AbstractMapModelElement implements Serializable,
		IPropertySource {

	/** The property change event when a child is modified */
	public static final String PROP_CHILD = "child";
	/** The property change event when an model element is moved */
	public static final String PROP_MOVED = "moved";
	/** The property change event when a model element is updated */
	public static final String PROP_UPDATE = "update";
	/** Listeners to the properties of this model */
	transient protected PropertyChangeSupport listeners;
	/** The serial version ID */
	static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public AbstractMapModelElement() {
		init();
	}

	/**
	 * Init method that should be called by both constructor and serialization
	 * method
	 */
	private void init() {
		listeners = new PropertyChangeSupport(this);
	}

	/**
	 * Reconstructs the AbstractMapModelElement from serialization
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	/**
	 * Add a property change listener
	 * 
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	/***
	 * Remove a property change listener
	 * 
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	/***
	 * Notify listeners that a child has been added to this model element
	 * 
	 * @param child
	 */
	protected void fireChildAdded(Object child) {
		listeners.firePropertyChange(PROP_CHILD, null, child);
	}

	/***
	 * Notify listeners that a child has been removed from this model element.
	 * 
	 * @param child
	 */
	protected void fireChildRemoved(Object child) {
		listeners.firePropertyChange(PROP_CHILD, child, null);
	}

	/**
	 * Notify listeners that this model element has been moved
	 * 
	 * @param child
	 */
	protected void firePropertyMoved(Object child) {
		listeners.firePropertyChange(PROP_MOVED, null, child);
	}

	/**
	 * Notify listeners that this model element has been updated
	 * 
	 * @param child
	 */
	protected void firePropertyUpdate(Object child) {
		listeners.firePropertyChange(PROP_UPDATE, null, child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
	 * .Object)
	 */
	@Override
	public boolean isPropertySet(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public void resetPropertyValue(Object arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	public void dispose() {
		// TODO: should probably remove listeners
	}

}
