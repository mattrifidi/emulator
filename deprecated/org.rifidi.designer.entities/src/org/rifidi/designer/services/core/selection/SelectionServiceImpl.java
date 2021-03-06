/*
 *  SelectionServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.selection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Selection service implementation.
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public class SelectionServiceImpl implements SelectionService,
		SceneDataChangedListener {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(SelectionServiceImpl.class);
	/** List of selected entities. */
	private List<Entity> hilited = new ArrayList<Entity>();
	/** List of registered selection listeners. */
	private List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();

	/**
	 *  Constructor.
	 */
	public SelectionServiceImpl() {
		logger.debug("SelectionService created");
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.selection.SelectionService#hilite(org
	 * .rifidi.designer.entities.VisualEntity, boolean, boolean)
	 */
	@Override
	public void select(final VisualEntity ent, boolean multiple,
			boolean informlisteners) {
		if (!hilited.contains(ent)) {
			if (!multiple) {
				clearSelection();
			}
			hilited.add(ent);
			if (informlisteners) {
				triggerSelection(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.selection.SelectionService#hilite(java
	 * .util.List, boolean, Object)
	 */
	@Override
	public void select(final List<VisualEntity> entities,
			boolean informlisteners, Object source) {
		hilited.clear();
		for (VisualEntity entity : entities) {
			hilited.add(entity);
		}
		if (informlisteners) {
			triggerSelection(source);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.selection.SelectionService#clearSelection
	 * ()
	 */
	@Override
	public void clearSelection() {
		hilited.clear();
		triggerSelection(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#
	 * sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#
	 * destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		hilited.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.selection.SelectionService#getSelectionList
	 * ()
	 */
	@Override
	public List<Entity> getSelectionList() {
		return new ArrayList<Entity>(hilited);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return new StructuredSelection(hilited);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse
	 * .jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub

	}

	/**
	 * Triggers a selection event that contains the currently highlighted
	 * entities.
	 */
	private void triggerSelection(Object source) {
		for (ISelectionChangedListener listener : selectionListeners) {
			SelectionChangedEvent event = new SelectionChangedEvent(this,
					new StructuredSelection(hilited));
			if (!listener.equals(source)) {
				listener.selectionChanged(event);
			}
		}
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		logger.debug("SelectionService got SceneDataService");
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
	}
}
