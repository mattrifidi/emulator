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
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public abstract class AbstractMapModelElement implements Serializable,
		IPropertySource {

	public static final String PROP_CHILD = "child", PROP_MOVED = "moved",
			PROP_UPDATE = "update";

	transient protected PropertyChangeSupport listeners;

	static final long serialVersionUID = 1;
	
	public AbstractMapModelElement(){
		init();
	}
	private void init(){
		listeners = new PropertyChangeSupport(this);
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	protected void fireChildAdded(Object child) {
		listeners.firePropertyChange(PROP_CHILD, null, child);
	}

	protected void fireChildRemoved(Object child) {
		listeners.firePropertyChange(PROP_CHILD, child, null);
	}

	protected void firePropertyMoved(Object child) {
		listeners.firePropertyChange(PROP_MOVED, null, child);
	}

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
		// TODO: Remove Listeners
	}

}
