/*
 *  ObservableTreeContentProvider.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.databinding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.databinding.annotations.MonitorThisList;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;

/**
 * This TreeContentProvider takes advantage of eclipse databinding. An object
 * that is given to this contentprovider can use annotations to tell the
 * contentprovider which properties/collections to monitor for changes.
 * 
 * @see MonitoredProperties
 * @see MonitorThisList
 * @author Jochen Mader Jan 17, 2008
 * 
 */

public class ObservableTreeContentProvider implements ITreeContentProvider,
		PropertyChangeListener {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(ObservableTreeContentProvider.class);
	/**
	 * The viewer for the entites.
	 */
	private AbstractTreeViewer viewer;
	/**
	 * A map containing all already created adapters.
	 */
	private Map<Object, Object> adapterCache;
	/**
	 * A map containg all observed elements and their ObserverHelper.
	 */
	private Map<Object, ObserverHelper> observedElements;
	/**
	 * A map containing the bean as key and another map containing name and
	 * listener of its monitored properties.
	 */
	private Map<Object, Map<String, PropertyChangeListenerProxy>> monitoredProperties;

	/**
	 * Constructor.
	 */
	public ObservableTreeContentProvider() {
		adapterCache = new HashMap<Object, Object>();
		observedElements = new HashMap<Object, ObserverHelper>();
		monitoredProperties = new HashMap<Object, Map<String, PropertyChangeListenerProxy>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object parentElement) {
		System.out.println("getChildren");
		monitorElement(parentElement);
		return getAdapter(parentElement).getChildren(parentElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(final Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(final Object element) {
		if (getAdapter(element) != null) {
			monitorElement(element);
			if (getAdapter(element).getChildren(element).length > 0) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(final Object inputElement) {
		monitorElement(inputElement);
		return getAdapter(inputElement).getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		if (this.viewer != viewer) {
			if (viewer instanceof AbstractTreeViewer) {
				this.viewer = (AbstractTreeViewer) viewer;
			} else {
				throw new RuntimeException(
						"ObservableTreeContentProvider supports AbstractTreeViewer but got "
								+ viewer.getClass());
			}
		}
		if (oldInput != newInput) {
			adapterCache = new HashMap<Object, Object>();
			observedElements = new HashMap<Object, ObserverHelper>();
			monitoredProperties = new HashMap<Object, Map<String, PropertyChangeListenerProxy>>();
			viewer.refresh();
		}
	}

	/**
	 * Helper method to ensure that a given Object is being monitored.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	private void monitorElement(final Object element) {
		if (!observedElements.containsKey(element)) {
			if (element.getClass().isAnnotationPresent(MonitorThisList.class)) {
				String name = ((MonitorThisList) (element.getClass()
						.getAnnotation(MonitorThisList.class))).name();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				try {
					observedElements.put(element, new ObserverHelper(element));
					((IObservableList) element.getClass().getMethod(
							"get" + name, null).invoke(element))
							.addListChangeListener(observedElements
									.get(element));
				} catch (Exception e) {
					logger.error(e);
				}
			}
			if (element.getClass().isAnnotationPresent(
					MonitoredProperties.class)
					&& !monitoredProperties.containsKey(element)) {
				Map<String, PropertyChangeListenerProxy> props = new HashMap<String, PropertyChangeListenerProxy>();
				monitoredProperties.put(element, props);
				for (String name : element.getClass().getAnnotation(
						MonitoredProperties.class).names()) {
					PropertyChangeListenerProxy proxy = new PropertyChangeListenerProxy(
							name, this);
					((Entity) element).addPropertyChangeListener(proxy);
					props.put(name, proxy);
				}
			}
		}
	}

	/**
	 * Helper method for retrieving the adapter for an object.
	 * 
	 * @param adaptable
	 * @return
	 */
	private IWorkbenchAdapter getAdapter(Object adaptable) {
		if (adapterCache.containsKey(adaptable)) {
			return (IWorkbenchAdapter) adapterCache.get(adaptable);
		}
		IWorkbenchAdapter adapter;
		if (adaptable instanceof IAdaptable) {
			adapter = (IWorkbenchAdapter) ((IAdaptable) adaptable)
					.getAdapter(IWorkbenchAdapter.class);
		} else {
			adapter = (IWorkbenchAdapter) Platform.getAdapterManager()
					.getAdapter(adaptable, IWorkbenchAdapter.class);
		}
		adapterCache.put(adaptable, adapter);
		return adapter;
	}

	/**
	 * Helper class to monitor changes to writable lists.
	 * 
	 * 
	 * @author Jochen Mader Jan 31, 2008
	 * @tags
	 * 
	 */
	private class ObserverHelper implements IListChangeListener {

		// the element the list is a part of
		private Object element;

		/**
		 * Constructor.
		 * 
		 * @param element
		 */
		@SuppressWarnings("unchecked")
		public ObserverHelper(final Object element) {
			this.element = element;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.databinding.observable.list.IListChangeListener#handleListChange(org.eclipse.core.databinding.observable.list.ListChangeEvent)
		 */
		public void handleListChange(final ListChangeEvent event) {
			if (element instanceof SceneData) {
				for (ListDiffEntry diff : event.diff.getDifferences()) {
					if (diff.isAddition()) {
						viewer.add(((SceneData) element)
								.getGroupedComponentsContainer(), diff
								.getElement());
					} else {
						viewer.remove(((SceneData) element)
								.getGroupedComponentsContainer(),
								new Object[] { diff.getElement() });
					}
				}
				return;
			}
			for (ListDiffEntry diff : event.diff.getDifferences()) {
				if (diff.isAddition()) {
					viewer.add(element, diff.getElement());
				} else {
					viewer.remove(element, new Object[] { diff.getElement() });
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getOldValue() != null) {
			viewer.remove(evt.getSource(), new Object[] { evt.getOldValue() });
		}
		viewer.add(evt.getSource(), evt.getNewValue());
		viewer.refresh(evt.getSource());
	}

}
