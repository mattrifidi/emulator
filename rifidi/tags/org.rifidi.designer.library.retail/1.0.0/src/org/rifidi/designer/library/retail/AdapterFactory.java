/*
 *  AdapterFactory.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.retail;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.properties.DefaultPropertySource;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.designer.library.retail.clothing.ClothingWorkbenchAdapter;
import org.rifidi.designer.library.retail.clothingrack.ClothingRack;
import org.rifidi.designer.library.retail.clothingrack.ClothingRackWorkbenchAdapter;
import org.rifidi.designer.library.retail.retailbox.RetailBox;
import org.rifidi.designer.library.retail.retailbox.RetailBoxWorkbenchAdapter;
import org.rifidi.designer.library.retail.shelf.Shelf;
import org.rifidi.designer.library.retail.shelf.ShelfWorkbenchAdapter;

/**
 * AdapterFactory for the primitves library.
 * 
 * @author Jochen Mader Jan 25, 2008
 * @tags
 * 
 */
public class AdapterFactory implements IAdapterFactory {
	/**
	 * Supported adapters.
	 */
	@SuppressWarnings("unchecked")
	private Class[] adapters = new Class[] { IWorkbenchAdapter.class,
			IPropertySource.class };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof Clothing) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new ClothingWorkbenchAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		}
		if (adaptableObject instanceof ClothingRack) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new ClothingRackWorkbenchAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		}
		if (adaptableObject instanceof Shelf) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new ShelfWorkbenchAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		}
		if (adaptableObject instanceof RetailBox) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new RetailBoxWorkbenchAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
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
	@Override
	public Class[] getAdapterList() {
		return adapters;
	}

}
