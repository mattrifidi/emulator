/*
 *  EntityGroupWorkbenchAdapter.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.adapters;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.rcp.Activator;

/**
 * IWorkbenchAdapter for EntityGroups.
 * 
 * @see EntityGroup
 * @author Jochen Mader Nov 14, 2007
 * 
 */
public class EntityGroupWorkbenchAdapter implements IWorkbenchAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object o) {
		return ((EntityGroup) o).getEntities().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
	 */
	public ImageDescriptor getImageDescriptor(final Object object) {
		if (object instanceof EntityGroup) {
			if ("Ungrouped Components".equals(((EntityGroup) object).getName())) {
				return Activator.getImageDescriptor("icons/shape_ungroup.png");
			} else if ("Generated Components".equals(((EntityGroup) object)
					.getName())) {
				return Activator
						.getImageDescriptor("icons/shape_square_add.png");
			}
		}
		return Activator.getImageDescriptor("icons/shape_square_add.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
	 */
	public String getLabel(final Object o) {
		return ((EntityGroup) o).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
	 */
	public Object getParent(final Object o) {
		return null;
	}

}
