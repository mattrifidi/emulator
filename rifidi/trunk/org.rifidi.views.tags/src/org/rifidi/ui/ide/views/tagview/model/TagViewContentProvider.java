/*
 *  TagViewContentProvider.java
 *
 *  Created:	Sep 21, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.ide.views.tagview.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.registry.ITagRegistry;

/**
 * 
 * This is providing the information about the structure of the TagRegistry
 * Object.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class TagViewContentProvider implements IStructuredContentProvider,
		PropertyChangeListener {
	/** Viewer this provider is associated with. */
	private Viewer viewer = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ITagRegistry) {
			return ((ITagRegistry) inputElement).getTags().toArray();
		}
		if (inputElement instanceof IRifidiTagService) {
			return ((IRifidiTagService) inputElement).getAvailableTags()
					.toArray();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (viewer != null) {
			viewer.refresh();
		}
	}
}
