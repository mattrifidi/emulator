/*
 *  DefaultAdapterFactory.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.CableEntity;
import org.rifidi.designer.entities.Entity;

/**
 * Factory for creating the default adapters.
 * 
 * @author Jochen Mader Dec 5, 2007
 * 
 */
public class DefaultAdapterFactory implements IAdapterFactory {

	/**
	 * Array that contains the supported adapters.
	 */
	@SuppressWarnings("unchecked")
	private Class[] adapters = new Class[] { IWorkbenchAdapter.class,
			IActionFilter.class };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(final Object adaptableObject, final Class adapterType) {
		if (adaptableObject instanceof Entity) {
			if (IActionFilter.class.equals(adapterType)) {
				return new EntityGroupActionFilterAdapter();
			}
		} else if (adaptableObject instanceof CableEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new CableEntityWorkbenchAdapter();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return adapters;
	}

}
