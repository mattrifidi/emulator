/*
 *  RifidiTagAdapterFactory.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * FIXME: Class comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 5, 2008
 * 
 */
public class RifidiTagAdapterFactory implements IAdapterFactory {

	protected Class<?>[] adapterList=new Class<?>[]{IWorkbenchAdapter.class};
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object arg0, Class arg1) {
		if(IWorkbenchAdapter.class.equals(arg1)){
			if(arg0 instanceof RifidiTag){
				return new RifidiTagWorkbenchAdapter();
			}	
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@Override
	public Class<?>[] getAdapterList() {
		return adapterList;
	}

}
