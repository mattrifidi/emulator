/*
 *  ClothingRackWorkbenchAdapter.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.retail.clothingrack;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.library.retail.Activator;

/**
 * IWorkbenchAdapter for CardboxEntities.
 * 
 * @see CardboxEntity
 * @author Jochen Mader Oct 8, 2007
 * 
 */
public class ClothingRackWorkbenchAdapter implements IWorkbenchAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object o) {
		return ((ClothingRack) o).getWrappers().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object
	 * )
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		return Activator.getDefault().getImageRegistry().getDescriptor(
				ClothingRack.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	public String getLabel(Object o) {
		return ((ClothingRack) o).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(Object o) {
		return null;
	}

}
