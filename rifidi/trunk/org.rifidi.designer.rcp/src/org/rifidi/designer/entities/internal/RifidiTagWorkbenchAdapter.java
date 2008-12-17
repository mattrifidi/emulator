/*
 *  RifidiTagWorkbenchAdapter.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.views.tags.Activator;

/**
 * Workbenchadapter for displaying a RifidiTag bellow the boxproducer it is
 * associated with.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 5, 2008
 * 
 */
public class RifidiTagWorkbenchAdapter implements IWorkbenchAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object arg0) {
		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object
	 * )
	 */
	@Override
	public ImageDescriptor getImageDescriptor(Object arg0) {
		return Activator.getDefault().getImageRegistry().getDescriptor(
				RifidiTagWorkbenchAdapter.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	@Override
	public String getLabel(Object arg0) {
		return ((RifidiTag) arg0).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
