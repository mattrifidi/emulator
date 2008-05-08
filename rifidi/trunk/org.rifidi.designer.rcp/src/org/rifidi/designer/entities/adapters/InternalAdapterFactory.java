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
import org.eclipse.ui.views.properties.IPropertySource;
import org.rifidi.designer.entities.internal.WatchAreaEntity;
import org.rifidi.designer.entities.internal.WatchAreaWorkbenchAdapter;
import org.rifidi.designer.entities.properties.DefaultPropertySource;

/**
 * Factory for creating adapters for built in entities.
 * 
 * @author Jochen Mader Dec 5, 2007
 * 
 */
public class InternalAdapterFactory implements IAdapterFactory {

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
		if (adaptableObject instanceof WatchAreaEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new WatchAreaWorkbenchAdapter();
			}
			else if(IPropertySource.class.equals(adapterType)){
				return new DefaultPropertySource((WatchAreaEntity)adaptableObject);
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
