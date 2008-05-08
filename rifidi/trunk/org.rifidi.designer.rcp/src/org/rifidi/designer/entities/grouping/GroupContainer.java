/*
 *  GroupContainer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.grouping;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.rcp.Activator;

/**
 * A container that is used to organize entity groups.
 * 
 * @see EntityGroup
 * @author Jochen Mader Nov 26, 2007
 * 
 */
public class GroupContainer implements IAdaptable, IWorkbenchAdapter {
	/**
	 * Name of this container
	 */
	private String name;
	/**
	 * The scene this container is associated with.
	 */
	private SceneData sceneData;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param sceneData
	 */
	public GroupContainer(String name, SceneData sceneData) {
		super();
		this.name = name;
		this.sceneData = sceneData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object o) {
		return sceneData.getEntityGroups().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
	 */
	public ImageDescriptor getImageDescriptor(final Object object) {
		return Activator.getImageDescriptor("icons/shape_group.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	public String getLabel(final Object o) {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(final Object o) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(final Class adapter) {
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return this;
		}
		return null;
	}

}
